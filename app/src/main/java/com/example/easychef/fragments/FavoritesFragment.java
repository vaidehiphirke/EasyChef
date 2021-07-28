package com.example.easychef.fragments;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.easychef.models.Recipe;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_CREATED_AT;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_USER;
import static com.example.easychef.models.Ingredient.KEY_NAME_INGREDIENT;
import static com.example.easychef.models.Recipe.KEY_RECIPE_ID;
import static com.example.easychef.utils.ParseCacheUtils.setQueryCacheControl;

public class FavoritesFragment extends RecipeListFragmentAbstract {

    private static final String TAG = "FavoritesFragment";

    @Override
    protected void getRecipesToShowInList() {
        final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.include(KEY_NAME_INGREDIENT);
        query.addDescendingOrder(KEY_CREATED_AT);
        setQueryCacheControl(query);
        query.findInBackground(new RetrieveSavedRecipesFindCallback());
    }

    @Override
    protected SaveRecipeToFavoritesUtils.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveOnClickListener();
    }

    private class UnsaveOnClickListener implements SaveRecipeToFavoritesUtils.OnUnsavedListener {
        private List<Recipe> recipesToLookIn = recipes;
        private Context context = getContext();

        @Override
        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        public void setRecipes(List<Recipe> recipesToLookIn) {
            this.recipesToLookIn = recipesToLookIn;
        }

        @Override
        public void onUnsavedChecked(int position) {
            final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
            query.whereEqualTo(KEY_RECIPE_ID, recipesToLookIn.get(position).getId());
            query.whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
            try {
                final String objectIdToDelete = query.getFirst().getObjectId();
                deleteSavedRecipeFromParse(objectIdToDelete);
                recipesToLookIn.remove(position);
                adapter.notifyItemRemoved(position);
            } catch (ParseException e) {
                Log.e(TAG, "Recipe id not found", e);
            }
            Toast.makeText(context, "Recipe removed from favorites!", Toast.LENGTH_SHORT).show();
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