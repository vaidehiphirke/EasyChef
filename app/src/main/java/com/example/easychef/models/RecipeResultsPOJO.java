package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeResultsPOJO {

    @SerializedName("results")
    private List<RecipePOJO> results;

    public List<RecipePOJO> getResults() {
        return results;
    }
}
