package com.example.easychef.activities;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;

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
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.easychef.ServiceGenerator.NANOSECONDS_IN_A_SECOND;

public class MainActivity extends AppCompatActivity {

    private static final int BOTTOM_NAV_ICON_SIZE = 42;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private static long oldestCacheTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oldestCacheTime = System.nanoTime() / NANOSECONDS_IN_A_SECOND;

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.easychef_logo_navy_blue);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        final ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.bottomNavigation.setOnNavigationItemSelectedListener(new EasyChefBottomMenuItemSelectedListener());
        mainBinding.bottomNavigation.setSelectedItemId(R.id.action_ingredient);

        final BottomNavigationMenuView menuView = (BottomNavigationMenuView) mainBinding.bottomNavigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(com.google.android.material.R.id.icon);
            iconView.getLayoutParams().height = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    BOTTOM_NAV_ICON_SIZE,
                    getResources().getDisplayMetrics());
            iconView.getLayoutParams().width = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    BOTTOM_NAV_ICON_SIZE,
                    getResources().getDisplayMetrics());

            iconView.setLayoutParams(iconView.getLayoutParams());
        }
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

    public static int getOldestCacheTime() {
        return (int) oldestCacheTime;
    }
}