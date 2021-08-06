package com.example.easychef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easychef.databinding.ItemIngredientBinding;
import com.example.easychef.models.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;
    private final Context context;

    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemIngredientBinding.inflate(LayoutInflater.from(context), parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void clear() {
        ingredients.clear();
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvIngredientName;
        private final ImageView ivIngredientImage;
        private final Context context;

        public ViewHolder(@NonNull ItemIngredientBinding itemIngredientBinding, Context context) {
            super(itemIngredientBinding.getRoot());
            tvIngredientName = itemIngredientBinding.tvIngredientName;
            ivIngredientImage = itemIngredientBinding.ivIngredientImage;
            this.context = context;
        }

        public void bind(Ingredient ingredient) {
            tvIngredientName.setText(ingredient.getName());

            Glide.with(context).load(ingredient.getImageUrl()).into(ivIngredientImage);
        }
    }
}