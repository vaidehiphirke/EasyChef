package com.example.easychef.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.easychef.R;
import com.example.easychef.adapters.IngredientAdapter;
import com.example.easychef.databinding.FragmentRecipeOverviewBinding;
import com.example.easychef.models.Ingredient;
import com.example.easychef.models.Recipe;
import com.example.easychef.models.RecipeDetailPOJO;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;
import static com.example.easychef.adapters.RecipeAdapter.ROUNDING_RADIUS;
import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_USER;
import static com.example.easychef.models.Recipe.KEY_RECIPE_ID;
import static com.example.easychef.utils.ParseCacheUtils.setQueryCacheControl;
import static com.example.easychef.utils.ParsePOJOUtils.getIngredientsFromExtendedIngredientPOJOS;
import static com.example.easychef.utils.ParsePOJOUtils.getRecipesFromRecipeDetailPOJOS;


public class RecipeOverviewFragment extends Fragment {

    private static final String TAG = "RecipeDetailsFragment";
    public static final int NUMBER_OF_COLUMNS = 2;
    private FragmentRecipeOverviewBinding binding;
    private final int id;
    private final SaveRecipeToFavoritesUtils.OnUnsavedListener onUnsavedListener;
    private final List<Ingredient> ingredients;

    public RecipeOverviewFragment(int id, SaveRecipeToFavoritesUtils.OnUnsavedListener onUnsavedListener) {
        this.id = id;
        this.onUnsavedListener = onUnsavedListener;
        ingredients = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), ingredients);
        binding.rvIngredients.setAdapter(ingredientAdapter);
        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                NUMBER_OF_COLUMNS,
                StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setAutoMeasureEnabled(true);
        binding.rvIngredients.setLayoutManager(staggeredGridLayoutManager);
        binding.rvIngredients.setNestedScrollingEnabled(false);

        getRecipeDetails();
    }

    private void getRecipeDetails() {
        getFoodAPI()
                .getRecipeDetails(id)
                .enqueue(new RecipeDetailsCallback());
    }

    private class RecipeDetailsCallback implements Callback<RecipeDetailPOJO> {
        @Override
        public void onResponse(@NotNull Call<RecipeDetailPOJO> call, Response<RecipeDetailPOJO> response) {
            setOverviewInfo(binding, response.body());
            setFavoriteButtonStatus(binding, response.body());

            ingredients.addAll(getIngredientsFromExtendedIngredientPOJOS(response.body().getExtendedIngredients()));
        }

        @Override
        public void onFailure(@NotNull Call<RecipeDetailPOJO> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }

    private void setFavoriteButtonStatus(FragmentRecipeOverviewBinding binding, RecipeDetailPOJO pojo) {
        final List<RecipeDetailPOJO> pojos = new ArrayList<>();
        pojos.add(pojo);
        final SaveRecipeToFavoritesUtils saveRecipeToFavoritesUtils = new SaveRecipeToFavoritesUtils(
                binding.btnSaveRecipe,
                null,
                onUnsavedListener,
                getContext(),
                getRecipesFromRecipeDetailPOJOS(pojos, id));

        final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.whereEqualTo(KEY_RECIPE_ID, id);
        query.whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
        setQueryCacheControl(query);
        query.getFirstInBackground(saveRecipeToFavoritesUtils.getSeeIfSavedAndToggleGetCallback());

        binding.btnSaveRecipe.setOnCheckedChangeListener(saveRecipeToFavoritesUtils.getSaveUnsaveButtonListener());
    }

    private void setOverviewInfo(FragmentRecipeOverviewBinding binding, RecipeDetailPOJO pojo) {
        binding.tvRecipeTitle.setText(pojo.getTitle());
        binding.tvReadyInMinutes.setText(String.format("Ready in %d minutes", pojo.getReadyInMinutes()));
        binding.tvServings.setText(String.format("Makes %d servings", pojo.getServings()));
        Glide.with(RecipeOverviewFragment.this).load(pojo.getImage()).transform(new RoundedCornersTransformation(ROUNDING_RADIUS, 0)).into(binding.ivRecipeDetails);

        setDietaryRestrictionIndicator(binding.tvVegetarian, pojo.isVegetarian());
        setDietaryRestrictionIndicator(binding.tvVegan, pojo.isVegan());
        setDietaryRestrictionIndicator(binding.tvDairyFree, pojo.isDairyFree());
        setDietaryRestrictionIndicator(binding.tvGlutenFree, pojo.isGlutenFree());
    }

    private void setDietaryRestrictionIndicator(TextView textView, boolean meetsRestriction) {
        if (!meetsRestriction) {
            textView.setTextColor(getResources().getColor(R.color.soft_gray));
            return;
        }
        textView.setTextColor(getResources().getColor(R.color.pine_green));
        textView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.soft_lime_green)));
    }
}