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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.easychef.R;
import com.example.easychef.adapters.AutoCompleteAdapter;
import com.example.easychef.adapters.RecipeAdapter;
import com.example.easychef.databinding.FragmentSearchBinding;
import com.example.easychef.models.Recipe;
import com.example.easychef.models.RecipeResultsPOJO;
import com.example.easychef.models.SimilarRecipePOJO;
import com.example.easychef.utils.AutoCompleteTextWatcher;
import com.example.easychef.utils.EndlessRecyclerViewScrollListener;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;
import com.example.easychef.utils.UXUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;
import static com.example.easychef.ServiceGenerator.setUserRefreshedExplore;
import static com.example.easychef.adapters.AutoCompleteAdapter.THRESHOLD;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_CREATED_AT;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_USER;
import static com.example.easychef.models.Recipe.KEY_NAME_RECIPE;
import static com.example.easychef.models.Recipe.KEY_RECIPE_ID;
import static com.example.easychef.utils.ParseCacheUtils.setQueryCacheControl;
import static com.example.easychef.utils.ParsePOJOUtils.getRecipesFromRecipePOJOS;
import static com.example.easychef.utils.ParsePOJOUtils.getRecipesFromSimilarRecipePOJOS;

public class SearchFragment extends RecipeListFragmentAbstract {

    private static final String TAG = "SearchFragment";
    private static final int NUMBER_OF_RECENT_LIKES_TO_USE = 3;
    private static final int SIMILAR_RECIPE_ADDITIONS = 2;
    private static final int EXPLORE_RECIPE_ADDITIONS = 6;

    private static final HashMap<Integer, List<Recipe>> idToSimilarRecipesToSuggest = new HashMap<>();
    private static final List<Recipe> popularRecipesToSuggest = new ArrayList<>();
    private static final List<Recipe> sessionCacheWithUserRecs = new ArrayList<>();

