package com.example.agrisite;

public class TaskItems {
    private String key;
    String title, description, startdate, enddate, taskStatus, fullName, DivisionFromDB, VSDomainFromDB,userIDFromDB;

    public TaskItems(String uniqueKey,String title, String description, String startdate, String enddate, String taskStatus,String fullName , String DivisionFromDB,String VSDomainFromDB,String userIDFromDB ) {

        this.key = uniqueKey;
        this.title = title;
        this.description = description;
        this.startdate = startdate;
        this.enddate = enddate;
        this.taskStatus = taskStatus;
        this.fullName = fullName;
        this.DivisionFromDB = DivisionFromDB;
        this.VSDomainFromDB = VSDomainFromDB;
        this.userIDFromDB =  userIDFromDB;
    }

    // Required no-argument constructor
    public TaskItems() {
        // Default constructor required for calls to DataSnapshot.getValue(Tasks.class)
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getTaskstatus() {
        return taskStatus;
    }

    public void setTaskstatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String uniqueKey) {
        this.key = uniqueKey;

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getVSDomainFromDB() {
        return VSDomainFromDB;
    }

    public void setVSDomainFromDB(String VSDomainFromDB) {
        this.VSDomainFromDB =  VSDomainFromDB;
    }

    public String getUserIDFromDB() {
        return userIDFromDB;
    }

    public void setUserIDFromDB(String userIDFromDB) {
        this.userIDFromDB = userIDFromDB;
    }


    public String getDivisionFromDB() {
        return DivisionFromDB;
    }

    public void setDivisionFromDB(String divisionFromDB) {
        DivisionFromDB = divisionFromDB;
    }
}
