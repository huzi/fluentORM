package at.lemme.orm.fluent.test.annotation;

import at.lemme.orm.fluent.api.annotation.Table;
import at.lemme.orm.fluent.impl.metadata.Metadata;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 01.02.2017.
 */
public class TestTable {

    private class Person {
        String id;
    }

    @Table(name = "PersonTable")
    private class PersonRenamed {
        String id;
    }


    @Table()
    private class PersonRenamedEmpty {
        String id;
    }

    @Test
    public void testTableNameWithoutAnnotation() {
        //When
        Metadata metadata = Metadata.of(Person.class);

        //Then
        assertThat(metadata.tableName()).isEqualTo("Person");
    }

    @Test
    public void testTableNameWithAnnotation() {
        //When
        Metadata metadata = Metadata.of(PersonRenamed.class);

        //Then
        assertThat(metadata.tableName()).isEqualTo("PersonTable");
    }

    @Test
    public void testTableNameWithAnnotationEmpty() {
        //When
        Metadata metadata = Metadata.of(PersonRenamedEmpty.class);

        //Then
        assertThat(metadata.tableName()).isEqualTo("PersonRenamedEmpty");
    }

}
