package com.mindyourlovedone.healthcare.model;

import java.io.Serializable;

/**
 * Created by welcome on 9/22/2017.
 */

public class Allergy implements Serializable {

    int userId;
    int id;
    String allergy = "--";
    String treatment = "--";
    String reaction = "--";
    String otherReaction = "--";

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

    public String getAllergy() {
        if(allergy.isEmpty()){
            return "--";
        }
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getTreatment() {
        if(treatment.isEmpty()){
            return "--";
        }
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getReaction() {
        if(reaction.isEmpty()){
            return "--";
        }
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getOtherReaction() {
        if(otherReaction.isEmpty()){
            return "--";
        }
        return otherReaction;
    }

    public void setOtherReaction(String otherReaction) {
        this.otherReaction = otherReaction;
    }
}
