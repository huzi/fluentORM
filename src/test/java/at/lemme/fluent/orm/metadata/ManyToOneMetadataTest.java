package at.lemme.fluent.orm.metadata;

import at.lemme.fluent.orm.metadata.Metadata;
import at.lemme.fluent.orm.metadata.model.InvalidEntity;
import at.lemme.fluent.orm.metadata.model.NoIdEntity;
import at.lemme.fluent.orm.metadata.model.Person;
import at.lemme.fluent.orm.metadata.model.Student;
import at.lemme.fluent.orm.metadata.model.manytoone.MtoEntity1;
import at.lemme.fluent.orm.metadata.model.manytoone.MtoEntity2;
import at.lemme.fluent.orm.metadata.model.onetoone.OtoNoIdOnTargetEntity;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 04.12.16.
 */
public class ManyToOneMetadataTest {

    @Test
    public void testJoinColumnName() {
        // GIVEN
        Class<?> clazz = MtoEntity1.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getColumns()).contains("target_targetId");
    }

    @Test
    public void testJoinColumnNameRenamed() {
        // GIVEN
        Class<?> clazz = MtoEntity2.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getColumns()).contains("targetColumn");
    }

}
