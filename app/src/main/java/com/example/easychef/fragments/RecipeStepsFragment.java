package com.example.easychef.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.easychef.adapters.RecipeStepAdapter;
import com.example.easychef.databinding.FragmentRecipeStepsBinding;
import com.example.easychef.models.RecipeDetailPOJO;
import com.example.easychef.models.StepPOJO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;

public class RecipeStepsFragment extends Fragment {

    private static final String TAG = "RecipeStepsFragment";
    private final int id;
    private final List<StepPOJO> steps;
    private FragmentRecipeStepsBinding binding;
    private RecipeStepAdapter adapter;

    public RecipeStepsFragment(int id) {
        this.id = id;
        steps = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeStepsBinding.inflate(
                inflater,
                container,
                false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new RecipeStepAdapter(getContext(), steps);
        binding.rvSteps.setAdapter(adapter);
        binding.rvSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSteps.setNestedScrollingEnabled(false);

        getRecipeDetails();
    }

    private void getRecipeDetails() {
        getFoodAPI()
                .getRecipeDetails(id)
                .enqueue(new RecipeDetailsCallback());
    }

    private class RecipeDetailsCallback implements Callback<RecipeDetailPOJO> {
        @Override
        public void onResponse(@NotNull Call<RecipeDetailPOJO> call, Response<RecipeDetailPOJO> response) {
            if (response.body().getAnalyzedInstructions().isEmpty()) {
                return;
            }
            steps.addAll(response.body().getAnalyzedInstructions().get(0).getSteps());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(@NotNull Call<RecipeDetailPOJO> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }
}