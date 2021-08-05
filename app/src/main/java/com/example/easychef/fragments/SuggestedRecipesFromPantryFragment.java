package com.example.easychef.fragments;

import android.util.Log;
import android.widget.Toast;

import com.example.easychef.R;
import com.example.easychef.models.Ingredient;
import com.example.easychef.models.RecipePOJO;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;
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
        getFoodAPI()
                .getPantryRecipes(getAPICall())
                .enqueue(new SuggestedRecipesCallback());
    }

    @Override
    protected SaveRecipeToFavoritesUtils.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveButPersistOnClickListener();
    }

    private String getAPICall() {
        final List<Ingredient> userIngredientsFromParse = new ArrayList<>();
        final ParseQuery<Ingredient> query = ParseQuery.getQuery(Ingredient.class);
        query.include(KEY_NAME_INGREDIENT);
        query.addDescendingOrder(KEY_CREATED_AT);
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
        try {
            userIngredientsFromParse.addAll(query.find());
        } catch (ParseException e) {
            Log.e(TAG, "Error with finding Parse ingredients", e);
        }

        if (userIngredientsFromParse.isEmpty()) {
            Toast.makeText(getContext(), "Add some ingredients to your list first in order to get recipe suggestions!", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new IngredientFragment()).commit();
            return "";
        }

        final StringBuilder response = new StringBuilder(userIngredientsFromParse.get(0).getName());
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