package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeDetailPOJO {

    @SerializedName("vegetarian")
    private boolean vegetarian;
    @SerializedName("vegan")
    private boolean vegan;
    @SerializedName("glutenFree")
    private boolean glutenFree;
    @SerializedName("dairyFree")
    private boolean dairyFree;

    @SerializedName("preparationMinutes")
    private int preparationMinutes;
    @SerializedName("cookingMinutes")
    private int cookingMinutes;

    @SerializedName("extendedIngredients")
    private List<ExtendedIngredientPOJO> extendedIngredients;

    @SerializedName("title")
    private String title;
    @SerializedName("readyInMinutes")
    private int readyInMinutes;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String image;

    @SerializedName("analyzedInstructions")
    private List<AnalyzedInstructionPOJO> analyzedInstructions;

    public String getTitle() {
        return title;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }
}
