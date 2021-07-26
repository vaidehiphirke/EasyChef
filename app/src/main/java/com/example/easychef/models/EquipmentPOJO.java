package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

public class EquipmentPOJO {

    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("image")
    private String imageUrl;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
