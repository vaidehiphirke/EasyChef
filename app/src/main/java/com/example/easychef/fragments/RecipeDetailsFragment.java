package com.example.easychef.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.easychef.databinding.FragmentRecipeDetailsBinding;
import com.example.easychef.models.RecipeDetailPOJO;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;


public class RecipeDetailsFragment extends Fragment {

    private static final String TAG = "RecipeDetailsFragment";
    private FragmentRecipeDetailsBinding binding;
    private final int id;

    public RecipeDetailsFragment(int id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecipeDetails();
    }

    private void getRecipeDetails() {
        getFoodAPI().getRecipeDetails(id)
                .enqueue(new RecipeDetailsCallback());
    }

    private class RecipeDetailsCallback implements Callback<RecipeDetailPOJO> {
        @Override
        public void onResponse(@NotNull Call<RecipeDetailPOJO> call, Response<RecipeDetailPOJO> response) {
            binding.tvRecipeTitle.setText(response.body().getTitle());
            binding.tvReadyInMinutes.setText(String.format("Ready in %d minutes", response.body().getReadyInMinutes()));
            binding.tvServings.setText(String.format("Makes %d servings", response.body().getServings()));
            Glide.with(RecipeDetailsFragment.this).load(response.body().getImage()).into(binding.ivRecipeDetails);

            binding.switchVegetarian.setChecked(response.body().isVegetarian());
            binding.switchVegan.setChecked(response.body().isVegan());
            binding.switchDairyFree.setChecked(response.body().isDairyFree());
            binding.switchGlutenFree.setChecked(response.body().isGlutenFree());
        }

        @Override
        public void onFailure(@NotNull Call<RecipeDetailPOJO> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }
}