package com.example.easychef.models;

import com.parse.ParseClassName;

@ParseClassName("SavedIngredient")
public class Ingredient extends EasyChefParseObjectAbstract {
    public static final String KEY_NAME = "name";
    private static final String IMAGE_URL_ROOT = "spoonacular.com/cdn/ingredients_100x100/%s";

    @Override
    public String getName() {
        return getString(KEY_NAME);
    }

    @Override
    public void setName(String name) {
        put(KEY_NAME, name);
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
