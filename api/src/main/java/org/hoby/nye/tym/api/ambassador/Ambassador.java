package org.hoby.nye.tym.api.ambassador;

import org.hoby.nye.tym.api.seminar.Group;
import org.hoby.nye.tym.core.Person;

import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public class Ambassador extends Person {
    @Embedded
    private Group group;

    protected Ambassador() {
        super();
    }

    public Ambassador(Long id) {
        super(id);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
