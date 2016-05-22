package com.cezia.bridgetoknowledge.dialogs;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cezia.bridgetoknowledge.R;

import java.util.Objects;

public class BookPickerFragment extends Fragment {
    public BookPickerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pickers, null);
        setPickersByBundle(view, getArguments());
        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        StringPicker pickerPart = getPicker(R.id.part_picker);
//        if (pickerPart != null) {
//        }
//        StringPicker pickerFragment = getPicker(R.id.fragment_picker);
//        if (pickerFragment != null) {
//        }
//    }

    public void setPickersByBundle(View view, Bundle params) {
        final String[] values1 = params.getStringArray(getContext().getResources().getString(R.string.string_picker_dialog_part));
        final String preset1 = params.getString(getContext().getResources().getString(R.string.string_picker_dialog_preset_part));
        StringPicker pickerPart = (StringPicker)view.findViewById(R.id.part_picker);
        if (pickerPart != null) {
            pickerPart.setValues(values1);
        }
        if (preset1 != "") pickerPart.setCurrent(getItemValues(values1, preset1));
        pickerPart.setListener();

        final String[] values2 = params.getStringArray(getContext().getResources().getString(R.string.string_picker_dialog_fragment));
        final String preset2 = params.getString(getContext().getResources().getString(R.string.string_picker_dialog_preset_fragment));
        StringPicker pickerFragment = (StringPicker)view.findViewById(R.id.fragment_picker);
        if (pickerFragment != null) {
            pickerFragment.setValues(values2);
        }
        if (preset2 != "") pickerFragment.setCurrent(getItemValues(values2, preset2));
    }

    private int getItemValues(String[] values, String preset) {
        int itemValues = -1;
        for (String iStr : values) {
            itemValues++;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Objects.equals(iStr, preset)) break;
            } else {
                if (iStr == preset) break;
            }
        }
        return itemValues;
    }

    public StringPicker getPickerPart() {
        return getPicker(R.id.part_picker);
    }

    public StringPicker getPickerFragment() {
        return getPicker(R.id.fragment_picker);
    }

    private StringPicker getPicker(int id) {
        StringPicker stringPicker = null;
        View view = getView();
        if (view != null) {
            stringPicker = (StringPicker) view.findViewById(id);
        }
        return stringPicker;
    }
}
