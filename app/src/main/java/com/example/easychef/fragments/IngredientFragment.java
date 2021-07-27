package com.example.easychef.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.easychef.R;
import com.example.easychef.adapters.AutoCompleteAdapter;
import com.example.easychef.adapters.IngredientAdapter;
import com.example.easychef.databinding.FragmentIngredientBinding;
import com.example.easychef.models.EasyChefParseObjectAbstract;
import com.example.easychef.models.Ingredient;
import com.example.easychef.models.IngredientPOJO;
import com.example.easychef.utils.UXUtils;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;
import static com.example.easychef.adapters.AutoCompleteAdapter.AUTO_COMPLETE_DELAY_CODE;
import static com.example.easychef.adapters.AutoCompleteAdapter.THRESHOLD;
import static com.example.easychef.adapters.AutoCompleteAdapter.TRIGGER_AUTO_COMPLETE_CODE;
import static com.example.easychef.models.Ingredient.KEY_NAME_INGREDIENT;
import static com.example.easychef.utils.ParsePOJOUtils.getIngredientsFromIngredientPOJOS;

public class IngredientFragment extends Fragment {

    private static final String TAG = "IngredientFragment";
    private FragmentIngredientBinding binding;
    private List<Ingredient> userIngredients;
    private IngredientAdapter adapter;

    private AutoCompleteAdapter autoCompleteAdapter;

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

        autoCompleteAdapter = new AutoCompleteAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        binding.etAddIngredient.setThreshold(THRESHOLD);
        binding.etAddIngredient.setAdapter(autoCompleteAdapter);
        binding.etAddIngredient.addTextChangedListener(new IngredientAutocompleteTextWatcher());
        binding.etAddIngredient.setOnItemClickListener(new AddIngredientOnItemClickListener());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new IngredientAdapter(getContext(), userIngredients, new IngredientOnLongClickListener());
        binding.rvRecipes.setAdapter(adapter);
        binding.rvRecipes.setLayoutManager(new LinearLayoutManager(getContext()));

        binding.btnGetRecipes.setOnClickListener(new GetRecipesOnClickListener());

        queryPantryIngredients();
    }

    private void queryPantryIngredients() {
        final ParseQuery<Ingredient> query = ParseQuery.getQuery(Ingredient.class);
        query.include(KEY_NAME_INGREDIENT);
        query.addDescendingOrder(Ingredient.KEY_CREATED_AT);
        query.findInBackground(new RetrievePantryIngredientsFindCallback());
    }

    private void makeIngredientAPICall(String query) {
        getFoodAPI().getAutocompleteIngredients(query)
                .enqueue(new AutoCompleteCallback());
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

    private class IngredientAutocompleteTextWatcher implements TextWatcher {
        private Handler handler;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int
                count, int after) {
            handler = new Handler(new IngredientHandlerCallback());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            handler.removeMessages(TRIGGER_AUTO_COMPLETE_CODE);
            handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE_CODE,
                    AUTO_COMPLETE_DELAY_CODE);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        private class IngredientHandlerCallback implements Handler.Callback {
            @Override
            public boolean handleMessage(@NonNull Message message) {
                if (message.what != TRIGGER_AUTO_COMPLETE_CODE) {
                    return false;
                }
                if (!TextUtils.isEmpty(binding.etAddIngredient.getText())) {
                    makeIngredientAPICall(binding.etAddIngredient.getText().toString());
                }
                return true;
            }
        }
    }

    private class AutoCompleteCallback implements Callback<List<IngredientPOJO>> {
        @Override
        public void onResponse(@NotNull Call<List<IngredientPOJO>> call, @NotNull Response<List<IngredientPOJO>> response) {
            final List<EasyChefParseObjectAbstract> autocompleteIngredients = new ArrayList<>(getIngredientsFromIngredientPOJOS(response.body()));
            autoCompleteAdapter.setData(autocompleteIngredients);
            autoCompleteAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(@NotNull Call<List<IngredientPOJO>> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }

    private class AddIngredientOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            UXUtils.hideKeyboard((Activity) getActivity());

            final Ingredient ingredient = (Ingredient) autoCompleteAdapter.getItem(position);
            ingredient.saveInBackground(new SaveIngredientSaveCallback());

            userIngredients.add(0, ingredient);

            adapter.notifyItemInserted(0);
            binding.etAddIngredient.setText("");
            binding.rvRecipes.smoothScrollToPosition(0);
        }

        private class SaveIngredientSaveCallback implements SaveCallback {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Ingredient was added!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}