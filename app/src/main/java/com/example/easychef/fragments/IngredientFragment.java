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

import com.example.easychef.R;
import com.example.easychef.adapters.IngredientAdapter;
import com.example.easychef.databinding.FragmentIngredientBinding;
import com.example.easychef.models.Ingredient;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class IngredientFragment extends Fragment {

    private static final String TAG = "IngredientFragment";
    private FragmentIngredientBinding binding;
    private List<Ingredient> userIngredients;
    private IngredientAdapter adapter;

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
        binding = FragmentIngredientBinding.inflate(inflater, container, false);
        userIngredients = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new IngredientAdapter(userIngredients, new IngredientOnLongClickListener());
        binding.rvRecipes.setAdapter(adapter);
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnAdd.setOnClickListener(new AddIngredientOnClickListener());
        binding.btnGetRecipes.setOnClickListener(new GetRecipesOnClickListener());

        queryPantryIngredients();
    }

    private void queryPantryIngredients() {
        final ParseQuery<Ingredient> query = ParseQuery.getQuery(Ingredient.class);
        query.include(Ingredient.KEY_NAME);
        query.addDescendingOrder(Ingredient.KEY_CREATED_AT);
        query.findInBackground(new RetrievePantryIngredientsFindCallback());
    }

    private void deletePantryIngredientFromParse(String objectId) {
        Log.i(TAG, "Ingredient objectId for deletion: " + objectId);
        final ParseQuery<Ingredient> query = ParseQuery.getQuery(Ingredient.class);
        query.getInBackground(objectId, new DeletePantryIngredientsGetCallback());
    }

    private class IngredientOnLongClickListener implements IngredientAdapter.OnLongClickListener {
        @Override
        public void onItemLongClicked(int position) {
            final String objectIdToDelete = userIngredients.remove(position).getObjectId();
            deletePantryIngredientFromParse(objectIdToDelete);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Item was removed!", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddIngredientOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final Ingredient ingredient = new Ingredient();
            ingredient.setName(binding.etAddIngredient.getText().toString());
            ingredient.setUser(ParseUser.getCurrentUser());
            ingredient.saveInBackground(new SaveIngredientSaveCallback());

            userIngredients.add(0, ingredient);

            adapter.notifyItemInserted(0);
            binding.etAddIngredient.setText("");
            binding.rvRecipes.smoothScrollToPosition(0);
            Toast.makeText(getContext(), "Ingredient was added!", Toast.LENGTH_SHORT).show();
        }
    }

    private class SaveIngredientSaveCallback implements SaveCallback {
        @Override
        public void done(ParseException e) {
            if (e != null) {
                Log.e(TAG, "Error while saving", e);
                Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Ingredient saved", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetRecipesOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final SuggestedRecipesFromPantryFragment fragment = new SuggestedRecipesFromPantryFragment();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, fragment).commit();
        }
    }

    protected class RetrievePantryIngredientsFindCallback implements FindCallback<Ingredient> {
        @Override
        public void done(List<Ingredient> ingredients, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with getting ingredients", e);
                return;
            }
            adapter.clear();
            userIngredients.addAll(ingredients);
            adapter.notifyDataSetChanged();
        }
    }

    private class DeletePantryIngredientsGetCallback implements com.parse.GetCallback<Ingredient> {
        @Override
        public void done(Ingredient ingredient, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with deleting ingredient", e);
                return;
            }
            ingredient.deleteInBackground();
            Toast.makeText(getContext(), "Ingredient deleted", Toast.LENGTH_SHORT).show();
        }
    }
}