package at.lemme.orm.fluent.test;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by thomas on 22.01.17.
 */
public class Person {

    private String id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDateTime lastLogin;
    private int loginCount;

    public Person() {
    }

    public Person(String id, String firstName, String lastName, LocalDate birthDate, LocalDateTime lastLogin, int loginCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.lastLogin = lastLogin;
        this.loginCount = loginCount;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public int getLoginCount() {
        return loginCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (loginCount != person.loginCount) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) return false;
        if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;
        if (birthDate != null ? !birthDate.equals(person.birthDate) : person.birthDate != null) return false;
        return lastLogin != null ? lastLogin.withNano(0).equals(person.lastLogin.withNano(0)) : person.lastLogin == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + loginCount;
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", lastLogin=" + lastLogin +
                ", loginCount=" + loginCount +
                '}';
    }
}
