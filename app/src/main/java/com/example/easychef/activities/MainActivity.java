package com.example.easychef.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easychef.BuildConfig;
import com.example.easychef.adapters.SuggestedRecipeAdapter;
import com.example.easychef.databinding.ActivityMainBinding;
import com.example.easychef.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    private static final String TEMPORARY_HARDCODED_API_RECIPE_CALL = String.format("https://api.spoonacular.com/recipes/findByIngredients?apiKey=%s&ingredients=apples,+flour,+sugar", BuildConfig.SPOONACULAR_KEY);
    private static final String TAG = "MainActivity";
    private List<Recipe> recipeList;
    private SuggestedRecipeAdapter suggestedRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recipeList = new ArrayList<>();

        suggestedRecipeAdapter = new SuggestedRecipeAdapter(this, recipeList);
        binding.rvSuggestedRecipes.setAdapter(suggestedRecipeAdapter);
        binding.rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(this));

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
                    recipeList.add(new Recipe(jsonObject.getString("title")));
                }
                suggestedRecipeAdapter.notifyDataSetChanged();
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