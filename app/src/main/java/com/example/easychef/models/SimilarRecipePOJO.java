package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

public class SimilarRecipePOJO {

    private static final String IMAGE_URL_BASE = "https://spoonacular.com/recipeImages/%d-312x231.%s";

    @SerializedName("title")
    private String title;
    @SerializedName("id")
    private int id;
    @SerializedName("imageType")
    private String imageType;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return String.format(IMAGE_URL_BASE, id, imageType);
    }
}
