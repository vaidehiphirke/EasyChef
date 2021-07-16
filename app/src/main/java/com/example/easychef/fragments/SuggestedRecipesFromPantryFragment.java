package com.example.easychef.fragments;

import android.util.Log;

import com.example.easychef.BuildConfig;
import com.example.easychef.models.SavedIngredient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesFromPantryFragment extends ShowRecipeListFragmentAbstract {

    private static final String TAG = "SuggestedRecipesFromPantryFragment";
    private static final String FIND_RECIPE_API_CALL =
            String.format(
                    "/findByIngredients?apiKey=%s",
                    BuildConfig.SPOONACULAR_KEY);
    private final List<SavedIngredient> userIngredientsFromParse = new ArrayList<>();

    @Override
    protected String getAPICall() {
        final ParseQuery<SavedIngredient> query = ParseQuery.getQuery(SavedIngredient.class);
        query.include(SavedIngredient.KEY_NAME);
        query.addDescendingOrder(SavedIngredient.KEY_CREATED_AT);
        query.whereEqualTo(SavedIngredient.KEY_USER, ParseUser.getCurrentUser());
        try {
            userIngredientsFromParse.addAll(query.find());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final StringBuilder response = new StringBuilder(FIND_RECIPE_API_CALL);
        response.append(String.format("&ingredients=%s", userIngredientsFromParse.get(0).getName()));
        for (int i = 1; i < userIngredientsFromParse.size(); i++) {
            response.append(String.format(",+%s", userIngredientsFromParse.get(i).getName()));
        }
        Log.i(TAG, response.toString());
        return response.toString();
    }
}
