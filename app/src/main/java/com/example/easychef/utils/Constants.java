package com.example.easychef.utils;

import com.example.easychef.BuildConfig;

public class Constants {

    public static final String RECIPE_SEARCH_ENDPOINT = "recipes/complexSearch" + BuildConfig.SPOONACULAR_KEY + "&sort=popularity";
    public static final String SUGGESTED_RECIPES_BY_INGREDIENTS_ENDPOINT = "recipes/findByIngredients" + BuildConfig.SPOONACULAR_KEY;
    public static final String AUTOCOMPLETE_RECIPES_ENDPOINT = "recipes/autocomplete" + BuildConfig.SPOONACULAR_KEY;
    public static final String EXPLORE_RECIPES_ENDPOINT = "recipes/complexSearch" + BuildConfig.SPOONACULAR_KEY + "&sort=random&popularity";
    public static final String RECIPE_DETAILS_ENDPOINT = "recipes/{id}/card" + BuildConfig.SPOONACULAR_KEY;
    public static final String AUTOCOMPLETE_INGREDIENTS_ENDPOINT = "food/ingredients/autocomplete" + BuildConfig.SPOONACULAR_KEY + "&metaInformation=true";
}
