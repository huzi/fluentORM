package at.lemme.fluent.orm.metadata.model.manytoone;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by thomas on 11.12.16.
 */
@Entity
public class MtoTargetEntity2 {
    @Id
    String targetId;

    String name;

    public MtoTargetEntity2() {
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
