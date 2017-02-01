package at.lemme.orm.fluent.test.annotation;

import at.lemme.orm.fluent.api.annotation.Column;
import at.lemme.orm.fluent.api.annotation.Id;
import at.lemme.orm.fluent.impl.metadata.Metadata;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 01.02.2017.
 */
public class TestColumn {

    @Test
    public void testColumnNamesDefault() {
        // GIVEN
        class TestClass {
            @Id
            String theId;

            String id;
        }
        ;
        // WHEN
        Metadata metadata = Metadata.of(TestClass.class);

        // THEN
        assertThat(metadata.columnNames()).contains("theId", "id");
    }

    @Test
    public void testColumnNamesWithAnnotation() {
        // GIVEN
        class TestClass {
            @Id
            @Column(name = "col_theId")
            String theId;

            @Column(name = "col_id")
            String id;
        }
        ;
        // WHEN
        Metadata metadata = Metadata.of(TestClass.class);

        // THEN
        assertThat(metadata.columnNames()).contains("col_theId", "col_id");
    }

    @Test
    public void testColumnNamesMixed() {
        // GIVEN
        class TestClass {
            @Id
            String theId;

            @Column(name = "col_name")
            String name;
        }
        ;
        // WHEN
        Metadata metadata = Metadata.of(TestClass.class);

        // THEN
        assertThat(metadata.columnNames()).contains("theId", "col_name");
    }


}
