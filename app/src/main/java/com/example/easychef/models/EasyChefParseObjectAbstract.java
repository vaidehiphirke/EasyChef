package com.example.easychef.models;

import androidx.annotation.NonNull;

import com.parse.ParseObject;

import org.jetbrains.annotations.NotNull;

public abstract class EasyChefParseObjectAbstract extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_IMAGE_URL = "image";
    public static final String KEY_CREATED_AT = "createdAt";

    public abstract String getName();

    public abstract int getId();

    public String getImageUrl() {
        return getString(KEY_IMAGE_URL);
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return getName();
    }
}