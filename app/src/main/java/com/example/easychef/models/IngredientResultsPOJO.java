package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResultsPOJO {

    @SerializedName("results")
    private List<IngredientPOJO> results;

    public IngredientPOJO getSingleResult() {
        return results.get(0);
    }
}
