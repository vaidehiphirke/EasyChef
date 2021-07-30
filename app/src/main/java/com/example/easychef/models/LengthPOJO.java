package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

public class LengthPOJO {

    @SerializedName("number")
    private int number;
    @SerializedName("unit")
    private String unit;

    public String getUnit() {
        return unit;
    }

    public int getNumber() {
        return number;
    }
}
