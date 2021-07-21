package com.example.easychef.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.easychef.BuildConfig;
import com.example.easychef.databinding.FragmentRecipeDetailsBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

import static com.example.easychef.AsyncClient.CLIENT;

public class RecipeDetailsFragment extends Fragment {

    private static final String TAG = "RecipeDetailsFragment";
    private FragmentRecipeDetailsBinding binding;
    private final int id;

    public RecipeDetailsFragment(int id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
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
        CLIENT.get(recipeDetailsAPICall, new RecipeDetailsJsonHttpResponseHandler());
    }

    private class RecipeDetailsJsonHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int i, Headers headers, JSON json) {
            final JSONObject jsonObject = json.jsonObject;
            try {
                final String recipeDetailsURL = jsonObject.getString("url");
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