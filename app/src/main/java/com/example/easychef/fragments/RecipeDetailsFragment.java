package com.example.easychef.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.easychef.adapters.RecipeDetailsTabAdapter;
import com.example.easychef.databinding.FragmentRecipeDetailsBinding;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;

import org.jetbrains.annotations.NotNull;

public class RecipeDetailsFragment extends Fragment {

    private final int id;
    private final SaveRecipeToFavoritesUtils.OnUnsavedListener onUnsavedListener;

    public RecipeDetailsFragment(int id, SaveRecipeToFavoritesUtils.OnUnsavedListener onUnsavedListener) {
        this.id = id;
        this.onUnsavedListener = onUnsavedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentRecipeDetailsBinding binding = FragmentRecipeDetailsBinding.inflate(inflater,
                container, false);
        binding.viewpager.setAdapter(new RecipeDetailsTabAdapter(getChildFragmentManager(), id, onUnsavedListener));
        binding.tlTabs.setupWithViewPager(binding.viewpager);
        return binding.getRoot();
    }
}