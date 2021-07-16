package com.example.easychef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychef.databinding.ItemRecipeBinding;
import com.example.easychef.models.SavedRecipe;

import java.util.List;

public class SavedRecipeAdapter extends RecyclerView.Adapter<SavedRecipeAdapter.ViewHolder> {

    private final Context context;
    private final List<SavedRecipe> savedRecipes;
    private final OnClickListener clickListener;

    public SavedRecipeAdapter(Context context, List<SavedRecipe> savedRecipes, OnClickListener clickListener) {
        this.context = context;
        this.savedRecipes = savedRecipes;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemRecipeBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(savedRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return savedRecipes.size();
    }

    public void clear() {
        savedRecipes.clear();
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRecipeName;
        private final Button btnSaveRecipe;

        public ViewHolder(@NonNull ItemRecipeBinding itemRecipeBinding) {
            super(itemRecipeBinding.getRoot());
            tvRecipeName = itemRecipeBinding.tvRecipeName;
            btnSaveRecipe = itemRecipeBinding.btnSaveRecipe;
        }

        public void bind(SavedRecipe savedRecipe) {
            tvRecipeName.setText(savedRecipe.getName());
            btnSaveRecipe.setText("Unsave");
            btnSaveRecipe.setOnClickListener(view -> clickListener.onItemClicked(getAdapterPosition()));
        }
    }
}
