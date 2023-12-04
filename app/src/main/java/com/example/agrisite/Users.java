package com.example.agrisite;

public class Users {

    String key,full_name_of_user, selectedDivision, selectedProvince, selectedRole, selectedVSDomain,user_password,user_confirmP, user_name_of_user;

    public Users(String uniqueKey,String full_name_of_user, String selectedDivision, String selectedProvince, String selectedRole, String selectedVSDomain, String user_password, String user_confirmP, String user_name_of_user) {
        this.key = uniqueKey;
        this.full_name_of_user = full_name_of_user;
        this.selectedDivision = selectedDivision;
        this.selectedProvince = selectedProvince;
        this.selectedRole = selectedRole;
        this.selectedVSDomain = selectedVSDomain;
        this.user_password = user_password;
        this.user_confirmP = user_confirmP;
        this.user_name_of_user = user_name_of_user;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String uniqueKey) {
        this.key = uniqueKey;
    }

    public String getFull_name_of_user() {
        return full_name_of_user;
    }

    public void setFull_name_of_user(String full_name_of_user) {
        this.full_name_of_user = full_name_of_user;
    }

    public String getSelectedDivision() {
        return selectedDivision;
    }

    public void setSelectedDivision(String selectedDivision) {
        this.selectedDivision = selectedDivision;
    }

    public String getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(String selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    public String getSelectedVSDomain() {
        return selectedVSDomain;
    }

    public void setSelectedVSDomain(String selectedVSDomain) {
        this.selectedVSDomain = selectedVSDomain;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_confirmP() {
        return user_confirmP;
    }

    public void setUser_confirmP(String user_confirmP) {
        this.user_confirmP = user_confirmP;
    }

    public String getUser_name_of_user() {
        return user_name_of_user;
    }

    public void setUser_name_of_user(String user_name_of_user) {
        this.user_name_of_user = user_name_of_user;
    }
}
