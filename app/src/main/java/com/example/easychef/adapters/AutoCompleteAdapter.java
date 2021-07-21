package com.example.easychef.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.easychef.models.EasyChefParseObjectAbstract;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<EasyChefParseObjectAbstract> {

    public static final int TRIGGER_AUTO_COMPLETE_CODE = 27;
    public static final long AUTO_COMPLETE_DELAY_CODE = 47;
    public static final int THRESHOLD = 1;

    private final List<EasyChefParseObjectAbstract> suggestions;

    public AutoCompleteAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        suggestions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Nullable
    @Override
    public EasyChefParseObjectAbstract getItem(int position) {
        return suggestions.get(position);
    }

    public void setData(List<EasyChefParseObjectAbstract> objects) {
        suggestions.clear();
        suggestions.addAll(objects);
    }

}
