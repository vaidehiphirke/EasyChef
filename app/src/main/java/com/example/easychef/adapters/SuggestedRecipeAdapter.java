package com.example.easychef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychef.databinding.ItemRecipeBinding;
import com.example.easychef.models.Recipe;

import java.util.List;


public class SuggestedRecipeAdapter extends RecyclerView.Adapter<SuggestedRecipeAdapter.ViewHolder> {

    private final Context context;
    private final List<Recipe> recipes;

    public SuggestedRecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        recipes = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemRecipeBinding itemRecipeBinding = ItemRecipeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(itemRecipeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Recipe recipe = recipes.get(position);
        holder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRecipeName;

        public ViewHolder(@NonNull ItemRecipeBinding itemRecipeBinding) {
            super(itemRecipeBinding.getRoot());
            tvRecipeName = itemRecipeBinding.tvRecipeName;
        }

        public void bind(Recipe recipe) {
            tvRecipeName.setText(recipe.getRecipeName());
        }
    }
}
