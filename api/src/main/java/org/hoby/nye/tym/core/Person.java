package org.hoby.nye.tym.core;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Person extends BaseEntity {
    private String first;
    private String last;

    public Person() {
        super();
    }

    public Person(final Long id) {
        super(id);
    }

    public Person(final String first, final String last) {
        this.first = first;
        this.last = last;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(final String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(final String last) {
        this.last = last;
    }
}
