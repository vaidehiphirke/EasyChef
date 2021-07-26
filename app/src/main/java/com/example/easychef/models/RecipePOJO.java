package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

public class RecipePOJO {

    @SerializedName("title")
    private String title;
    @SerializedName("id")
    private int id;
    @SerializedName("image")
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
