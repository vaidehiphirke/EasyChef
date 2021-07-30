package com.example.easychef.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.easychef.fragments.RecipeOverviewFragment;
import com.example.easychef.fragments.RecipeStepsFragment;
import com.example.easychef.utils.SaveRecipeToFavoritesUtils;

import org.jetbrains.annotations.NotNull;

public class RecipeDetailsTabAdapter extends FragmentPagerAdapter {

    private static final int NUMBER_OF_TABS = 2;
    private static final int INDEX_OF_STEPS_TAB = 1;
    private static final String INSTRUCTIONS_TAB_TITLE = "Instructions";
    private static final String RECIPE_OVERVIEW_TAB_TITLE = "Recipe Overview";
    private final int id;
    private final SaveRecipeToFavoritesUtils.OnUnsavedListener listener;

    public RecipeDetailsTabAdapter(@NonNull @NotNull FragmentManager fm, int id, SaveRecipeToFavoritesUtils.OnUnsavedListener listener) {
        super(fm);
        this.id = id;
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        if (position == INDEX_OF_STEPS_TAB) {
            return new RecipeStepsFragment();
        }
        return new RecipeOverviewFragment(id, listener);
    }

    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == INDEX_OF_STEPS_TAB) {
            return INSTRUCTIONS_TAB_TITLE;
        }
        return RECIPE_OVERVIEW_TAB_TITLE;
    }
}
