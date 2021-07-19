package com.example.easychef.models;

import com.parse.ParseClassName;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("Recipe")
public class Recipe extends EasyChefParseObjectAbstract {

    public static final String KEY_NAME_RECIPE = "title";
    public static final String KEY_RECIPE_ID = "recipeId";
    public static final String KEY_MISSED_INGREDIENT_COUNT = "missedIngredientCount";

    public Recipe() {

    }

    public Recipe(JSONObject jsonObject) throws JSONException {
        setId(jsonObject.getInt(KEY_ID));
        setName(jsonObject.getString(KEY_NAME_RECIPE));
    }

    public int getId() {
        return getInt(KEY_RECIPE_ID);
    }

    public void setId(int id) {
        put(KEY_RECIPE_ID, id);
    }

    @Override
    public String getName() {
        return getString(KEY_NAME_RECIPE);
    }

    @Override
    public void setName(String name) {
        put(KEY_NAME_RECIPE, name);
    }

    @Override
    public String getImageUrl() {
        return getString(KEY_IMAGE_URL);
    }

    @Override
    public void setImageUrl(String imageUrl) {
        put(KEY_IMAGE_URL, imageUrl);
    }

    public String getMissedIngredientCount() {
        return getString(KEY_MISSED_INGREDIENT_COUNT);
    }

    public void setMissedIngredientCount(int count) {
        put(KEY_MISSED_INGREDIENT_COUNT, count);
    }

}