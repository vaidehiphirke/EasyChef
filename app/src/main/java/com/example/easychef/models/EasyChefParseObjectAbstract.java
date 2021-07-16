package com.example.easychef.models;

import com.parse.ParseObject;
import com.parse.ParseUser;

public abstract class EasyChefParseObjectAbstract extends ParseObject {

    public static final String KEY_NAME = "name";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_OBJECT_ID = "objectId";

    public String getName() {
        return getString(KEY_NAME);
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
