package at.lemme.orm.fluent.test.annotation;

import at.lemme.orm.fluent.api.annotation.Id;
import at.lemme.orm.fluent.impl.metadata.Metadata;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 01.02.2017.
 */
public class TestId {

    @Test
    public void testIdAttributeWithAnnotation() {
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
        assertThat(metadata.id().getName()).isEqualTo("theId");
    }

    @Test
    public void testIdAttributeWithoutAnnotation() {
        // GIVEN
        class TestClass {
            String theId;
            String id;
        }
        // WHEN
        Metadata metadata = Metadata.of(TestClass.class);

        // THEN
        assertThat(metadata.id().getName()).isEqualTo("id");
    }


    @Test(expected = RuntimeException.class)
    public void testIdAttributeWithoutId() {
        // GIVEN
        class TestClass {
            String theId;
        }
        // WHEN
        Metadata metadata = Metadata.of(TestClass.class);

        // THEN
        assertThat(true).isFalse();
    }


}
