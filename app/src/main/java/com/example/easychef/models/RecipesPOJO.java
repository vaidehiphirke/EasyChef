package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipesPOJO {

    @SerializedName("recipes")
    private List<RecipePOJO> recipes;

    public List<RecipePOJO> getRecipes() {
        return recipes;
    }
}
