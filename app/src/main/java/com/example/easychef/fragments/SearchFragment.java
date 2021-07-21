package com.example.easychef.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easychef.BuildConfig;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.databinding.FragmentSearchBinding;
import com.example.easychef.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Headers;

import static com.example.easychef.AsyncClient.CLIENT;

public class SearchFragment extends RecipeListFragmentAbstract {

    private static final String RECIPE_SEARCH_API_CALL =
            String.format(
                    "%s/complexSearch?apiKey=%s&query=",
                    API_URL_ROOT, BuildConfig.SPOONACULAR_KEY);
    private static final String RECIPE_EXPLORE_API_CALL =
            String.format(
                    "%s/random?apiKey=%s&number=10",
                    API_URL_ROOT, BuildConfig.SPOONACULAR_KEY);
    private static final String TAG = "SearchFragment";

    private FragmentSearchBinding binding;
    private String searchQuery;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recipes = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), recipes, getOnUnsavedListener());
        binding.rvRecipes.setAdapter(adapter);
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnSearch.setOnClickListener(new GetRecipesOnClickListener());

        getExploreRecipes();
    }

    @Override
    protected void getRecipesToShowInList() {
        CLIENT.get(RECIPE_SEARCH_API_CALL.concat(searchQuery), new SearchRecipeJsonHttpResponseHandler());
    }

    @Override
    protected RecipeAdapter.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveButPersistOnClickListener();
    }

    private void getExploreRecipes() {
        CLIENT.get(RECIPE_EXPLORE_API_CALL, new ExploreRecipeJsonHttpResponseHandler());

    }

    private class GetRecipesOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            searchQuery = binding.etAddIngredient.getText().toString();
            getRecipesToShowInList();
        }
    }

    private class SearchRecipeJsonHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int i, Headers headers, JSON json) {
            try {
                adapter.clear();
                recipes.clear();
                final JSONArray results = json.jsonObject.getJSONArray("results");
                for (int j = 0; j < results.length(); j++) {
                    recipes.add(new Recipe(results.getJSONObject(j)));
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

    private class ExploreRecipeJsonHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int i, Headers headers, JSON json) {
            try {
                adapter.clear();
                recipes.clear();
                final JSONArray results = json.jsonObject.getJSONArray("recipes");
                for (int j = 0; j < results.length(); j++) {
                    recipes.add(new Recipe(results.getJSONObject(j)));
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
}