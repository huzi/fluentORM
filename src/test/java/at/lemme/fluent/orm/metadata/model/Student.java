package at.lemme.fluent.orm.metadata.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * Test Entity. Everything straight forward.
 */
@Entity
public class Student {

    @Id
    String id;
    String studentId;

    @OneToOne
    Address address;

    @ManyToOne
    University university;

    public Student() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
