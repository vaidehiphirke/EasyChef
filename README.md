# EasyChef

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
EasyChef is a cooking app that finds recipes using ingredients a user already has.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Cooking
- **Mobile:** This app would be primarily developed for mobile but would perhaps be just as viable via a website as users might benefit from having it open while grocery shopping online/meal planning along the way.
- **Story:** Takes in user's ingredients (what they have in their pantry/fridge) and provides recipe suggestions from them. Users can also search for recipes independent of their entered ingredients.
- **Market:** Any individual looking to find what meals they can make with what they have (possibly college students living on their own for the first time, busy people looking for a quick meal inspiration, etc).
- **Habit:** This app would likely be used pretty regularly, alongside users' daily eating, cooking, and grocery shopping routines.
- **Scope:** First the app will only support recipe suggestion based on user ingredients as well as search; later on a grocery list feature, recommended recipes based on user tastes, autocompletion of search entries for greater ease, etc might make the app more of a general cooking/kitchen helper.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can sign up for a new account
* User can log into their account
* User can log out of their account
* User can add their ingredients to their Pantry/Ingredient list
* User ingredients can be used to get suggested recipes
* User can search for recipes
* User can save recipes to their Favorites
* User can view recipe details by clicking on a recipe

**Optional Nice-to-have Stories**

* Ingredient adding autocompletes
* User can add ingredients to purchase to a Grocery list
* Recipe search autocompletes
* Search tab displays popular recipes on open (like an Explore feature)
* Explore feature is personalized to user's tastes based on data
* Missing ingredients for a specific recipe can be autoadded to a user's Grocery list
* User can swipe to remove an ingredient from their Pantry
* Explore tab has infinite scroll
* Recipe and Ingredient representations have images and nice UI
* App has a cute logo and UI is consistent and coherent

### 2. Screen Archetypes

* Login/register page
   * User can sign up for a new account
   * User can log into their account
* Profile tab
   * User can log out of their account
* Pantry tab
   * User can add their ingredients to their Pantry/Ingredient list
* Suggested Recipes tab
   * User ingredients can be used to get suggested recipes
   * User can view recipe details by clicking on a recipe
* Search tab
   * User can search for recipes
   * User can view recipe details by clicking on a recipe
* Favorites tab
   * User can save recipes to their Favorites
   * User can view recipe details by clicking on a recipe

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Pantry
* Search
* Favorites
* Profile

Optional:
* Grocery List

**Flow Navigation** (Screen to Screen)

* Login/Sign up page -> Main app page
* Main app page
   * Pantry tab -> Suggested Recipes tab
   * Search tab
   * Favorites tab
   * Profile tab

## Wireframes
<img src="https://i.imgur.com/NVZ07cC.jpg" width=740><br>

## Schema

### Models
#### Recipe

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | user        | Pointer to User| account user |
   | imageURL         | String     | API URL to recipe image |
   | title       | String   | recipe title |
   | missedIngredientCount | Number   | number of ingredients needed in addition to user list to make recipe |
   | recipeId    | Number   | API ID of recipe |
   | readyInMinutes | Number   | number of minutes needed to make recipe |
   | servings    | Number   | number of servings |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |

#### Ingredient

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | user        | Pointer to User| account user |
   | imageURL         | String     | API URL to ingredient image |
   | ingredientId    | Number   | API ID of ingredient |
   | name       | String   | ingredient name |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
### Networking
#### List of network requests by screen
   - Pantry tab
      - (Read/GET) Query ingredients saved to account to display in Pantry
         ```java
        final ParseQuery<Ingredient> query = ParseQuery.getQuery(Ingredient.class);
        query.include(Ingredient.KEY_NAME);
        query.addDescendingOrder(Ingredient.KEY_CREATED_AT);
        query.findInBackground(new RetrievePantryIngredientsFindCallback());
         ```
      - (Create/POST) Save new ingredient to account
         ```java
        final Ingredient ingredient = new Ingredient();
        ingredient.setName(binding.etAddIngredient.getText().toString());
        ingredient.setUser(ParseUser.getCurrentUser());
        ingredient.saveInBackground(new SaveIngredientSaveCallback());
         ```
   - Suggested Recipes tab / Search tab / Favorites tab
      - (Read/GET) Query saved recipes
         ```java
        final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.whereEqualTo(Recipe.KEY_RECIPE_ID, recipe.getId());
        query.whereEqualTo(Recipe.KEY_USER, ParseUser.getCurrentUser());
        query.getFirstInBackground(new SeeIfSavedAndToggleGetCallback());
         ```
      - (Create/POST) Save new recipe to account
         ```java
        final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
        query.whereEqualTo(Recipe.KEY_RECIPE_ID, recipes.get(getAdapterPosition()).getId());
        query.whereEqualTo(Recipe.KEY_USER, ParseUser.getCurrentUser());
        query.getFirstInBackground(new SaveIfNotAlreadySavedGetCallback());
         ```
