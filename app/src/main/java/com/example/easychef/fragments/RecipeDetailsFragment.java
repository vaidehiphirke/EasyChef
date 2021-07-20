package com.example.easychef.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AbsCallback;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easychef.BuildConfig;
import com.example.easychef.R;
import com.example.easychef.databinding.FragmentProfileBinding;
import com.example.easychef.databinding.FragmentRecipeDetailsBinding;
import com.example.easychef.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class RecipeDetailsFragment extends Fragment {

    private static final String TAG = "RecipeDetailsFragment";
    private FragmentRecipeDetailsBinding binding;
    private final int id;
    private String recipeDetailsURL;

    public RecipeDetailsFragment(int id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecipeDetails();
    }

    private void getRecipeDetails() {
        final String recipeDetailsAPICall = String.format(RecipeListFragmentAbstract.API_URL_ROOT + "/%d/card?apiKey=%s", id, BuildConfig.SPOONACULAR_KEY);
        Log.i(TAG, recipeDetailsAPICall);
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(recipeDetailsAPICall, new RecipeDetailsJsonHttpResponseHandler());
    }

    private class RecipeDetailsJsonHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int i, Headers headers, JSON json) {
            final JSONObject jsonObject = json.jsonObject;
            try {
                recipeDetailsURL = jsonObject.getString("url");
                Glide.with(RecipeDetailsFragment.this).load(recipeDetailsURL).into(binding.ivRecipeDetails);
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