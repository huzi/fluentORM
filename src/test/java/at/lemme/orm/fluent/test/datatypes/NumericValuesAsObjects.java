package at.lemme.orm.fluent.test.datatypes;

import at.lemme.orm.fluent.api.annotation.Id;
import at.lemme.orm.fluent.api.annotation.Table;

import java.util.Objects;

/**
 * Created by thomas18 on 01.02.2017.
 */
@Table(name = "NumericValues")
public class NumericValuesAsObjects {

    @Id
    private Integer id;
    private Long longValue;
    private Short shortValue;
    private Float floatValue;
    private Double doubleValue;

    public NumericValuesAsObjects() {
    }

    public NumericValuesAsObjects(Integer id, Long longValue, Short shortValue, Float floatValue, Double doubleValue) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(longValue);
        Objects.requireNonNull(shortValue);
        Objects.requireNonNull(floatValue);
        Objects.requireNonNull(doubleValue);
        this.id = id;
        this.longValue = longValue;
        this.shortValue = shortValue;
        this.floatValue = floatValue;
        this.doubleValue = doubleValue;
    }

    public Integer getId() {
        return id;
    }

    public Long getLongValue() {
        return longValue;
    }

    public Short getShortValue() {
        return shortValue;
    }

    public Float getFloatValue() {
        return floatValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumericValuesAsObjects that = (NumericValuesAsObjects) o;

        if (!id.equals(that.id)) return false;
        if (!longValue.equals(that.longValue)) return false;
        if (!shortValue.equals(that.shortValue)) return false;
        if (!floatValue.equals(that.floatValue)) return false;
        return doubleValue.equals(that.doubleValue);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + longValue.hashCode();
        result = 31 * result + shortValue.hashCode();
        result = 31 * result + floatValue.hashCode();
        result = 31 * result + doubleValue.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NumericValues{" +
                "id=" + id +
                ", longValue=" + longValue +
                ", shortValue=" + shortValue +
                ", floatValue=" + floatValue +
                ", doubleValue=" + doubleValue +
                '}';
    }
}
