package com.example.easychef.fragments;

import android.util.Log;
import android.widget.Toast;

import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.models.Ingredient;
import com.example.easychef.models.Recipe;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class FavoritesFragment extends RecipeListFragmentAbstract {

    private static final String TAG = "FavoritesFragment";

    @Override
    protected void getRecipesToShowInList() {
        final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.include(Ingredient.KEY_NAME);
        query.addDescendingOrder(Ingredient.KEY_CREATED_AT);
        query.findInBackground(new RetrieveSavedRecipesFindCallback());
    }

    @Override
    protected RecipeAdapter.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveOnClickListener();
    }

    private class UnsaveOnClickListener implements RecipeAdapter.OnUnsavedListener {
        @Override
        public void onUnsavedChecked(int position) {
            final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
            query.whereEqualTo(Recipe.KEY_RECIPE_ID, recipes.get(position).getId());
            query.whereEqualTo(Recipe.KEY_USER, ParseUser.getCurrentUser());
            try {
                final String objectIdToDelete = query.getFirst().getObjectId();
                deleteSavedRecipeFromParse(objectIdToDelete);
                recipes.remove(position);
                adapter.notifyItemRemoved(position);
            } catch (ParseException e) {
                Log.e(TAG, "Recipe id not found", e);
            }
            Toast.makeText(getContext(), "Recipe removed from favorites!", Toast.LENGTH_SHORT).show();
        }
    }

    private class RetrieveSavedRecipesFindCallback implements FindCallback<Recipe> {
        @Override
        public void done(List<Recipe> savedRecipes, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with getting recipes from pantry ingredients", e);
                return;
            }
            adapter.clear();
            recipes.addAll(savedRecipes);
            adapter.notifyDataSetChanged();
        }
    }
}