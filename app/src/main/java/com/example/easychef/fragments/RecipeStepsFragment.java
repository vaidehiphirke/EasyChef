package com.example.easychef.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.easychef.databinding.FragmentRecipeStepsBinding;

import org.jetbrains.annotations.NotNull;

public class RecipeStepsFragment extends Fragment {

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final FragmentRecipeStepsBinding binding = FragmentRecipeStepsBinding.inflate(inflater
                , container, false);
        return binding.getRoot();
    }
}