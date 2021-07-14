package com.example.easychef.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easychef.BuildConfig;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.databinding.FragmentSearchBinding;
import com.example.easychef.models.RecipeFromAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class SearchFragment extends Fragment {

    private static final String API_URL_ROOT = "https://api.spoonacular.com/recipes";
    private static final String TEMPORARY_HARDCODED_API_RECIPE_CALL =
            String.format(
                    "%s/findByIngredients?apiKey=%s&ingredients=apples,+flour,+sugar",
                    API_URL_ROOT,
                    BuildConfig.SPOONACULAR_KEY);
    private static final String TAG = "SearchFragment";
    private List<RecipeFromAPI> suggestedRecipeFromAPIList;
    private RecipeAdapter recipeAdapter;
    private FragmentSearchBinding searchBinding;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        searchBinding = FragmentSearchBinding.inflate(inflater, container, false);
        return searchBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        suggestedRecipeFromAPIList = new ArrayList<>();

        recipeAdapter = new RecipeAdapter(getContext(), suggestedRecipeFromAPIList);
        searchBinding.rvSuggestedRecipes.setAdapter(recipeAdapter);
        searchBinding.rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(TEMPORARY_HARDCODED_API_RECIPE_CALL, new RecipeJsonHttpResponseHandler());

    }

    private class RecipeJsonHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int i, Headers headers, JSON json) {
            Log.d(TAG, "onSuccess");
            final JSONArray jsonArray = json.jsonArray;
            try {
                for (int j = 0; j < jsonArray.length(); j++) {
                    final JSONObject jsonObject = jsonArray.getJSONObject(j);
                    suggestedRecipeFromAPIList.add(new RecipeFromAPI(jsonObject));
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
}