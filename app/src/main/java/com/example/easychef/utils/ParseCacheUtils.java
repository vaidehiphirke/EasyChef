package com.example.easychef.utils;

import com.example.easychef.EasyChefApplication;
import com.parse.ParseQuery;

public class ParseCacheUtils {

    public static void setQueryCacheControl(ParseQuery query) {
        if (!EasyChefApplication.hasNetwork()) {
            query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        } else {
            query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
    }
}
