package com.example.easychef.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.easychef.adapters.IngredientAdapter;
import com.example.easychef.databinding.FragmentIngredientBinding;
import com.example.easychef.models.SavedIngredient;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class IngredientFragment extends Fragment {

    private FragmentIngredientBinding ingredientBinding;
    private List<SavedIngredient> userIngredients;
    private IngredientAdapter ingredientAdapter;

    public IngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ingredientBinding = FragmentIngredientBinding.inflate(inflater, container, false);
        userIngredients = new ArrayList<>();
        return ingredientBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ingredientAdapter = new IngredientAdapter(userIngredients, new IngredientOnLongClickListener());
        ingredientBinding.rvSuggestedRecipes.setAdapter(ingredientAdapter);
        ingredientBinding.rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        ingredientBinding.btnAdd.setOnClickListener(new AddIngredientOnClickListener());
    }

    private class IngredientOnLongClickListener implements IngredientAdapter.OnLongClickListener {
        @Override
        public void onItemLongClicked(int position) {
            userIngredients.remove(position);
            ingredientAdapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Item was removed!", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddIngredientOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final SavedIngredient savedIngredient = new SavedIngredient();
            savedIngredient.setName(ingredientBinding.etAddIngredient.getText().toString());
            savedIngredient.setUser(ParseUser.getCurrentUser());
            savedIngredient.saveInBackground(new SaveIngredientSaveCallback());

            userIngredients.add(savedIngredient);

            ingredientAdapter.notifyItemInserted(userIngredients.size() - 1);
            ingredientBinding.etAddIngredient.setText("");
            Toast.makeText(getContext(), "Ingredient was added!", Toast.LENGTH_SHORT).show();
        }
    }

    private class SaveIngredientSaveCallback implements SaveCallback {
        @Override
        public void done(ParseException e) {
            if (e != null) {
                Log.e("Saving Ingredient", "Error while saving", e);
                Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Ingredient saved", Toast.LENGTH_SHORT).show();
        }
    }
}