package com.example.easychef.utils;

import android.content.Context;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychef.R;
import com.example.easychef.models.Recipe;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_USER;
import static com.example.easychef.models.Recipe.KEY_RECIPE_ID;
import static com.example.easychef.utils.ParseCacheUtils.setQueryCacheControl;

public class SaveRecipeToFavoritesUtils extends Fragment {

    private static final String TAG = "SaveRecipeToFavoritesUtils";
    private final ToggleButton btnSaveRecipe;
    private final OnUnsavedListener onUnsavedListener;
    private final Context context;
    private final List<Recipe> recipes;
    private final RecyclerView.ViewHolder viewHolder;

    public SaveRecipeToFavoritesUtils(ToggleButton btnSaveRecipe, RecyclerView.ViewHolder viewHolder,
                                      OnUnsavedListener onUnsavedListener, Context context, List<Recipe> recipes) {
        this.btnSaveRecipe = btnSaveRecipe;
        this.onUnsavedListener = onUnsavedListener;
        this.context = context;
        this.recipes = recipes;
        this.viewHolder = viewHolder;
    }

    private int getPosition() {
        if (viewHolder == null) {
            return 0;
        }
        return viewHolder.getAdapterPosition();
    }

    public interface OnUnsavedListener {
        void setContext(Context context);

        void setRecipes(List<Recipe> recipes);

        void onUnsavedChecked(int position);
    }

    public SaveUnsaveButtonListener getSaveUnsaveButtonListener() {
        return new SaveUnsaveButtonListener();
    }

    public SeeIfSavedAndToggleGetCallback getSeeIfSavedAndToggleGetCallback() {
        return new SeeIfSavedAndToggleGetCallback();
    }

    private class SaveUnsaveButtonListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                btnSaveRecipe.setBackgroundResource(R.drawable.heart_icon_blue);
                onUnsavedListener.setRecipes(recipes);
                onUnsavedListener.setContext(context);
                onUnsavedListener.onUnsavedChecked(getPosition());
                return;
            }
            final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
            query.whereEqualTo(KEY_RECIPE_ID, recipes.get(getPosition()).getId());
            query.whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
            setQueryCacheControl(query);
            query.getFirstInBackground(new SaveIfNotAlreadySavedGetCallback());
        }
    }

    private class SaveRecipeSaveCallback implements SaveCallback {
        @Override
        public void done(ParseException e) {
            if (e != null) {
                Log.e("Saving Recipe", "Error while saving", e);
                Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(context, "Recipe saved to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private class SaveIfNotAlreadySavedGetCallback implements GetCallback<Recipe> {
        @Override
        public void done(Recipe object, ParseException e) {
            if (e == null) {
                return;
            }
            if (e.getCode() != ParseException.OBJECT_NOT_FOUND) {
                Log.e(TAG, "Other error with finding Recipe object", e);
                return;
            }
            final Recipe recipe = new Recipe.Builder()
                    .name(recipes.get(getPosition()).getName())
                    .id(recipes.get(getPosition()).getId())
                    .imageUrl(recipes.get(getPosition()).getImageUrl())
                    .user(ParseUser.getCurrentUser())
                    .build();
            btnSaveRecipe.setBackgroundResource(R.drawable.heart_icon_filled_blue);
            recipe.saveInBackground(new SaveRecipeSaveCallback());
        }
    }

    private class SeeIfSavedAndToggleGetCallback implements GetCallback<Recipe> {
        @Override
        public void done(Recipe recipe, ParseException e) {
            if (e == null) {
                btnSaveRecipe.setBackgroundResource(R.drawable.heart_icon_filled_blue);
                btnSaveRecipe.toggle();
                return;
            }
            if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                Log.i(TAG, "Recipe not already saved");
            } else {
                Log.e(TAG, "Other error with finding Recipe object", e);
            }
        }
    }
}
