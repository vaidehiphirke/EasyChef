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

    public int getNumber() {
        return number;
    }

    public String getStep() {
        return step;
    }

    public List<IngredientPOJO> getIngredients() {
        return ingredients;
    }

    public List<EquipmentPOJO> getEquipment() {
        return equipment;
    }

    public LengthPOJO getLength() {
        return length;
    }
}
