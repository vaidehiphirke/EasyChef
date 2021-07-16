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

import com.example.easychef.adapters.SavedRecipeAdapter;
import com.example.easychef.databinding.FragmentShowRecipeListBinding;
import com.example.easychef.models.SavedIngredient;
import com.example.easychef.models.SavedRecipe;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private static final String TAG = "FavoritesFragment";
    private FragmentShowRecipeListBinding showRecipeListBinding;
    private List<SavedRecipe> userFavoriteRecipes;
    private SavedRecipeAdapter savedRecipeAdapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showRecipeListBinding = FragmentShowRecipeListBinding.inflate(inflater, container, false);
        userFavoriteRecipes = new ArrayList<>();
        return showRecipeListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedRecipeAdapter = new SavedRecipeAdapter(getContext(), userFavoriteRecipes, new RecipeUnsaveOnClickListener());
        showRecipeListBinding.rvRecipes.setAdapter(savedRecipeAdapter);
        showRecipeListBinding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        querySavedRecipes();
    }

    private void querySavedRecipes() {
        final ParseQuery<SavedRecipe> query = ParseQuery.getQuery(SavedRecipe.class);
        query.include(SavedIngredient.KEY_NAME);
        query.addDescendingOrder(SavedIngredient.KEY_CREATED_AT);
        query.findInBackground(new RetrieveSavedRecipesFindCallback());
    }

    private void deleteSavedRecipeFromParse(String objectIdToDelete) {
        final ParseQuery<SavedRecipe> query = ParseQuery.getQuery(SavedRecipe.class);
        query.getInBackground(objectIdToDelete, new DeleteSavedRecipeGetCallback());
    }

    private class RetrieveSavedRecipesFindCallback implements com.parse.FindCallback<SavedRecipe> {
        @Override
        public void done(List<SavedRecipe> recipes, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with getting ingredients", e);
                return;
            }
            savedRecipeAdapter.clear();
            userFavoriteRecipes.addAll(recipes);
            savedRecipeAdapter.notifyDataSetChanged();
        }
    }

    private class RecipeUnsaveOnClickListener implements SavedRecipeAdapter.OnClickListener {
        @Override
        public void onItemClicked(int position) {
            final String objectIdToDelete = userFavoriteRecipes.remove(position).getObjectId();
            deleteSavedRecipeFromParse(objectIdToDelete);
            savedRecipeAdapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Item was removed!", Toast.LENGTH_SHORT).show();
        }
    }

    private class DeleteSavedRecipeGetCallback implements GetCallback<SavedRecipe> {
        @Override
        public void done(SavedRecipe recipe, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with removing recipe from favorites", e);
                return;
            }
            recipe.deleteInBackground();
            Toast.makeText(getContext(), "Recipe removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }
}
