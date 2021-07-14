package com.example.easychef.models;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeFromAPI {

    private static final String JSON_KEY_TITLE = "title";
    private final String name;

    public RecipeFromAPI(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString(JSON_KEY_TITLE);
    }

    public String getName() {
        return name;
    }
}