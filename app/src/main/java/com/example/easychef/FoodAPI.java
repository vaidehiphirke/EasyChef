package com.example.easychef;

import com.example.easychef.models.IngredientPOJO;
import com.example.easychef.models.RecipeDetailPOJO;
import com.example.easychef.models.RecipePOJO;
import com.example.easychef.models.RecipeResultsPOJO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.example.easychef.utils.Constants.AUTOCOMPLETE_INGREDIENTS_ENDPOINT;
import static com.example.easychef.utils.Constants.AUTOCOMPLETE_RECIPES_ENDPOINT;
import static com.example.easychef.utils.Constants.EXPLORE_RECIPES_ENDPOINT;
import static com.example.easychef.utils.Constants.RECIPE_DETAILS_ENDPOINT;
import static com.example.easychef.utils.Constants.RECIPE_SEARCH_ENDPOINT;
import static com.example.easychef.utils.Constants.SUGGESTED_RECIPES_BY_INGREDIENTS_ENDPOINT;

public interface FoodAPI {

    @GET(SUGGESTED_RECIPES_BY_INGREDIENTS_ENDPOINT)
    Call<List<RecipePOJO>> getPantryRecipes(
            @Query("ingredients") String ingredients
    );

    @GET(RECIPE_SEARCH_ENDPOINT)
    Call<RecipeResultsPOJO> getSearchRecipes(
            @Query("query") String query
    );

    @GET(AUTOCOMPLETE_RECIPES_ENDPOINT)
    Call<List<RecipePOJO>> getAutocompleteRecipes(
            @Query("query") String query
    );

    @GET(EXPLORE_RECIPES_ENDPOINT)
    Call<RecipeResultsPOJO> getExploreRecipes(
    );

    @GET(RECIPE_DETAILS_ENDPOINT)
    Call<RecipeDetailPOJO> getRecipeDetails(
            @Path("id") int id
    );

    @GET(AUTOCOMPLETE_INGREDIENTS_ENDPOINT)
    Call<List<IngredientPOJO>> getAutocompleteIngredients(
            @Query("query") String query
    );
}