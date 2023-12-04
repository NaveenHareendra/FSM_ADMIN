package com.example.agrisite;

public class Divisions {

    String District;
    int Count;

    public Divisions() {
        // Default constructor required for calls to DataSnapshot.getValue(Divisions.class)
    }

    public Divisions(String District, int Count) {
        this.District = District;
        this.Count = Count;
    }

    public int getCount() {
        return Count;
    }

    public String getDistrict() {
        return District;
    }
}
