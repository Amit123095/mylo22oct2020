package com.mindyourlovedone.healthcare.model;

import com.mindyourlovedone.healthcare.DashBoard.DateClass;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by welcome on 10/12/2017.
 */

public class Appoint implements Serializable {
    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    boolean isOpen = false;
    int unique;
    String Pre = "";
    int id;
    int userid;
    String type = "";
    ArrayList<DateClass> dateList = new ArrayList<>();
    String otherDoctor = "";
    String OtherFrequency = "";
    String Doctor = "";
    String Date = "";
    String frequency = "";

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    String note="";

    public int getUnique() {
        return unique;
    }

    public void setUnique(int unique) {
        this.unique = unique;
    }

    public String getPre() {
        return Pre;
    }

    public void setPre(String pre) {
        Pre = pre;
    }

    public ArrayList<DateClass> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<DateClass> dateList) {
        this.dateList = dateList;
    }

    public String getOtherDoctor() {
        return otherDoctor;
    }

    public void setOtherDoctor(String otherDoctor) {
        this.otherDoctor = otherDoctor;
    }

    public String getOtherFrequency() {
        return OtherFrequency;
    }

    public void setOtherFrequency(String otherFrequency) {
        OtherFrequency = otherFrequency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoctor() {
        return Doctor;
    }

    public void setDoctor(String doctor) {
        Doctor = doctor;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
