package com.example.easychef.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easychef.databinding.ItemRecipeBinding;
import com.example.easychef.models.RecipeFromAPI;
import com.example.easychef.models.SavedRecipe;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private final Context context;
    private final List<RecipeFromAPI> recipesFromAPI;

    public RecipeAdapter(Context context, List<RecipeFromAPI> suggestedRecipeFromAPIList) {
        this.context = context;
        recipesFromAPI = suggestedRecipeFromAPIList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ItemRecipeBinding itemRecipeBinding = ItemRecipeBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(itemRecipeBinding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RecipeFromAPI recipeFromAPI = recipesFromAPI.get(position);
        holder.bind(recipeFromAPI);
    }

    @Override
    public int getItemCount() {
        return recipesFromAPI.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRecipeName;
        private final Context context;

        public ViewHolder(@NonNull ItemRecipeBinding itemRecipeBinding, Context context) {
            super(itemRecipeBinding.getRoot());
            tvRecipeName = itemRecipeBinding.tvRecipeName;
            this.context = context;

            itemRecipeBinding.btnSaveRecipe.setOnClickListener(new SaveRecipeButtonViewOnClickListener());
        }

        public void bind(RecipeFromAPI recipeFromAPI) {
            tvRecipeName.setText(recipeFromAPI.getName());
        }

        private class SaveRecipeButtonViewOnClickListener implements View.OnClickListener {
            @Override
            public void onClick(View view) {
                final SavedRecipe savedRecipe = new SavedRecipe();
                savedRecipe.setName(tvRecipeName.getText().toString());
                savedRecipe.setUser(ParseUser.getCurrentUser());
                savedRecipe.saveInBackground(new SaveRecipeSaveCallback());
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
    }

}
