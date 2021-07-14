package com.example.easychef.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Recipe {

    private static final String JSON_KEY_TITLE = "title";
    private final String recipeName;

    public Recipe(JSONObject jsonObject) throws JSONException {
        this.recipeName = jsonObject.getString(JSON_KEY_TITLE);
    }

    public String getName() {
        return recipeName;
    }
}