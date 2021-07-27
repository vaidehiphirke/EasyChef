package com.example.easychef.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.easychef.adapters.AutoCompleteAdapter;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.databinding.FragmentSearchBinding;
import com.example.easychef.models.RecipeResultsPOJO;
import com.example.easychef.utils.AutoCompleteTextWatcher;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;
import com.example.easychef.utils.UXUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;
import static com.example.easychef.adapters.AutoCompleteAdapter.THRESHOLD;
import static com.example.easychef.utils.ParsePOJOUtils.getRecipesFromRecipePOJOS;

public class SearchFragment extends RecipeListFragmentAbstract {

    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private String searchQuery;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        final AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        binding.etSearchForRecipe.setThreshold(THRESHOLD);
        binding.etSearchForRecipe.setAdapter(autoCompleteAdapter);
        binding.etSearchForRecipe.addTextChangedListener(new AutoCompleteTextWatcher(binding.etSearchForRecipe,
                autoCompleteAdapter, "recipe"));

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
        getFoodAPI().getSearchRecipes(searchQuery)
                .enqueue(new SearchCallback());
    }

    @Override
    protected SaveRecipeToFavoritesUtils.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveButPersistOnClickListener();
    }

    private void getExploreRecipes() {
        getFoodAPI().getExploreRecipes()
                .enqueue(new SearchCallback());
    }

    private class GetRecipesOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            UXUtils.hideKeyboard((Activity) getActivity());
            searchQuery = binding.etSearchForRecipe.getText().toString();
            binding.rvRecipes.smoothScrollToPosition(0);
            getRecipesToShowInList();
        }
    }

    private class SearchCallback implements Callback<RecipeResultsPOJO> {
        @Override
        public void onResponse(@NotNull Call<RecipeResultsPOJO> call, @NotNull Response<RecipeResultsPOJO> response) {
            adapter.clear();
            recipes.clear();
            recipes.addAll(getRecipesFromRecipePOJOS(response.body().getResults()));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(@NotNull Call<RecipeResultsPOJO> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }
}