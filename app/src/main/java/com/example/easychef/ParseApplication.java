package com.example.easychef;

import android.app.Application;

import com.example.easychef.models.SavedRecipe;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(SavedRecipe.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APP_ID)
                .clientKey(BuildConfig.PARSE_CONSUMER_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
