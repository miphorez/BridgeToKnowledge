package com.cezia.bridgetoknowledge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Objects;

public class DialogPartPicker extends DialogFragment {

    private OnClickListener mListener;

    private Activity mActivity;

    @Override
    public void onAttach(Activity activity) {
        if (!(activity instanceof OnClickListener)) {
            throw new RuntimeException("Callback is must implements PartPickerDialog.OnClickListener!");
        }
        mListener = (OnClickListener) activity;
        mActivity = activity;
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_part_picker, null, false);

        final StringPicker pickerPart = (StringPicker) view.findViewById(R.id.part_picker);
        final StringPicker pickerFragment = (StringPicker) view.findViewById(R.id.fragment_picker);
        final Bundle params = getArguments();
        if (params == null) {
            throw new RuntimeException("params is null!");
        }

        final String[] values1 = params.getStringArray(getValue(R.string.string_picker_dialog_part));
        final String preset1 = params.getString(getValue(R.string.string_picker_dialog_preset_part));
        pickerPart.setValues(values1);
        if (preset1 != "") pickerPart.setCurrent(getItemValues(values1, preset1));

        final String[] values2 = params.getStringArray(getValue(R.string.string_picker_dialog_fragment));
        final String preset2 = params.getString(getValue(R.string.string_picker_dialog_preset_fragment));
        pickerFragment.setValues(values2);
        if (preset2 != "") pickerFragment.setCurrent(getItemValues(values2, preset2));

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(getValue(R.string.string_picker_dialog_title));
        builder.setPositiveButton(getValue(R.string.string_picker_dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onClick(pickerPart.getCurrentValue(), pickerFragment.getCurrentValue());
            }
        });
        builder.setNegativeButton(getValue(R.string.string_picker_dialog_cancel), null);
        builder.setView(view);
        return builder.create();
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

    private String getValue(final int resId) {
        return mActivity.getString(resId);
    }

    public interface OnClickListener {
        void onClick(final String value1, final String value2);
    }

}
