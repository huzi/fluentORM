package at.lemme.fluent.orm.metadata.model.onetoone;

import at.lemme.fluent.orm.metadata.model.NoIdEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by thomas on 08.12.16.
 */
@Entity
public class OtoNoIdOnTargetEntity {
    @Id
    private String id;

    @OneToOne
    private NoIdEntity noIdEntity;

    public OtoNoIdOnTargetEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NoIdEntity getNoIdEntity() {
        return noIdEntity;
    }

    public void setNoIdEntity(NoIdEntity noIdEntity) {
        this.noIdEntity = noIdEntity;
    }
}
