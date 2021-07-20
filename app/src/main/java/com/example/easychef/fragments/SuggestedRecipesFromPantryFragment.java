package com.example.easychef.fragments;

import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.example.easychef.BuildConfig;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.models.Ingredient;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesFromPantryFragment extends RecipeListFragmentAbstract {

    private static final String TAG = "SuggestedRecipesFromPantryFragment";
    private static final String FIND_RECIPE_API_CALL =
            String.format(
                    "/findByIngredients?apiKey=%s",
                    BuildConfig.SPOONACULAR_KEY);
    private final List<Ingredient> userIngredientsFromParse = new ArrayList<>();

    @Override
    protected void getRecipesToShowInList() {
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(apiCall.append(getAPICall()).toString(), new RecipeJsonHttpResponseHandler());
    }

    @Override
    protected RecipeAdapter.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveButPersistOnClickListener();
    }

    private String getAPICall() {
        final ParseQuery<Ingredient> query = ParseQuery.getQuery(Ingredient.class);
        query.include(Ingredient.KEY_NAME);
        query.addDescendingOrder(Ingredient.KEY_CREATED_AT);
        query.whereEqualTo(Ingredient.KEY_USER, ParseUser.getCurrentUser());
        try {
            userIngredientsFromParse.addAll(query.find());
        } catch (ParseException e) {
            Log.e(TAG, "Error with finding Parse ingredients", e);
        }

        final StringBuilder response = new StringBuilder(FIND_RECIPE_API_CALL);
        response.append(String.format("&ingredients=%s", userIngredientsFromParse.get(0).getName()));
        for (int i = 1; i < userIngredientsFromParse.size(); i++) {
            response.append(String.format(",+%s", userIngredientsFromParse.get(i).getName()));
        }
        Log.i(TAG, "API Call: " + response.toString());
        return response.toString();
    }
}
