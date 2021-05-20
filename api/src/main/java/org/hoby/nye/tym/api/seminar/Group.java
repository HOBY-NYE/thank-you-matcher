package org.hoby.nye.tym.api.seminar;

import javax.persistence.Embeddable;

@Embeddable
public class Group {
    private String color;
    private String letter;

    public Group() {

    }

    public Group(final String color, final String letter) {
        this.color = color;
        this.letter = letter;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
