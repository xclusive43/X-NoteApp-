package com.xclusive.x_note.model;

import java.io.Serializable;

public class Model_notes implements Serializable {
   private String id, notes,color,title,dates,subtitle;

    public Model_notes() {
    }

    public Model_notes(String  id, String notes, String color, String title, String dates, String subtitle) {
        this.notes = notes;
        this.color = color;
        this.title = title;
        this.dates = dates;
        this.subtitle = subtitle;
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
