package at.lemme.fluent.orm.metadata.model.manytoone;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by thomas on 11.12.16.
 */
@Entity
public class MtoEntity2 {

    @Id
    String id;

    @ManyToOne
    @JoinColumn(name = "targetColumn")
    MtoTargetEntity2 target;

    public MtoEntity2() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MtoTargetEntity2 getTarget() {
        return target;
    }

    public void setTarget(MtoTargetEntity2 target) {
        this.target = target;
    }
}
