package com.example.easychef.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Recipe")
public class Recipe extends EasyChefParseObjectAbstract {

    public static final String KEY_NAME_RECIPE = "title";
    public static final String KEY_RECIPE_ID = "recipeId";

    public Recipe() {
        // required empty constructor for Parse
    }

    private Recipe(Builder builder) {
        if (builder.title == null) {
            throw new IllegalStateException("Recipe title cannot be null");
        }
        put(KEY_NAME_RECIPE, builder.title);
        if (builder.user == null) {
            throw new IllegalStateException("User cannot be null");
        }
        put(KEY_USER, builder.user);
        put(KEY_RECIPE_ID, builder.id);
        if (builder.imageUrl != null) {
            put(KEY_IMAGE_URL, builder.imageUrl);
        }
    }

    public void setName(String title) {
        put(KEY_NAME_RECIPE, title);

    }

    @Override
    public int getId() {
        return getInt(KEY_RECIPE_ID);
    }

    @Override
    public String getName() {
        return getString(KEY_NAME_RECIPE);
    }

    public static class Builder {
        private String title;
        private ParseUser user;
        private int id;
        private String imageUrl;

        public Builder name(String title) {
            this.title = title;
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
            this.imageUrl = imageUrl;
            return this;
        }

        public Recipe build() {
            return new Recipe(this);
        }
    }

}