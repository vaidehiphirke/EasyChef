package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

public class RecipeDetailPOJO {

    @SerializedName("url")
    private String url;

    public String getRecipeCardUrl() {
        return url;
    }
}
