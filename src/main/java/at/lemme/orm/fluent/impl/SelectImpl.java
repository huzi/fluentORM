package at.lemme.orm.fluent.impl;

import at.lemme.orm.fluent.api.Condition;
import at.lemme.orm.fluent.api.Order;
import at.lemme.orm.fluent.api.Select;
import at.lemme.orm.fluent.impl.metadata.Attribute;
import at.lemme.orm.fluent.impl.metadata.Metadata;
import at.lemme.orm.fluent.impl.metadata.Relation;
import at.lemme.orm.fluent.impl.select.FetchRelation;
import at.lemme.orm.fluent.impl.select.Limit;
import at.lemme.orm.fluent.impl.select.OrderBy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static at.lemme.orm.fluent.impl.metadata.Relation.Type.ManyToOne;
import static at.lemme.orm.fluent.impl.metadata.Relation.Type.OneToMany;
import static at.lemme.orm.fluent.impl.util.JdbcUtil.printColumns;
import static at.lemme.orm.fluent.impl.util.JdbcUtil.printRow;

/**
 * Created by thomas on 22.01.17.
 */
public class SelectImpl<T> implements Select<T> {


    private final Connection connection;
    private final Class<?> entityClass;
    private final Metadata metadata;

    private static final String alias = "t";
    private final String idAlias;
    private final String attributeString;


    private List<FetchRelation> fetchRelations = Collections.emptyList();
    private Limit limit;
    private OrderBy order;
    private Condition condition = Condition.empty();

    public SelectImpl(Connection connection, Class<?> clazz) {
        this.connection = connection;
        entityClass = clazz;
        metadata = Metadata.of(entityClass);
        idAlias = "fluent_" + alias + "_" + metadata.id().columnName();
        attributeString = metadata.columnNames().stream()
                .map(c -> alias + "." + c + " AS fluent_" + alias + "_" + c)
                .collect(Collectors.joining(", "));
    }

    @Override
    public Select<T> with(String... relations) {
        List<String> relationList = Arrays.asList(relations);
        AtomicInteger index = new AtomicInteger();
        fetchRelations = metadata.relations().stream()
                .filter(r -> relationList.contains(r.name()))
                .map(r -> FetchRelation.of(index.getAndIncrement(), r))
                .collect(Collectors.toList());
        return this;
    }

    @Override
    public Select<T> where(Condition condition) {
        this.condition = condition;
        return this;
    }

    @Override
    public Select<T> orderBy(String attribute, Order order) {
        this.order = new OrderBy(metadata, attribute, order);
        return this;
    }

    @Override
    public Select<T> limit(int limit) {
        this.limit = new Limit(limit);
        return this;
    }

    @Override
    public Select<T> limit(int limit, int offset) {
        this.limit = new Limit(limit, offset);
        return (Select<T>) this;
    }

    @Override
    public List<T> fetch() {
        Parameters parameters = new Parameters();
        StringBuilder sql = buildSql(parameters);

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            parameters.apply(stmt);
            System.out.println(stmt);
            return executeAndFetch(stmt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<T> executeAndFetch(PreparedStatement stmt) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> resultList = new ArrayList<>();

        List<String> relationIdAliases = fetchRelations.stream()
                .map(FetchRelation::idColumnAlias)
                .collect(Collectors.toList());
        Map<String, List<String>> fetchedIds = new HashMap<>();
        fetchedIds.put(idAlias, new ArrayList<>());
        relationIdAliases.forEach(alias -> fetchedIds.put(alias, new ArrayList<>()));

        try (ResultSet resultSet = stmt.executeQuery()) {
            printColumns(resultSet);
            while (resultSet.next()) {
                fetchRow(resultSet, resultList, fetchedIds);
            }
        }
        return resultList;
    }

    private void fetchRow(ResultSet resultSet, List<T> resultList, Map<String, List<String>> fetchedIds) throws InstantiationException, IllegalAccessException, SQLException {


        printRow(resultSet);
        // first, fetch the root object
        final T parent;
        final String rootId = resultSet.getString(idAlias);
        if (!fetchedIds.get(idAlias).contains(rootId)) {
            parent = (T) metadata.getEntityClass().newInstance();
            for (Attribute attribute : metadata.columnAttributes()) {
                attribute.setAttribute(parent, resultSet, alias);
            }
            fetchedIds.get(idAlias).add(rootId);
            resultList.add(parent);
        } else {
            parent = resultList.stream().filter(o -> metadata.id().getValue(o).toString().equals(rootId)).findFirst().get();
        }

        // then fetch related objects
        for (FetchRelation relation : fetchRelations) {
            final String relationIdValue = resultSet.getString(relation.idColumnAlias());
            if (relationIdValue == null) {
                continue;
            }
            System.out.println("relationIdValue:" + relationIdValue);
            if (!fetchedIds.get(relation.idColumnAlias()).contains(relationIdValue)) {
                // Fetch object
                Object child = relation.relation().referencedMetadata().getEntityClass().newInstance();
                for (Attribute attribute : relation.relation().referencedMetadata().columnAttributes()) {
                    attribute.setAttribute(child, resultSet, relation.tableAlias());
                }
                System.out.println(child.toString());
                fetchedIds.get(relation.idColumnAlias()).add(relationIdValue);

                // Connect related Objects
                if (OneToMany.equals(relation.type())) {
                    final Attribute attribute = relation.relation().attribute();
                    if (attribute.getValue(parent) == null) {
                        attribute.setValue(parent, new ArrayList<>());
                    }
                    ((List) attribute.getValue(parent)).add(child);
                    relation.relation().referencedMetadata().getAttribute(relation.relation().mappedBy()).setValue(child, parent);
                } else if (ManyToOne.equals(relation.type())) {
                    final Attribute attribute = relation.relation().attribute();
                    attribute.setValue(parent, child);
                }
            }
        }
    }

    private StringBuilder buildSql(Parameters parameters) {
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(attributeString);

        if (!fetchRelations.isEmpty()) {
            fetchRelations.forEach(relation -> sql.append(',').append(relation.attributeString()));
        }

        sql.append(" FROM ").append(metadata.tableName()).append(' ').append(alias);

        if (!fetchRelations.isEmpty()) {
            fetchRelations.forEach(relation -> {
                String joinAlias = relation.tableAlias();
                Relation joinAttribute = relation.relation();

                sql.append(" LEFT JOIN ").append(joinAttribute.referencedTable()).append(' ').append(joinAlias);
                sql.append(" ON (");
                sql.append(alias).append('.').append(joinAttribute.column());
                sql.append('=');
                sql.append(joinAlias).append('.').append(joinAttribute.referencedColumn()).append(')');
            });
        }

        sql.append(" WHERE ").append(condition.toSql(metadata, parameters));
        if (order != null) {
            sql.append(" ORDER BY ").append(order.column()).append(' ').append(order.order());
        }
        if (limit != null) {
            sql.append(" LIMIT ").append(limit.limit());
            sql.append(" OFFSET ").append(limit.offset());
        }
        return sql;
    }
}
