package com.example.easychef.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychef.models.SavedIngredient;

import java.util.List;

import static android.R.id.*;
import static android.R.layout.*;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final List<SavedIngredient> savedIngredientList;
    private final OnLongClickListener longClickListener;

    public IngredientAdapter(List<SavedIngredient> savedIngredientList, OnLongClickListener longClickListener) {
        this.savedIngredientList = savedIngredientList;
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        final SavedIngredient item = savedIngredientList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return savedIngredientList.size();
    }

    public void clear() {
        savedIngredientList.clear();
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

        public void bind(SavedIngredient item) {
            tvIngredient.setText(item.getName());
            tvIngredient.setOnLongClickListener(view -> {
                longClickListener.onItemLongClicked(getAdapterPosition());
                return true;
            });
        }
    }
}
