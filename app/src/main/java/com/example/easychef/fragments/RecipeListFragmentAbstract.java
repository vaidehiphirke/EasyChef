package com.example.easychef.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.databinding.FragmentRecipeListBinding;
import com.example.easychef.models.Recipe;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public abstract class RecipeListFragmentAbstract extends Fragment {

    private static final String TAG = "RecipeListFragmentAbstract";
    private static final String API_URL_ROOT = "https://api.spoonacular.com/recipes";
    protected final StringBuilder apiCall = new StringBuilder(API_URL_ROOT);

    protected List<Recipe> recipes;
    protected RecipeAdapter recipeAdapter;
    private FragmentRecipeListBinding showRecipeListBinding;

    public RecipeListFragmentAbstract() {
        // Required empty public constructor
    }

    protected abstract void getRecipesToShowInList();

    protected abstract RecipeAdapter.OnUnsavedListener getOnUnsavedListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showRecipeListBinding = FragmentRecipeListBinding.inflate(inflater, container, false);
        return showRecipeListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipes = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getContext(), recipes, getOnUnsavedListener());
        showRecipeListBinding.rvRecipes.setAdapter(recipeAdapter);
        showRecipeListBinding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        getRecipesToShowInList();
    }

    protected void deleteSavedRecipeFromParse(String objectIdToDelete) {
        final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.getInBackground(objectIdToDelete, new DeleteSavedRecipeGetCallback());
    }

    protected static class DeleteSavedRecipeGetCallback implements GetCallback<Recipe> {
        @Override
        public void done(Recipe recipe, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with removing recipe from favorites", e);
                return;
            }
            recipe.deleteInBackground();
        }
    }

    protected class RecipeJsonHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int i, Headers headers, JSON json) {
            Log.d(TAG, "onSuccess");
            final JSONArray jsonArray = json.jsonArray;
            try {
                for (int j = 0; j < jsonArray.length(); j++) {
                    recipes.add(new Recipe(jsonArray.getJSONObject(j)));
                }
                recipeAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e(TAG, "Hit json exception", e);
            }
        }

        @Override
        public void onFailure(int i, Headers headers, String s, Throwable throwable) {
            Log.d(TAG, "onFailure" + throwable.getMessage());
        }
    }

    protected class UnsaveButPersistOnClickListener implements RecipeAdapter.OnUnsavedListener {
        @Override
        public void onUnsavedChecked(int position) {
            final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
            query.whereEqualTo(Recipe.KEY_RECIPE_ID, recipes.get(position).getId());
            query.whereEqualTo(Recipe.KEY_USER, ParseUser.getCurrentUser());
            try {
                final String objectIdToDelete = query.getFirst().getObjectId();
                deleteSavedRecipeFromParse(objectIdToDelete);
            } catch (ParseException e) {
                Log.e(TAG, "Recipe id not found", e);
            }
            Toast.makeText(getContext(), "Recipe removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }
}