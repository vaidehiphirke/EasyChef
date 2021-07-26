package com.example.easychef.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StepPOJO {

    @SerializedName("number")
    private int number;
    @SerializedName("step")
    private String step;
    @SerializedName("ingredients")
    private List<IngredientPOJO> ingredients = null;
    @SerializedName("equipment")
    private List<EquipmentPOJO> equipment = null;
    @SerializedName("length")
    private LengthPOJO length;
}
