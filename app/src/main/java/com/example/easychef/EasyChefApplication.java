package com.example.easychef;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.example.easychef.models.Ingredient;
import com.example.easychef.models.Recipe;
import com.parse.Parse;
import com.parse.ParseObject;

public class EasyChefApplication extends Application {

    private static EasyChefApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (instance == null) {
            instance = this;
        }

        ParseObject.registerSubclass(Recipe.class);
        ParseObject.registerSubclass(Ingredient.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APP_ID)
                .clientKey(BuildConfig.PARSE_CONSUMER_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

    public static EasyChefApplication getInstance() {
        return instance;
    }

    public static boolean hasNetwork() {
        return instance.isNetworkConnected();
    }

    private boolean isNetworkConnected() {
        final ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}