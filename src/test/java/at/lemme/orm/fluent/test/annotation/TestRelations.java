package at.lemme.orm.fluent.test.annotation;

import at.lemme.orm.fluent.api.annotation.ManyToOne;
import at.lemme.orm.fluent.api.annotation.OneToMany;
import at.lemme.orm.fluent.api.annotation.Table;
import at.lemme.orm.fluent.impl.metadata.Metadata;
import at.lemme.orm.fluent.impl.metadata.Relation;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by thomas18 on 02.02.2017.
 */
public class TestRelations {

    class Person {
        int id;
        @OneToMany(mappedBy = "person")
        List<Subscription> subscriptions;
    }

    class Subscription {
        int id;
        @ManyToOne
        Person person;
    }

    @Table(name = "tablePerson")
    class PersonRenamed {
        int id;
        @OneToMany(mappedBy = "person")
        List<SubscriptionRenamed> subscriptions;
    }

    @Table(name = "tableSubscription")
    class SubscriptionRenamed {
        int id;
        @ManyToOne(column = "personJoinColumn")
        PersonRenamed person;
    }

    @Test
    public void testOneToMany() {
        // WHEN
        Metadata metadata = Metadata.of(Person.class);
        List<Relation> relations = metadata.relations();

        // THEN
        assertThat(relations).hasSize(1);
        assertThat(relations.get(0).table()).isEqualTo("Person");
        assertThat(relations.get(0).column()).isEqualTo("id");
        assertThat(relations.get(0).referencedTable()).isEqualTo("Subscription");
        assertThat(relations.get(0).referencedColumn()).isEqualTo("person");
    }

    @Test
    public void testManyToOne() {
        // WHEN
        Metadata metadata = Metadata.of(Subscription.class);
        List<Relation> relations = metadata.relations();

        // THEN
        assertThat(relations).hasSize(1);
        assertThat(relations.get(0).table()).isEqualTo("Subscription");
        assertThat(relations.get(0).column()).isEqualTo("person");
        assertThat(relations.get(0).referencedTable()).isEqualTo("Person");
        assertThat(relations.get(0).referencedColumn()).isEqualTo("id");
    }

    @Test
    public void testOneToManyRenamed() {
        // WHEN
        Metadata metadata = Metadata.of(PersonRenamed.class);
        List<Relation> relations = metadata.relations();

        // THEN
        assertThat(relations).hasSize(1);
        assertThat(relations.get(0).table()).isEqualTo("tablePerson");
        assertThat(relations.get(0).column()).isEqualTo("id");
        assertThat(relations.get(0).referencedTable()).isEqualTo("tableSubscription");
        assertThat(relations.get(0).referencedColumn()).isEqualTo("personJoinColumn");
    }

    @Test
    public void testManyToOneRenamed() {
        // WHEN
        Metadata metadata = Metadata.of(SubscriptionRenamed.class);
        List<Relation> relations = metadata.relations();

        // THEN
        assertThat(relations).hasSize(1);
        assertThat(relations.get(0).table()).isEqualTo("tableSubscription");
        assertThat(relations.get(0).column()).isEqualTo("personJoinColumn");
        assertThat(relations.get(0).referencedTable()).isEqualTo("tablePerson");
        assertThat(relations.get(0).referencedColumn()).isEqualTo("id");
    }
}
