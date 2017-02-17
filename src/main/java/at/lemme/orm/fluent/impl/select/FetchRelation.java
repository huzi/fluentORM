package at.lemme.orm.fluent.impl.select;

import at.lemme.orm.fluent.impl.metadata.Relation;

import java.util.stream.Collectors;

/**
 * Created by thomas18 on 14.02.2017.
 */
public class FetchRelation {

    private final int index;
    private final String tableAlias;
    private final String idColumnAlias;
    private final String attributeString;
    private final Relation relation;

    public FetchRelation(int index, Relation to) {
        this.index = index;
        this.tableAlias = "j" + index;
        this.idColumnAlias = "fluent_j" + index + '_' + to.referencedMetadata().id().columnName();
        this.attributeString = to.referencedMetadata().columnNames().stream()
                .map(c -> tableAlias + "." + c + " AS fluent_" + tableAlias + "_" + c).collect(Collectors.joining(", "));
        this.relation = to;
    }

    public static FetchRelation of(int index, Relation to) {
        return new FetchRelation(index, to);
    }

    public int index() {
        return index;
    }

    public String tableAlias() {
        return tableAlias;
    }

    public String idColumnAlias() {
        return idColumnAlias;
    }

    public String attributeString() {
        return attributeString;
    }

    public Relation relation() {
        return relation;
    }
    public Relation.Type type() {
        return relation.type();
    }
}
