package com.mindyourlovedone.healthcare.model;

import java.io.Serializable;

/**
 * Created by welcome on 9/22/2017.
 */

public class Implant implements Serializable {
    String other = "--";
    int userId;
    int id;
    String name = "--";
    String date = "--";

    String location = "--";

    public String getLocation() {
        if (location!=null) {
            if (location.isEmpty()) {
                return "--";
            }
        }
        else return "--";
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        if (details!=null) {
            if (details.isEmpty()) {
                return "--";
            }
        }
        else return "--";
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getNotes() {
        if (notes!=null) {
            if (notes.isEmpty()) {
                return "--";
            }
        } else return "--";
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    String details = "--";
    String notes = "--";

    public String getOther() {
        if (other!=null) {
            if (other.isEmpty()) {
                return "--";
            }
        } else return "--";
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if (name!=null) {
            if (name.isEmpty()) {
                return "--";
            }
        } else return "--";
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        if (date!=null) {
            if (date.isEmpty()) {
                return "--";
            }
        } else return "--";
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
