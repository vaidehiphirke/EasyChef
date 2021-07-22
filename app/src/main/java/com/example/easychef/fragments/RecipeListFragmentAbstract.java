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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_ID;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_IMAGE_URL;
import static com.example.easychef.models.Recipe.KEY_NAME_RECIPE;

public abstract class RecipeListFragmentAbstract extends Fragment {

    private static final String TAG = "RecipeListFragmentAbstract";
    protected static final String API_URL_ROOT = "https://api.spoonacular.com/recipes";
    protected List<Recipe> recipes;
    protected RecipeAdapter adapter;
    private FragmentRecipeListBinding binding;

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
        binding = FragmentRecipeListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recipes = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), recipes, getOnUnsavedListener());
        binding.rvRecipes.setAdapter(adapter);
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

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
                final Recipe.Builder builder = new Recipe.Builder().user(ParseUser.getCurrentUser());
                for (int j = 0; j < jsonArray.length(); j++) {
                    final JSONObject jsonObject = jsonArray.getJSONObject(j);
                    final Recipe recipe = builder.name(jsonObject.getString(KEY_NAME_RECIPE))
                            .id(jsonObject.getInt(KEY_ID))
                            .imageUrl(jsonObject.getString(KEY_IMAGE_URL))
                            .build();

                    recipes.add(recipe);
                }
                adapter.notifyDataSetChanged();
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