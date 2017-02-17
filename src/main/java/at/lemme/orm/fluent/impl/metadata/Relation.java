package at.lemme.orm.fluent.impl.metadata;

import at.lemme.orm.fluent.api.annotation.ManyToOne;
import at.lemme.orm.fluent.api.annotation.OneToMany;

import java.lang.reflect.ParameterizedType;

import static at.lemme.orm.fluent.impl.metadata.Relation.Type.ManyToOne;
import static at.lemme.orm.fluent.impl.metadata.Relation.Type.OneToMany;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class Relation {

    public enum Type {
        ManyToOne, OneToMany
    }

    final Attribute attribute;
    final Type type;
    private final Metadata referencedMetadata;
    private final String mappedBy;

    private Relation(Attribute attribute) {
        this.attribute = attribute;
        if (attribute.field().isAnnotationPresent(ManyToOne.class)) {
            this.type = ManyToOne;
            mappedBy = null;
            referencedMetadata = Metadata.of(attribute.field().getType());
        } else if (attribute.field().isAnnotationPresent(OneToMany.class)) {
            this.type = OneToMany;
            mappedBy = attribute.field().getAnnotation(OneToMany.class).mappedBy();
            Class<?> referencedType = (Class<?>) ((ParameterizedType) attribute.field().getGenericType()).getActualTypeArguments()[0];
            referencedMetadata = Metadata.of(referencedType);
        } else {
            throw new RuntimeException("Field has no relation annotation! " + attribute.name());
        }

    }

    public static Relation of(Attribute attribute) {
        return new Relation(attribute);
    }

    public Attribute attribute() {
        return attribute;
    }

    public String name(){
        return attribute.name();
    }

    public Type type(){
        return type;
    }

    public Metadata referencedMetadata() {
        return referencedMetadata;
    }

    public String referencedTable() {
        return referencedMetadata.tableName();
    }

    public String referencedColumn() {
        if (OneToMany.equals(type)) {
            return referencedMetadata.columnForAttribute(mappedBy);
        } else {
            return referencedMetadata.id().columnName();
        }
    }

    public String table() {
        return Metadata.of(attribute.field().getDeclaringClass()).tableName();
    }

    public String column() {
        if(OneToMany.equals(type)){
            return Metadata.of(attribute.field().getDeclaringClass()).id().columnName();
        }else{
            return attribute.columnName();
        }
    }

    public String mappedBy() {
        return mappedBy;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "type=" + type +
                ", referencedMetadata=" + referencedMetadata.tableName() +
                '}';
    }
}
