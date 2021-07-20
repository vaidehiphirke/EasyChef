package com.example.easychef.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychef.models.Ingredient;

import java.util.List;

import static android.R.id.*;
import static android.R.layout.*;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;
    private final OnLongClickListener longClickListener;

    public IngredientAdapter(List<Ingredient> ingredients, OnLongClickListener longClickListener) {
        this.ingredients = ingredients;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(simple_list_item_1, parent, false));
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

        private final TextView tvIngredient;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredient = itemView.findViewById(text1);
        }

        public void bind(Ingredient item) {
            tvIngredient.setText(item.getName());
            tvIngredient.setOnLongClickListener(view -> {
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });
        }
    }
}