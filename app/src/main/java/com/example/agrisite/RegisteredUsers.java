package com.example.agrisite;

public class RegisteredUsers {
    String full_name_of_user,user_name_of_user, selectedRole, user_password,user_confirmP,selectedProvince, selectedDivision, selectedVSDomain;

    public RegisteredUsers(String full_name_of_user, String user_name_of_user, String user_password,String selectedRole, String user_confirmP, String selectedProvince, String selectedDivision, String selectedVSDomain) {
        this.full_name_of_user = full_name_of_user;
        this.user_name_of_user = user_name_of_user;
        this.selectedRole = selectedRole;
        this.user_password = user_password;
        this.user_confirmP = user_confirmP;
        this.selectedProvince = selectedProvince;
        this.selectedDivision = selectedDivision;
        this.selectedVSDomain = selectedVSDomain;
    }

    public String getFull_name_of_user() {
        return full_name_of_user;
    }

    public void setFull_name_of_user(String full_name_of_user) {
        this.full_name_of_user = full_name_of_user;
    }

    public String getUser_name_of_user() {
        return user_name_of_user;
    }

    public void setUser_name_of_user(String user_name_of_user) {
        this.user_name_of_user = user_name_of_user;
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

    public String getSelectedProvince() {
        return selectedProvince;
    }

    public void setSelectedProvince(String selectedProvince) {
        this.selectedProvince = selectedProvince;
    }

    public String getSelectedDivision() {
        return selectedDivision;
    }

    public void setSelectedDivision(String selectedDivision) {
        this.selectedDivision = selectedDivision;
    }

    public String getSelectedVSDomain() {
        return selectedVSDomain;
    }

    public void setSelectedVSDomain(String selectedVSDomain) {
        this.selectedVSDomain = selectedVSDomain;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }
}

