package at.lemme.fluent.orm.metadata.model.manytoone;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by thomas on 11.12.16.
 */
@Entity
public class MtoEntity1 {

    @Id
    String id;

    @ManyToOne
    MtoTargetEntity1 target;

    public MtoEntity1() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MtoTargetEntity1 getTarget() {
        return target;
    }

    public void setTarget(MtoTargetEntity1 target) {
        this.target = target;
    }
}
