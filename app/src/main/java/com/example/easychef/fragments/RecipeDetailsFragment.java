package com.example.easychef.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.easychef.adapters.RecipeDetailsTabAdapter;
import com.example.easychef.databinding.FragmentRecipeDetailsBinding;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;
import com.google.android.material.tabs.TabLayout;

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
        final FragmentRecipeDetailsBinding binding = FragmentRecipeDetailsBinding.inflate(inflater
                , container, false);
        final ViewPager viewPager = binding.viewpager;
        viewPager.setAdapter(new RecipeDetailsTabAdapter(getChildFragmentManager(), id, onUnsavedListener));

        final TabLayout tabLayout = binding.tlTabs;
        tabLayout.setupWithViewPager(viewPager);
        return binding.getRoot();
    }
}