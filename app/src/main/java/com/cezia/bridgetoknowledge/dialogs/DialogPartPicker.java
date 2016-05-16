package com.cezia.bridgetoknowledge.dialogs;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import com.cezia.bridgetoknowledge.R;
import org.xmlpull.v1.XmlPullParser;

import java.util.Objects;

public class DialogPartPicker extends DialogFragment {
    private OnClickListener mListener;
    private View itemView;
    StringPicker pickerPart;
    StringPicker pickerFragment;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        itemView = getActivity().getLayoutInflater().inflate(R.layout.dialog_part_picker, null);
        mListener = (OnClickListener) getActivity();

        pickerPart = (StringPicker) itemView.findViewById(R.id.part_picker);
        pickerFragment = (StringPicker) itemView.findViewById(R.id.fragment_picker);
        Bundle params = getArguments();
        if (params == null) {
            throw new RuntimeException("params is null!");
        }

        final String[] values1 = params.getStringArray(getStringResource(R.string.string_picker_dialog_part));
        final String preset1 = params.getString(getStringResource(R.string.string_picker_dialog_preset_part));
        pickerPart.setValues(values1);
        if (preset1 != "") pickerPart.setCurrent(getItemValues(values1, preset1));
        pickerPart.setListener();

        final String[] values2 = params.getStringArray(getStringResource(R.string.string_picker_dialog_fragment));
        final String preset2 = params.getString(getStringResource(R.string.string_picker_dialog_preset_fragment));
        pickerFragment.setValues(values2);
        if (preset2 != "") pickerFragment.setCurrent(getItemValues(values2, preset2));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.string_picker_dialog_title);
        builder.setView(itemView);
        builder.setPositiveButton(R.string.string_picker_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onClick(pickerPart.getCurrentValue(), pickerFragment.getCurrentValue());
            }
        });
        builder.setNegativeButton(R.string.string_picker_dialog_cancel, null);
        return builder.create();
    }

    public void setNewListFragments(String[] values) {
            StringPicker pickerFragment = (StringPicker) itemView.findViewById(R.id.fragment_picker);
            pickerFragment.setValues(values);
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, 10);
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
