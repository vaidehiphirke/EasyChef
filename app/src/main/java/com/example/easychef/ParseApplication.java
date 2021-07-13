package com.example.easychef;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.PARSE_APP_ID)
                .clientKey(BuildConfig.PARSE_CONSUMER_KEY)
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
