package com.example.easychef.models;

import com.parse.ParseClassName;

import org.json.JSONException;
import org.json.JSONObject;

@ParseClassName("Ingredient")
public class Ingredient extends EasyChefParseObjectAbstract {
    public static final String KEY_NAME = "name";
    private static final String IMAGE_URL_ROOT = "https://spoonacular.com/cdn/ingredients_100x100/%s";
    public static final String KEY_INGREDIENT_ID = "ingredientId";

    public Ingredient() {

    }

    public Ingredient(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has(KEY_ID)) {
            setId(jsonObject.getInt(KEY_ID));
        }
        setName(jsonObject.getString(KEY_NAME));
        setImageUrl(jsonObject.getString(KEY_IMAGE_URL));
    }

    @Override
    public String getName() {
        return getString(KEY_NAME);
    }

    @Override
    public void setName(String name) {
        put(KEY_NAME, name);
    }

    @Override
    public int getId() {
        return getInt(KEY_INGREDIENT_ID);
    }

    @Override
    public void setId(int id) {
        put(KEY_INGREDIENT_ID, id);
    }

    @Override
    public String getImageUrl() {
        return String.format(IMAGE_URL_ROOT,
                getString(KEY_IMAGE_URL));
    }

    @Override
    public void setImageUrl(String imageUrl) {
        put(KEY_IMAGE_URL, imageUrl);
    }

}
