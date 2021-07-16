package com.example.easychef.fragments;

import com.example.easychef.BuildConfig;

public class SearchFragment extends ShowRecipeListFragmentAbstract {

    private static final String TEMPORARY_HARDCODED_API_RECIPE_CALL =
            String.format(
                    "/findByIngredients?apiKey=%s&ingredients=oranges,+flour,+sugar",
                    BuildConfig.SPOONACULAR_KEY);

    @Override
    protected String getAPICall() {
        return TEMPORARY_HARDCODED_API_RECIPE_CALL;
    }
}