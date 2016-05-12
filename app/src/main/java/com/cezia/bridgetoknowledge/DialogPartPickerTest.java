package com.cezia.bridgetoknowledge;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
        import android.view.View;

import java.util.Objects;

public class DialogPartPickerTest extends DialogFragment {
    private OnClickListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_part_picker, null);

        final StringPicker pickerPart = (StringPicker) view.findViewById(R.id.part_picker);
        final StringPicker pickerFragment = (StringPicker) view.findViewById(R.id.fragment_picker);
        final Bundle params = getArguments();
        if (params == null) {
            throw new RuntimeException("params is null!");
        }

        final String[] values1 = params.getStringArray(getStringResource(R.string.string_picker_dialog_part));
        final String preset1 = params.getString(getStringResource(R.string.string_picker_dialog_preset_part));
        pickerPart.setValues(values1);
        if (preset1 != "") pickerPart.setCurrent(getItemValues(values1, preset1));

        final String[] values2 = params.getStringArray(getStringResource(R.string.string_picker_dialog_fragment));
        final String preset2 = params.getString(getStringResource(R.string.string_picker_dialog_preset_fragment));
        pickerFragment.setValues(values2);
        if (preset2 != "") pickerFragment.setCurrent(getItemValues(values2, preset2));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.string_picker_dialog_title);
        builder.setView(view);
        builder.setPositiveButton(R.string.string_picker_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onClick(pickerPart.getCurrentValue(), pickerFragment.getCurrentValue());
            }
        });
        builder.setNegativeButton(R.string.string_picker_dialog_cancel, null);
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

    private String getStringResource(int idRes){
        return getActivity().getResources().getString(idRes);
    }

    public interface OnClickListener {
        void onClick(final String strPart, final String strFragment);
    }

}
