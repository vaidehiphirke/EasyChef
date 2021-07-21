package com.example.easychef.models;

import androidx.annotation.NonNull;

import com.parse.ParseObject;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

public abstract class EasyChefParseObjectAbstract extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_IMAGE_URL = "imageURL";
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_OBJECT_ID = "objectId";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public abstract String getName();

    public abstract void setName(String name);

    public abstract String getImageUrl();

    public abstract void setImageUrl(String imageUrl);

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return getName();
    }
}