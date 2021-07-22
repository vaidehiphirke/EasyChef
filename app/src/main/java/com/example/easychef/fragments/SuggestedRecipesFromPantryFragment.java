package com.example.easychef.fragments;

import android.util.Log;

import com.example.easychef.BuildConfig;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.models.Ingredient;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.easychef.AsyncClient.CLIENT;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_CREATED_AT;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_USER;
import static com.example.easychef.models.Ingredient.KEY_NAME_INGREDIENT;

public class SuggestedRecipesFromPantryFragment extends RecipeListFragmentAbstract {

    private static final String TAG = "SuggestedRecipesFromPantryFragment";
    private static final String FIND_RECIPE_API_CALL =
            String.format(
                    "/findByIngredients?apiKey=%s",
                    BuildConfig.SPOONACULAR_KEY);

    @Override
    protected void getRecipesToShowInList() {
        CLIENT.get(API_URL_ROOT.concat(getAPICall()), new RecipeJsonHttpResponseHandler());
    }

    @Override
    protected RecipeAdapter.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveButPersistOnClickListener();
    }

    private String getAPICall() {
        final List<Ingredient> userIngredientsFromParse = new ArrayList<>();
        final ParseQuery<Ingredient> query = ParseQuery.getQuery(Ingredient.class);
        query.include(KEY_NAME_INGREDIENT);
        query.addDescendingOrder(KEY_CREATED_AT);
        query.whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
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
