package at.lemme.orm.fluent.test.datatypes;

import at.lemme.orm.fluent.BaseDbTest;
import at.lemme.orm.fluent.api.Conditions;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 01.02.2017.
 */
public class TestInsertNumericValues extends BaseDbTest {

    @Test
    public void testInsert() {
        // GIVEN
        NumericValues nv = new NumericValues(1, 100, (short) 10, 10.10f, 100.100d);

        // WHEN
        fluent.insert(nv).execute();

        // THEN
        NumericValues storedNv = (NumericValues) fluent.select(NumericValues.class).where(Conditions.equals("id", 1)).fetch().get(0);
        assertThat(storedNv).isEqualTo(nv);
    }

    @Test
    public void testInsertMultiple() {
        // GIVEN
        NumericValues nv1 = new NumericValues(Integer.MIN_VALUE, -100, Short.MIN_VALUE, Float.MIN_VALUE, Double.MIN_VALUE);
        NumericValues nv2 = new NumericValues(0, 0, (short) 0, 0f, 0d);
        NumericValues nv3 = new NumericValues(Integer.MAX_VALUE, 100, Short.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE);

        // WHEN
        fluent.insert(nv1, nv2, nv3).execute();

        // THEN
        List<NumericValues> storedNv = fluent.select(NumericValues.class).fetch();
        assertThat(storedNv).contains(nv1);
        assertThat(storedNv).contains(nv2);
        assertThat(storedNv).contains(nv3);
    }

    @Test
    public void testInsertAsObjects() {
        // GIVEN
        NumericValuesAsObjects nv = new NumericValuesAsObjects(1, 100l, (short) 10, 10.10f, 100.100d);

        // WHEN
        fluent.insert(nv).execute();

        // THEN
        NumericValuesAsObjects storedNv = (NumericValuesAsObjects) fluent.select(NumericValuesAsObjects.class).where(Conditions.equals("id", 1)).fetch().get(0);
        assertThat(storedNv).isEqualTo(nv);
    }

    @Test
    public void testInsertMultipleAsObjects() {
        // GIVEN
        NumericValuesAsObjects nv1 = new NumericValuesAsObjects(Integer.MIN_VALUE, Long.MIN_VALUE, Short.MIN_VALUE, Float.MIN_VALUE, Double.MIN_VALUE);
        NumericValuesAsObjects nv2 = new NumericValuesAsObjects(0, 0l, (short) 0, 0f, 0d);
        NumericValuesAsObjects nv3 = new NumericValuesAsObjects(Integer.MAX_VALUE, Long.MAX_VALUE, Short.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE);

        // WHEN
        fluent.insert(nv1, nv2, nv3).execute();

        // THEN
        List<NumericValuesAsObjects> storedNv = fluent.select(NumericValuesAsObjects.class).fetch();
        System.out.println(storedNv);
        assertThat(storedNv).contains(nv1);
        assertThat(storedNv).contains(nv2);
        assertThat(storedNv).contains(nv3);
    }
}
