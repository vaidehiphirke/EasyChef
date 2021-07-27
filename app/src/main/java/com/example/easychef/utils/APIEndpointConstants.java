package com.example.easychef.utils;

import com.example.easychef.BuildConfig;

public class APIEndpointConstants {

    public static final String RECIPE_BASE = "recipes/";
    public static final String INGREDIENT_BASE = "food/ingredients/";

    public static final String COMPLEX_SEARCH_AND_KEY = "complexSearch" + BuildConfig.SPOONACULAR_KEY;
    public static final String AUTOCOMPLETE_AND_KEY = "autocomplete" + BuildConfig.SPOONACULAR_KEY;

    public static final String FIND_BY_INGREDIENTS_AND_KEY = "findByIngredients" + BuildConfig.SPOONACULAR_KEY;
    public static final String DETAILS_AND_KEY = "{id}/information" + BuildConfig.SPOONACULAR_KEY;
}

