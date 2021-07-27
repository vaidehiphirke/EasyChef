package com.example.easychef.utils;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.example.easychef.adapters.AutoCompleteAdapter;
import com.example.easychef.models.EasyChefParseObjectAbstract;
import com.example.easychef.models.IngredientPOJO;
import com.example.easychef.models.RecipePOJO;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.easychef.ServiceGenerator.getFoodAPI;
import static com.example.easychef.utils.ParsePOJOUtils.getIngredientsFromIngredientPOJOS;
import static com.example.easychef.utils.ParsePOJOUtils.getRecipesFromRecipePOJOS;

public class AutoCompleteTextWatcher implements TextWatcher {

    public static final int TRIGGER_AUTO_COMPLETE_CODE = 27;
    public static final long AUTO_COMPLETE_DELAY_CODE = 47;
    private static final String TAG = "AutoCompleteTextWatcher";

    private final AutoCompleteTextView textView;
    private final AutoCompleteAdapter adapter;
    private final String recipeOrIngredient;

    private Handler handler;

    public AutoCompleteTextWatcher(AutoCompleteTextView textView, AutoCompleteAdapter adapter, String recipeOrIngredient) {
        this.textView = textView;
        this.adapter = adapter;
        if (!recipeOrIngredient.equals("ingredient") && !recipeOrIngredient.equals("recipe")) {
            throw new IllegalStateException("Invalid recipeOrIngredient value passed");
        }
        this.recipeOrIngredient = recipeOrIngredient;
    }

    private void makeAutoCompleteAPICall(String query) {
        if (recipeOrIngredient.equals("ingredient")) {
            getFoodAPI().getAutocompleteIngredients(query)
                    .enqueue(new AutoCompleteCallback<>());
            return;
        }
        getFoodAPI().getAutocompleteRecipes(query)
                .enqueue(new AutoCompleteCallback<>());
    }

    private class AutoCompleteCallback<T> implements Callback<List<T>> {
        @Override
        public void onResponse(@NotNull Call<List<T>> call, @NotNull Response<List<T>> response) {
            final List<EasyChefParseObjectAbstract> autocompleteSuggestions;
            if (recipeOrIngredient.equals("ingredient")) {
                autocompleteSuggestions = new ArrayList<>(getIngredientsFromIngredientPOJOS((List<IngredientPOJO>) response.body()));
            } else {
                autocompleteSuggestions = new ArrayList<>(getRecipesFromRecipePOJOS((List<RecipePOJO>) response.body()));
            }
            adapter.setData(autocompleteSuggestions);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(@NotNull Call<List<T>> call, @NotNull Throwable t) {
            Log.e(TAG, "hit exception", t);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int
            count, int after) {
        handler = new Handler(new AutoCompleteHandlerCallback());
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

    private class AutoCompleteHandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what != TRIGGER_AUTO_COMPLETE_CODE) {
                return false;
            }
            if (!TextUtils.isEmpty(textView.getText())) {
                makeAutoCompleteAPICall(textView.getText().toString());
            }
            return true;
        }
    }
}
