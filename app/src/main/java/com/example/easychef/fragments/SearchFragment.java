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

import com.example.easychef.adapters.AutoCompleteAdapter;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.databinding.FragmentSearchBinding;
import com.example.easychef.models.EasyChefParseObjectAbstract;
import com.example.easychef.models.RecipePOJO;
import com.example.easychef.models.RecipesPOJO;
import com.example.easychef.models.RecipeResultsPOJO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;
import static com.example.easychef.adapters.AutoCompleteAdapter.AUTO_COMPLETE_DELAY_CODE;
import static com.example.easychef.adapters.AutoCompleteAdapter.THRESHOLD;
import static com.example.easychef.adapters.AutoCompleteAdapter.TRIGGER_AUTO_COMPLETE_CODE;
import static com.example.easychef.utils.ParsePOJOUtils.getRecipesFromRecipePOJOS;

public class SearchFragment extends RecipeListFragmentAbstract {

    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding binding;
    private String searchQuery;
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
        getFoodAPI().getSearchRecipes(searchQuery)
                .enqueue(new SearchCallback());
    }

    @Override
    protected RecipeAdapter.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveButPersistOnClickListener();
    }

    private void makeRecipeAPICall(String query) {
        getFoodAPI().getAutocompleteRecipes(query)
                .enqueue(new AutoCompleteCallback());
    }

    private void getExploreRecipes() {
        getFoodAPI().getExploreRecipes()
                .enqueue(new ExploreCallback());
    }

    private class GetRecipesOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            searchQuery = binding.etSearchForRecipe.getText().toString();
            getRecipesToShowInList();
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

    private class AutoCompleteCallback implements Callback<List<RecipePOJO>> {
        @Override
        public void onResponse(@NotNull Call<List<RecipePOJO>> call, @NotNull Response<List<RecipePOJO>> response) {
            final List<EasyChefParseObjectAbstract> autocompleteRecipes = new ArrayList<>(getRecipesFromRecipePOJOS(response.body()));
            autoCompleteAdapter.setData(autocompleteRecipes);
            autoCompleteAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(@NotNull Call<List<RecipePOJO>> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }

    private class ExploreCallback implements Callback<RecipesPOJO> {
        @Override
        public void onResponse(@NotNull Call<RecipesPOJO> call, @NotNull Response<RecipesPOJO> response) {
            adapter.clear();
            recipes.clear();
            recipes.addAll(getRecipesFromRecipePOJOS(response.body().getRecipes()));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(@NotNull Call<RecipesPOJO> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }
}