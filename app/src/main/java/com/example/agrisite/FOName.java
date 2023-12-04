package com.example.agrisite;

public class FOName {
    String full_name_of_user, uid;

    public FOName(String full_name_of_user, String uid) {

        this.full_name_of_user = full_name_of_user;
        this.uid = uid;
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
}
