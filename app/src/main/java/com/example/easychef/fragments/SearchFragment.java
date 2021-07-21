package com.example.easychef.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easychef.BuildConfig;
import com.example.easychef.adapters.AutoCompleteAdapter;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.databinding.FragmentSearchBinding;
import com.example.easychef.models.EasyChefParseObjectAbstract;
import com.example.easychef.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

import static com.example.easychef.AsyncClient.CLIENT;
import static com.example.easychef.adapters.AutoCompleteAdapter.AUTO_COMPLETE_DELAY_CODE;
import static com.example.easychef.adapters.AutoCompleteAdapter.THRESHOLD;
import static com.example.easychef.adapters.AutoCompleteAdapter.TRIGGER_AUTO_COMPLETE_CODE;

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

    private static final String RECIPE_AUTOCOMPLETE_API_BASE_CALL = "https://api.spoonacular.com/recipes/autocomplete?apiKey=%s&query=";
    private static final String RECIPE_AUTOCOMPLETE_API_CALL_WITH_KEY =
            String.format(
                    RECIPE_AUTOCOMPLETE_API_BASE_CALL, BuildConfig.SPOONACULAR_KEY);
    private AutoCompleteAdapter autoCompleteAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        autoCompleteAdapter = new AutoCompleteAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        binding.etSearchForRecipe.setThreshold(THRESHOLD);
        binding.etSearchForRecipe.setAdapter(autoCompleteAdapter);
        binding.etSearchForRecipe.addTextChangedListener(new RecipeAutocompleteTextWatcher());

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

    private void makeRecipeAPICall(String query) {
        CLIENT.get(RECIPE_AUTOCOMPLETE_API_CALL_WITH_KEY + query, new AutocompleteJsonHttpResponseHandler());
    }

    private void getExploreRecipes() {
        CLIENT.get(RECIPE_EXPLORE_API_CALL, new ExploreRecipeJsonHttpResponseHandler());

    }

    private class GetRecipesOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            searchQuery = binding.etSearchForRecipe.getText().toString();
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

    private class AutocompleteJsonHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int i, Headers headers, JSON json) {
            final List<EasyChefParseObjectAbstract> autocompleteRecipes = new ArrayList<>();
            try {
                for (int j = 0; j < json.jsonArray.length(); j++) {
                    final EasyChefParseObjectAbstract recipe = new Recipe(json.jsonArray.getJSONObject(j));
                    autocompleteRecipes.add(recipe);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Hit json exception", e);
            }
            autoCompleteAdapter.setData(autocompleteRecipes);
            autoCompleteAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int i, Headers headers, String s, Throwable throwable) {
            Log.d(TAG, "onFailure ingredient autocomplete" + throwable.getMessage());
        }
    }

    private class RecipeAutocompleteTextWatcher implements TextWatcher {
        private Handler handler;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int
                count, int after) {
            handler = new Handler(new RecipeHandlerCallback());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            handler.removeMessages(TRIGGER_AUTO_COMPLETE_CODE);
            handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE_CODE,
                    AUTO_COMPLETE_DELAY_CODE);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private class RecipeHandlerCallback implements Handler.Callback {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if (message.what != TRIGGER_AUTO_COMPLETE_CODE) {
                    return false;
                }
                if (!TextUtils.isEmpty(binding.etSearchForRecipe.getText())) {
                    makeRecipeAPICall(binding.etSearchForRecipe.getText().toString());
                }
                return true;
            }
        }
    }
}