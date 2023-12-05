package com.example.agrisite;

public class FOName {
    String full_name_of_user, uid, AgrarianName;

    public FOName(String full_name_of_user, String uid, String AgrarianName) {

        this.full_name_of_user = full_name_of_user;
        this.uid = uid;
        this.AgrarianName = AgrarianName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFull_name_of_user() {
        return full_name_of_user;
    }

    public void setFull_name_of_user(String full_name_of_user) {
        this.full_name_of_user = full_name_of_user;
    }

    public String getAgrarianName() {
        return AgrarianName;
    }

    public void setAgrarianName(String AgrarianName) {
        this.AgrarianName = AgrarianName;
    }
}
