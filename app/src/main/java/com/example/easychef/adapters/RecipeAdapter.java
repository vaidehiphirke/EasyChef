package com.example.easychef.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easychef.R;
import com.example.easychef.databinding.ItemRecipeBinding;
import com.example.easychef.fragments.RecipeDetailsFragment;
import com.example.easychef.models.Recipe;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final String TAG = "RecipeAdapter";
    private final Context context;
    private final List<Recipe> recipes;
    private final OnUnsavedListener onUnsavedListener;

    public RecipeAdapter(Context context, List<Recipe> recipes, OnUnsavedListener onUnsavedListener) {
        this.context = context;
        this.recipes = recipes;
        this.onUnsavedListener = onUnsavedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemRecipeBinding.inflate(LayoutInflater.from(context), parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void clear() {
        recipes.clear();
        notifyDataSetChanged();
    }

    public interface OnUnsavedListener {
        void onUnsavedChecked(int position);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvRecipeName;
        private final ImageView ivRecipeImage;
        private final ToggleButton btnSaveRecipe;
        private final Context context;

        public ViewHolder(@NonNull ItemRecipeBinding itemRecipeBinding, Context context) {
            super(itemRecipeBinding.getRoot());
            tvRecipeName = itemRecipeBinding.tvRecipeName;
            ivRecipeImage = itemRecipeBinding.ivRecipeImage;
            this.context = context;
            btnSaveRecipe = itemRecipeBinding.btnSaveRecipe;
            btnSaveRecipe.setOnCheckedChangeListener(new SaveUnsaveButtonListener());

            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            tvRecipeName.setText(recipe.getName());
            Glide.with(context).load(recipe.getImageUrl()).into(ivRecipeImage);

            final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
            query.whereEqualTo(Recipe.KEY_RECIPE_ID, recipe.getId());
            query.whereEqualTo(Recipe.KEY_USER, ParseUser.getCurrentUser());
            query.getFirstInBackground(new SeeIfSavedAndToggleGetCallback());
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                return;
            }
            final AppCompatActivity activity = (AppCompatActivity) view.getContext();
            final RecipeDetailsFragment fragment = new RecipeDetailsFragment(recipes.get(getAdapterPosition()).getId());
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
        }

        private class SaveUnsaveButtonListener implements CompoundButton.OnCheckedChangeListener {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
                    query.whereEqualTo(Recipe.KEY_RECIPE_ID, recipes.get(getAdapterPosition()).getId());
                    query.whereEqualTo(Recipe.KEY_USER, ParseUser.getCurrentUser());
                    query.getFirstInBackground(new SaveIfNotAlreadySavedGetCallback());
                } else {
                    btnSaveRecipe.setBackgroundDrawable(RecipeAdapter.this.context.getDrawable(android.R.drawable.btn_star_big_off));
                    onUnsavedListener.onUnsavedChecked(getAdapterPosition());
                }
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
                Toast.makeText(context, "Recipe saved", Toast.LENGTH_SHORT).show();
            }
        }

        private class SaveIfNotAlreadySavedGetCallback implements GetCallback<Recipe> {
            @Override
            public void done(Recipe object, ParseException e) {
                if (e == null) {
                    return;
                }
                if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                    final Recipe recipe = new Recipe();
                    recipe.setName(tvRecipeName.getText().toString());
                    recipe.setId(recipes.get(getAdapterPosition()).getId());
                    recipe.setImageUrl(recipes.get(getAdapterPosition()).getImageUrl());
                    Log.i(TAG, "Saving " + recipe.getName());
                    recipe.setUser(ParseUser.getCurrentUser());
                    btnSaveRecipe.setBackgroundDrawable(RecipeAdapter.this.context.getDrawable(android.R.drawable.btn_star_big_on));
                    recipe.saveInBackground(new SaveRecipeSaveCallback());
                } else {
                    Log.e(TAG, "Other error with finding Recipe object", e);
                }
            }
        }

        private class SeeIfSavedAndToggleGetCallback implements GetCallback<Recipe> {
            @Override
            public void done(Recipe object, ParseException e) {
                if (e == null) {
                    btnSaveRecipe.setBackgroundDrawable(RecipeAdapter.this.context.getDrawable(android.R.drawable.btn_star_big_on));
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
}