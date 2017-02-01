package at.lemme.orm.fluent.test.datatypes;

import at.lemme.orm.fluent.api.annotation.Id;
import at.lemme.orm.fluent.api.annotation.Table;

/**
 * Created by thomas18 on 01.02.2017.
 */
@Table(name = "NumericValues")
public class NumericValues {

    @Id
    private int id;
    private long longValue;
    private short shortValue;
    private float floatValue;
    private double doubleValue;

    public NumericValues() {
    }

    public NumericValues(int id, long longValue, short shortValue, float floatValue, double doubleValue) {
        this.id = id;
        this.longValue = longValue;
        this.shortValue = shortValue;
        this.floatValue = floatValue;
        this.doubleValue = doubleValue;
    }

    public int getId() {
        return id;
    }

    public long getLongValue() {
        return longValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumericValues that = (NumericValues) o;

        if (id != that.id) return false;
        if (longValue != that.longValue) return false;
        if (shortValue != that.shortValue) return false;
        if (Float.compare(that.floatValue, floatValue) != 0) return false;
        return Double.compare(that.doubleValue, doubleValue) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (int) (longValue ^ (longValue >>> 32));
        result = 31 * result + (int) shortValue;
        result = 31 * result + (floatValue != +0.0f ? Float.floatToIntBits(floatValue) : 0);
        temp = Double.doubleToLongBits(doubleValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
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
