package com.example.easychef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easychef.databinding.ItemIngredientBinding;
import com.example.easychef.databinding.ItemRecipeBinding;
import com.example.easychef.models.Ingredient;

import java.util.List;

import static android.R.id.*;
import static android.R.layout.*;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;
    private final OnLongClickListener longClickListener;
    private final Context context;

    public IngredientAdapter(Context context, List<Ingredient> ingredients, OnLongClickListener longClickListener) {
        this.context = context;
        this.ingredients = ingredients;
        this.longClickListener = longClickListener;
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

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

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
            tvIngredientName.setOnLongClickListener(view -> {
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });

            Glide.with(context).load(ingredient.getImageUrl()).into(ivIngredientImage);
        }
    }
}