package com.example.easychef.fragments;

import android.util.Log;

import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.models.Ingredient;
import com.example.easychef.models.RecipePOJO;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_CREATED_AT;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_USER;
import static com.example.easychef.models.Ingredient.KEY_NAME_INGREDIENT;
import static com.example.easychef.utils.ParsePOJOUtils.getRecipesFromRecipePOJOS;

public class SuggestedRecipesFromPantryFragment extends RecipeListFragmentAbstract {

    private static final String TAG = "SuggestedRecipesFromPantryFragment";

    @Override
    protected void getRecipesToShowInList() {
        getFoodAPI().getPantryRecipes(getAPICall())
                .enqueue(new SuggestedRecipesCallback());
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

        final StringBuilder response = new StringBuilder(String.format("&ingredients=%s", userIngredientsFromParse.get(0).getName()));
        for (int i = 1; i < userIngredientsFromParse.size(); i++) {
            response.append(String.format(",+%s", userIngredientsFromParse.get(i).getName()));
        }
        Log.i(TAG, "API Call: " + response.toString());
        return response.toString();
    }

    private class SuggestedRecipesCallback implements Callback<List<RecipePOJO>> {
        @Override
        public void onResponse(@NotNull Call<List<RecipePOJO>> call, Response<List<RecipePOJO>> response) {
            recipes.addAll(getRecipesFromRecipePOJOS(response.body()));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(@NotNull Call<List<RecipePOJO>> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }
}