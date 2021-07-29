package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

public class ExtendedIngredientPOJO {

    @SerializedName("image")
    private String image;
    @SerializedName("originalString")
    private String originalString;

    public String getImage() {
        return image;
    }

    public String getOriginalString() {
        return originalString;
    }
}
