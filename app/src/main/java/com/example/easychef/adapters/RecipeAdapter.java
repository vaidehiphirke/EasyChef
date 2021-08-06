package com.example.easychef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easychef.R;
import com.example.easychef.databinding.ItemRecipeBinding;
import com.example.easychef.fragments.RecipeDetailsFragment;
import com.example.easychef.models.Recipe;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.easychef.models.EasyChefParseObjectAbstract.KEY_USER;
import static com.example.easychef.models.Recipe.KEY_RECIPE_ID;
import static com.example.easychef.utils.ParseCacheUtils.setQueryCacheControl;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final int ROUNDING_RADIUS = 84;
    private final Context context;
    private final List<Recipe> recipes;
    private final SaveRecipeToFavoritesUtils.OnUnsavedListener onUnsavedListener;

    public RecipeAdapter(Context context, List<Recipe> recipes, SaveRecipeToFavoritesUtils.OnUnsavedListener onUnsavedListener) {
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

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView tvRecipeName;
        private final ImageView ivRecipeImage;
        private final Context context;
        private final SaveRecipeToFavoritesUtils saveRecipeToFavoritesUtils;

        public ViewHolder(@NonNull ItemRecipeBinding binding, Context context) {
            super(binding.getRoot());
            tvRecipeName = binding.tvRecipeName;
            ivRecipeImage = binding.ivRecipeImage;
            this.context = context;
            saveRecipeToFavoritesUtils = new SaveRecipeToFavoritesUtils(binding.btnSaveRecipe,
                    this, onUnsavedListener, context, recipes);
            binding.btnSaveRecipe.setOnCheckedChangeListener(saveRecipeToFavoritesUtils.getSaveUnsaveButtonListener());

            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe) {
            tvRecipeName.setText(recipe.getName());
            Glide.with(context).load(recipe.getImageUrl()).transform(new RoundedCornersTransformation(ROUNDING_RADIUS, 0)).into(ivRecipeImage);

            final ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
            query.whereEqualTo(KEY_RECIPE_ID, recipe.getId());
            query.whereEqualTo(KEY_USER, ParseUser.getCurrentUser());
            setQueryCacheControl(query);
            query.getFirstInBackground(saveRecipeToFavoritesUtils.getSeeIfSavedAndToggleGetCallback());
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) {
                return;
            }
            final AppCompatActivity activity = (AppCompatActivity) view.getContext();
            final RecipeDetailsFragment fragment = new RecipeDetailsFragment(
                    recipes.get(getAdapterPosition()).getId(),
                    onUnsavedListener);
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in, R.anim.fade_out)
                    .replace(R.id.flContainer, fragment)
                    .commit();
        }
    }
}