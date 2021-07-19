package com.example.easychef.fragments;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.example.easychef.BuildConfig;
import com.example.easychef.adapters.RecipeAdapter;

public class SearchFragment extends RecipeListFragmentAbstract {

    private static final String TEMPORARY_HARDCODED_API_RECIPE_CALL =
            String.format(
                    "/findByIngredients?apiKey=%s&ingredients=oranges,+flour,+sugar",
                    BuildConfig.SPOONACULAR_KEY);

    @Override
    protected void getRecipesToShowInList() {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(apiCall.append(TEMPORARY_HARDCODED_API_RECIPE_CALL).toString(), new RecipeJsonHttpResponseHandler());
    }

    @Override
    protected RecipeAdapter.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveButPersistOnClickListener();
    }
}