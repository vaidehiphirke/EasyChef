package com.example.easychef.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.easychef.R;
import com.example.easychef.databinding.ActivityMainBinding;
import com.example.easychef.fragments.FavoritesFragment;
import com.example.easychef.fragments.IngredientFragment;
import com.example.easychef.fragments.ProfileFragment;
import com.example.easychef.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.bottomNavigation.setOnNavigationItemSelectedListener(new EasyChefBottomMenuItemSelectedListener());
        mainBinding.bottomNavigation.setSelectedItemId(R.id.action_ingredient);
    }

    private class EasyChefBottomMenuItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            final Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_profile:
                    fragment = new ProfileFragment();
                    break;
                case R.id.action_search:
                    fragment = new SearchFragment();
                    break;
                case R.id.action_favorites:
                    fragment = new FavoritesFragment();
                    break;
                case R.id.action_ingredient:
                default:
                    fragment = new IngredientFragment();
                    break;
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
            return true;
        }
    }
}