package at.lemme.orm.fluent.impl.select;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class Limit {
    int limit = 1000;
    int offset = 0;

    public Limit(int limit) {
        this.limit = limit;
    }

    public Limit(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public int limit() {
        return limit;
    }

    public int offset() {
        return offset;
    }
}