    private FragmentSearchBinding binding;
    private String searchQuery;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);

        final AutoCompleteAdapter autoCompleteAdapter = new AutoCompleteAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        binding.etSearchForRecipe.setThreshold(THRESHOLD);
        binding.etSearchForRecipe.setAdapter(autoCompleteAdapter);
        binding.etSearchForRecipe.addTextChangedListener(
                new AutoCompleteTextWatcher(
                        binding.etSearchForRecipe,
                        autoCompleteAdapter,
                        "recipe"));

        swipeContainer = binding.swipeContainer;
        swipeContainer.setOnRefreshListener(new PersonalizedExploreRefreshListener());
        swipeContainer.setColorSchemeResources(
                R.color.pine_green,
                R.color.sky_blue,
                R.color.lime_green,
                R.color.light_coral);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recipes = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), recipes, getOnUnsavedListener());
        binding.rvRecipes.setAdapter(adapter);
        final LinearLayoutManager recipesLayoutManager = new LinearLayoutManager(getContext());
        binding.rvRecipes.setLayoutManager(recipesLayoutManager);
        binding.btnSearch.setOnClickListener(new GetRecipesOnClickListener());

        final EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(recipesLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                preparePersonalizedExplorePage();
            }
        };
        binding.rvRecipes.addOnScrollListener(scrollListener);

        if (sessionCacheWithUserRecs.isEmpty()) {
            preparePersonalizedExplorePage();
            return;
        }
        recipes.addAll(sessionCacheWithUserRecs);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void getRecipesToShowInList() {
        getFoodAPI()
                .getSearchRecipes(searchQuery)
                .enqueue(new SearchCallback());
    }

    @Override
    protected SaveRecipeToFavoritesUtils.OnUnsavedListener getOnUnsavedListener() {
        return new UnsaveButPersistOnClickListener();
    }

    private void addToAndShowPersonalizedExplorePage(List<Recipe> recipesToAdd, int numberOfAdditions) {
        final List<Recipe> recipesToSuggest = getPersonalizedExplorePage(recipesToAdd, numberOfAdditions);
        recipes.addAll(recipesToSuggest);
        sessionCacheWithUserRecs.addAll(recipesToSuggest);
        if (recipes.size() >= NUMBER_OF_RECENT_LIKES_TO_USE) {
            Collections.shuffle(recipes.subList(NUMBER_OF_RECENT_LIKES_TO_USE - 1, recipes.size() - 1));
        }
        adapter.notifyDataSetChanged();
        swipeContainer.setRefreshing(false);
    }

    private List<Recipe> getPersonalizedExplorePage(List<Recipe> recipesToAdd, int numberOfAdditions) {
        final List<Recipe> exploreRecipes = new ArrayList<>();
        for (int i = 0; i < numberOfAdditions; i++) {
            exploreRecipes.add(recipesToAdd.remove(0));
        }
        return exploreRecipes;
    }

    private void preparePersonalizedExplorePage() {
        populateSimilarRecipesMap();
        getExploreRecipes();
    }

    private void getExploreRecipes() {
        getFoodAPI()
                .getExploreRecipes()
                .enqueue(new ExploreCallback());
    }

    private void populateSimilarRecipesMap() {
        final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
        query.include(KEY_NAME_RECIPE);
        query.include(KEY_RECIPE_ID);
        query.addDescendingOrder(KEY_CREATED_AT);
        setQueryCacheControl(query);
        query.findInBackground(new GetKeysFillMapFindCallback());
    }

    private void populateFaveList(Integer id) {
        getFoodAPI()
                .getSimilarRecipes(id)
                .enqueue(new SimilarCallback(id));
    }

    private class GetRecipesOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            binding.rvRecipes.smoothScrollToPosition(0);
            UXUtils.hideKeyboard((Activity) getActivity());
            searchQuery = binding.etSearchForRecipe.getText().toString();
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
            swipeContainer.setRefreshing(false);
        }

        @Override
        public void onFailure(@NotNull Call<RecipeResultsPOJO> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }

    private class GetKeysFillMapFindCallback implements com.parse.FindCallback<Recipe> {
        @Override
        public void done(List<Recipe> keys, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with getting liked recipes", e);
                return;
            }

            final List<Integer> mostRecentLikes = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_RECENT_LIKES_TO_USE && i < keys.size(); i++) {
                mostRecentLikes.add(keys.get(i).getId());
            }

            idToSimilarRecipesToSuggest.keySet().retainAll(mostRecentLikes);
            for (Integer id : mostRecentLikes) {
                if (idToSimilarRecipesToSuggest.putIfAbsent(id, new ArrayList<>()) == null) {
                    populateFaveList(id);
                    continue;
                }
                addToAndShowPersonalizedExplorePage(idToSimilarRecipesToSuggest.get(id), SIMILAR_RECIPE_ADDITIONS);
            }
        }
    }

    private class SimilarCallback implements Callback<List<SimilarRecipePOJO>> {
        private final Integer id;

        SimilarCallback(Integer id) {
            this.id = id;
        }

        @Override
        public void onResponse(@NotNull Call<List<SimilarRecipePOJO>> call, @NotNull Response<List<SimilarRecipePOJO>> response) {
            idToSimilarRecipesToSuggest.get(id).addAll(getRecipesFromSimilarRecipePOJOS(response.body()));
            addToAndShowPersonalizedExplorePage(idToSimilarRecipesToSuggest.get(id), SIMILAR_RECIPE_ADDITIONS);
        }

        @Override
        public void onFailure(@NotNull Call<List<SimilarRecipePOJO>> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }

    private class ExploreCallback implements Callback<RecipeResultsPOJO> {
        @Override
        public void onResponse(@NotNull Call<RecipeResultsPOJO> call, @NotNull Response<RecipeResultsPOJO> response) {
            popularRecipesToSuggest.addAll(getRecipesFromRecipePOJOS(response.body().getResults()));
            addToAndShowPersonalizedExplorePage(popularRecipesToSuggest, EXPLORE_RECIPE_ADDITIONS);
        }

        @Override
        public void onFailure(@NotNull Call<RecipeResultsPOJO> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }

    private class PersonalizedExploreRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            if (binding.etSearchForRecipe.getText().toString().equals("")) {
                setUserRefreshedExplore(true);
                recipes.clear();
                adapter.clear();
                sessionCacheWithUserRecs.clear();
                preparePersonalizedExplorePage();
            }
            swipeContainer.setRefreshing(false);
        }
    }
}