package at.lemme.fluent.orm.metadata;

import at.lemme.fluent.orm.metadata.model.InvalidEntity;
import at.lemme.fluent.orm.metadata.model.NoIdEntity;
import at.lemme.fluent.orm.metadata.model.Person;
import at.lemme.fluent.orm.metadata.model.Student;
import at.lemme.fluent.orm.metadata.model.onetoone.OtoNoIdOnTargetEntity;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas on 04.12.16.
 */
public class MetadataTest {


    @Test
    public void testRetrieveClass() {
        // GIVEN
        Class<?> clazz = Person.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getEntityClass()).isEqualTo(Person.class);
    }

    @Test
    public void testRetrieveTableName() {
        // GIVEN
        Class<?> clazz = Student.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getTableName()).isEqualTo("Student");
    }

    @Test
    public void testRetrieveTableNameFromAnnotation() {
        // GIVEN
        Class<?> clazz = Person.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getTableName()).isEqualTo("Persons");
    }

    @Test
    public void testGetIdClass() {
        // GIVEN
        Class<?> clazz = Student.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getIdClass()).isEqualTo(String.class);
    }

    @Test
    public void testGetIdClassNoId() {
        // GIVEN
        Class<?> clazz = NoIdEntity.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getIdClass()).isNull();
    }

    @Test
    public void testEntityHasNoArgConstructor() {
        // GIVEN
        Class<?> clazz = Person.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        // THEN
        // No exception thrown
        assertThat(true).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEntityHasNoArgConstructorInvalid() {
        // GIVEN
        Class<?> clazz = InvalidEntity.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        // THEN
        // Not reachable - exception thrown
        assertThat(false).isTrue();
    }

    @Test
    public void testListColumns() {
        // GIVEN
        Class<?> clazz = Student.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getColumns()).hasSize(4);
    }

    @Test
    public void testListColumnsRenameColumn() {
        // GIVEN
        Class<?> clazz = Person.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getColumns()).contains("birthDate");
    }

    @Test
    public void testListColumnsOneToOneJoincolumn() {
        // GIVEN
        Class<?> clazz = Student.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getColumns()).contains("address_addressId");
    }

    @Test
    public void testListColumnsOneToOneJoincolumnInvalidTargetType() {
        // GIVEN
        Class<?> clazz = OtoNoIdOnTargetEntity.class;
        String message = null;

        // WHEN
        try {
            new Metadata(clazz);
        } catch (Exception e) {
            message = e.getMessage();
        }

        //THEN
        assertThat(message).startsWith("No Id attribute on target entity ");
    }

    @Test
    public void testListColumnsOneToOneJoincolumnRenamed() {
        // GIVEN
        Class<?> clazz = Person.class;

        // WHEN
        Metadata metadata = new Metadata(clazz);

        //THEN
        assertThat(metadata.getColumns()).contains("addressid");
    }
}
