package at.lemme.fluent.orm.metadata.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Test Entity.
 * <p>
 * Special cases like renaming table, columns, join-column etc.
 */
@Entity
@Table(name = "Persons")
public class Person {

    @Id
    String id;

    @Column(name = "givenName")
    String firstName;

    String lastName;

    @Column(name = "birthDate")
    LocalDate birthDay;

    @OneToOne
    @JoinColumn(name = "addressid")
    private Address address;

    public Person() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
