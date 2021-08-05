package com.example.easychef.utils;

import com.example.easychef.models.EquipmentPOJO;
import com.example.easychef.models.ExtendedIngredientPOJO;
import com.example.easychef.models.Ingredient;
import com.example.easychef.models.IngredientPOJO;
import com.example.easychef.models.Recipe;
import com.example.easychef.models.RecipeDetailPOJO;
import com.example.easychef.models.RecipePOJO;
import com.example.easychef.models.SimilarRecipePOJO;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ParsePOJOUtils {

    private static final String EQUIPMENT_IMAGE_ROOT = "https://spoonacular.com/cdn/equipment_100x100/%s";

    public static List<Recipe> getRecipesFromRecipePOJOS(List<RecipePOJO> recipePOJOs) {
        final List<Recipe> recipes = new ArrayList<>();
        for (RecipePOJO pojo : recipePOJOs) {
            final Recipe.Builder builder = new Recipe.Builder()
                    .id(pojo.getId())
                    .name(pojo.getTitle())
                    .user(ParseUser.getCurrentUser());
            if (pojo.getImageUrl() != null) {
                builder.imageUrl(pojo.getImageUrl());
            }
            recipes.add(builder.build());
        }
        return recipes;
    }

    public static List<Recipe> getRecipesFromSimilarRecipePOJOS(List<SimilarRecipePOJO> recipePOJOs) {
        final List<Recipe> recipes = new ArrayList<>();
        for (SimilarRecipePOJO pojo : recipePOJOs) {
            final Recipe.Builder builder = new Recipe.Builder()
                    .id(pojo.getId())
                    .name(pojo.getTitle())
                    .user(ParseUser.getCurrentUser());
            if (pojo.getImageUrl() != null) {
                builder.imageUrl(pojo.getImageUrl());
            }
            recipes.add(builder.build());
        }
        return recipes;
    }

    public static List<Ingredient> getIngredientsFromIngredientPOJOS(List<IngredientPOJO> ingredientPOJOs) {
        final List<Ingredient> ingredients = new ArrayList<>();
        for (IngredientPOJO pojo : ingredientPOJOs) {
            final Ingredient.Builder builder = new Ingredient.Builder()
                    .id(pojo.getId())
                    .name(pojo.getName())
                    .user(ParseUser.getCurrentUser());
            if (pojo.getImageUrl() != null) {
                builder.imageUrl(pojo.getImageUrl());
            }
            ingredients.add(builder.build());
        }
        return ingredients;
    }

    public static List<Recipe> getRecipesFromRecipeDetailPOJOS(List<RecipeDetailPOJO> recipePOJOs, int id) {
        final List<Recipe> recipes = new ArrayList<>();
        for (RecipeDetailPOJO pojo : recipePOJOs) {
            final Recipe.Builder builder = new Recipe.Builder()
                    .id(id)
                    .name(pojo.getTitle())
                    .user(ParseUser.getCurrentUser());
            if (pojo.getImage() != null) {
                builder.imageUrl(pojo.getImage());
            }
            recipes.add(builder.build());
        }
        return recipes;
    }

    public static List<Ingredient> getIngredientsFromExtendedIngredientPOJOS(List<ExtendedIngredientPOJO> ingredientPOJOs) {
        final List<Ingredient> ingredients = new ArrayList<>();
        for (ExtendedIngredientPOJO pojo : ingredientPOJOs) {
            final Ingredient.Builder builder = new Ingredient.Builder()
                    .name(pojo.getOriginalString())
                    .user(ParseUser.getCurrentUser());
            if (pojo.getImage() != null) {
                builder.imageUrl(pojo.getImage());
            }
            ingredients.add(builder.build());
        }
        return ingredients;
    }

    public static List<Ingredient> getIngredientModelsForEquipment(List<EquipmentPOJO> equipmentPOJOS) {
        final List<Ingredient> ingredients = new ArrayList<>();
        for (EquipmentPOJO pojo : equipmentPOJOS) {
            final Ingredient.Builder builder = new Ingredient.Builder()
                    .name(pojo.getName())
                    .user(ParseUser.getCurrentUser());
            if (pojo.getImageUrl() != null) {
                builder.imageUrl(pojo.getImageUrl(), EQUIPMENT_IMAGE_ROOT);
            }
            ingredients.add(builder.build());
        }
        return ingredients;
    }
}