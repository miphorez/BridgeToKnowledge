package com.cezia.bridgetoknowledge.dialogs;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.cezia.bridgetoknowledge.R;

public class DialogPartPicker extends DialogFragment {
    private OnClickDialogPartListener mListener;
    BookPickerFragment bookPickerFragment;

    public static DialogPartPicker newInstance(Bundle bundle) {
        DialogPartPicker dialogPartPicker = new DialogPartPicker();
        dialogPartPicker.setArguments(bundle);
        return dialogPartPicker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mListener = (OnClickDialogPartListener) getActivity();

        getDialog().setTitle(R.string.string_picker_dialog_title);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_part_picker, null);
        bookPickerFragment = new BookPickerFragment();
        bookPickerFragment.setArguments(getArguments());
        getChildFragmentManager().beginTransaction().add(R.id.fragment_part_picker, bookPickerFragment).commit();

//        int textViewId = getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
//        TextView tv = (TextView) view.findViewById(textViewId);
//        colorTitle = tv.getCurrentTextColor();

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
//        builder.setTitle(R.string.string_picker_dialog_title);
//        builder.setView(view);
//        builder.setPositiveButton(R.string.string_picker_dialog_ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                mListener.onClick(pickerPart.getCurrentValue(), pickerFragment.getCurrentValue());
//            }
//        });
//        builder.setNegativeButton(R.string.string_picker_dialog_cancel, null);
//        builder.create();

        Button button = (Button)view.findViewById(R.id.button_negative);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        button = (Button)view.findViewById(R.id.button_positive);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(bookPickerFragment.getPickerPart().getCurrentValue(),
                        bookPickerFragment.getPickerFragment().getCurrentValue());
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        if (view != null) {
            Button btn = (Button) view.findViewById(R.id.button_positive);
            btn.setTextColor(getContext().getResources().getColor(R.color.colorTitleBlueLight));
            btn = (Button) view.findViewById(R.id.button_negative);
            btn.setTextColor(getContext().getResources().getColor(R.color.colorTitleBlueLight));
        }
    }

    public void setNewListFragments(String[] values) {
//            StringPicker pickerFragment = (StringPicker) itemView.findViewById(R.id.fragment_picker);
//            pickerFragment.setValues(values);
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, 10);
    }

    public interface OnClickDialogPartListener {
        void onClick(final String strPart, final String strFragment);
    }

}
