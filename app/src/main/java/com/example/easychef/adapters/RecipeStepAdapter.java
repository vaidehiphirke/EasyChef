
package com.example.easychef.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.easychef.databinding.ItemRecipeStepBinding;
import com.example.easychef.fragments.RecipeOverviewFragment;
import com.example.easychef.models.Ingredient;
import com.example.easychef.models.StepPOJO;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.easychef.fragments.RecipeOverviewFragment.NUMBER_OF_COLUMNS;
import static com.example.easychef.utils.ParsePOJOUtils.getIngredientModelsForEquipment;
import static com.example.easychef.utils.ParsePOJOUtils.getIngredientsFromIngredientPOJOS;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private final List<StepPOJO> steps;
    private final Context context;

    public RecipeStepAdapter(Context context, List<StepPOJO> steps) {
        this.context = context;
        this.steps = steps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemRecipeStepBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvStepNumber;
        private final TextView tvStepText;
        private final TextView tvLengthTimeTaken;
        private final TextView tvIngredientTitle;
        private final RecyclerView rvIngredientsNeeded;
        private final TextView tvEquipmentTitle;
        private final RecyclerView rvEquipmentNeeded;

        public ViewHolder(@NonNull ItemRecipeStepBinding itemRecipeStepBinding) {
            super(itemRecipeStepBinding.getRoot());
            tvStepNumber = itemRecipeStepBinding.tvStepNumber;
            tvStepText = itemRecipeStepBinding.tvStepText;
            tvLengthTimeTaken = itemRecipeStepBinding.tvLengthTimeTaken;
            tvIngredientTitle = itemRecipeStepBinding.tvIngredientTitle;
            rvIngredientsNeeded = itemRecipeStepBinding.rvIngredientsNeeded;
            tvEquipmentTitle = itemRecipeStepBinding.tvEquipmentTitle;
            rvEquipmentNeeded = itemRecipeStepBinding.rvEquipmentNeeded;
        }

        public void bind(StepPOJO pojo) {
            tvStepNumber.setText(String.format("Step #%d", pojo.getNumber()));
            tvStepText.setText(pojo.getStep());
            if (pojo.getLength() != null) {
                tvLengthTimeTaken.setText(String.format("Time taken: %d %s",
                        pojo.getLength().getNumber(), pojo.getLength().getUnit()));
            }
            final List<Ingredient> ingredients = getIngredientsFromIngredientPOJOS(pojo.getIngredients());
            setIngredientRecyclerview(ingredients, rvIngredientsNeeded, tvIngredientTitle);
            final List<Ingredient> equipmentModels = getIngredientModelsForEquipment(pojo.getEquipment());
            setIngredientRecyclerview(equipmentModels, rvEquipmentNeeded, tvEquipmentTitle);
        }

        private void setIngredientRecyclerview(List<Ingredient> ingredients, RecyclerView recyclerView, TextView title) {
            if (ingredients.size() == 0) {
                title.setHeight(0);
            }
            final IngredientAdapter ingredientAdapter = new IngredientAdapter(
                    context,
                    ingredients,
                    new RecipeOverviewFragment.OnLongClickListener());

            recyclerView.setAdapter(ingredientAdapter);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                    NUMBER_OF_COLUMNS,
                    StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setNestedScrollingEnabled(false);
        }
    }
}

