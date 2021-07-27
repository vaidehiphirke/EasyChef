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

import static com.example.easychef.utils.APIEndpointConstants.AUTOCOMPLETE_AND_KEY;
import static com.example.easychef.utils.APIEndpointConstants.COMPLEX_SEARCH_AND_KEY;
import static com.example.easychef.utils.APIEndpointConstants.DETAILS_AND_KEY;
import static com.example.easychef.utils.APIEndpointConstants.FIND_BY_INGREDIENTS_AND_KEY;
import static com.example.easychef.utils.APIEndpointConstants.INGREDIENT_BASE;
import static com.example.easychef.utils.APIEndpointConstants.RECIPE_BASE;

public interface FoodAPI {

    @GET(RECIPE_BASE + FIND_BY_INGREDIENTS_AND_KEY
            + "&sort=min-missed-ingredients")
    Call<List<RecipePOJO>> getPantryRecipes(
            @Query("ingredients") String ingredients
    );

    @GET(RECIPE_BASE + COMPLEX_SEARCH_AND_KEY
            + "&sort=popularity")
    Call<RecipeResultsPOJO> getSearchRecipes(
            @Query("query") String query
    );

    @GET(RECIPE_BASE + AUTOCOMPLETE_AND_KEY)
    Call<List<RecipePOJO>> getAutocompleteRecipes(
            @Query("query") String query
    );

    @GET(RECIPE_BASE + COMPLEX_SEARCH_AND_KEY
            + "&sort=random&popularity")
    Call<RecipeResultsPOJO> getExploreRecipes(
    );

    @GET(RECIPE_BASE + DETAILS_AND_KEY)
    Call<RecipeDetailPOJO> getRecipeDetails(
            @Path("id") int id
    );

    @GET(INGREDIENT_BASE + AUTOCOMPLETE_AND_KEY
            + "&metaInformation=true")
    Call<List<IngredientPOJO>> getAutocompleteIngredients(
            @Query("query") String query
    );
}