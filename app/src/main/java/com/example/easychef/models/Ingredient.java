package com.example.easychef.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Ingredient")
public class Ingredient extends EasyChefParseObjectAbstract {

    public static final String KEY_NAME_INGREDIENT = "name";
    private static final String IMAGE_URL_ROOT = "https://spoonacular.com/cdn/ingredients_100x100/%s";
    public static final String KEY_INGREDIENT_ID = "ingredientId";

    public Ingredient() {
        // required empty constructor for Parse
    }

    private Ingredient(Builder builder) {
        put(KEY_NAME_INGREDIENT, builder.name);
        put(KEY_USER, builder.user);
        put(KEY_INGREDIENT_ID, builder.id);
        put(KEY_IMAGE_URL, builder.imageUrl);
    }

    @Override
    public String getName() {
        return getString(KEY_NAME_INGREDIENT);
    }

    @Override
    public int getId() {
        return getInt(KEY_INGREDIENT_ID);
    }

    public static class Builder {
        private String name;
        private ParseUser user;
        private int id;
        private String imageUrl;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder user(ParseUser user) {
            this.user = user;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = String.format(IMAGE_URL_ROOT, imageUrl);
            return this;
        }

        public Ingredient build() {
            return new Ingredient(this);
        }
    }

}
