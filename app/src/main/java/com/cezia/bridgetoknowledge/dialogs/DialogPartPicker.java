package com.cezia.bridgetoknowledge.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.cezia.bridgetoknowledge.BookMark;
import com.cezia.bridgetoknowledge.EBookPart;
import com.cezia.bridgetoknowledge.R;

public class DialogPartPicker extends DialogFragment {
    private OnClickDialogPartListener mListener;
    private BookPickerFragment bookPickerFragment;
    private Bundle bundle;

    public static DialogPartPicker newInstance(Bundle bundle) {
        DialogPartPicker dialogPartPicker = new DialogPartPicker();
        dialogPartPicker.setArguments(bundle);
        return dialogPartPicker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListener = (OnClickDialogPartListener) getActivity();
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_part_picker, null);
        getDialog().setTitle(R.string.string_picker_dialog_title);

        if (savedInstanceState != null) {
            BookMark bookMark = new BookMark();
            bookMark.setNumPart(savedInstanceState.getInt("numPart"));
            bookMark.setNumFragment(savedInstanceState.getInt("numFragment"));
            bundle = createBundle(getContext(), bookMark);
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    replacePickerTransaction(bundle);
                }
            });
        } else {
            bundle = getArguments();
            addPickerTransaction(bundle);
        }

        Button button = (Button) view.findViewById(R.id.button_negative);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        button = (Button) view.findViewById(R.id.button_positive);
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
    public void onSaveInstanceState(Bundle outState) {
        BookMark bookMark = EBookPart.getBookMarkByStr(bookPickerFragment.getPickerPart().getCurrentValue(),
                bookPickerFragment.getPickerFragment().getCurrentValue());
        if (bookMark != null) {
            outState.putInt("numPart", bookMark.getNumPart());
            outState.putInt("numFragment", bookMark.getNumFragment());
        }
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

    public void setNewPickerTransaction(Context context, BookMark bookMark) {
        Bundle bundle = createBundle(context, bookMark);
        replacePickerTransaction(bundle);
    }

    private void addPickerTransaction(Bundle bundle) {
        bookPickerFragment = new BookPickerFragment();
        bookPickerFragment.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_part_picker, bookPickerFragment, "bookPickerFragment");
        transaction.commit();
    }

    private void replacePickerTransaction(Bundle bundle) {
        bookPickerFragment = new BookPickerFragment();
        bookPickerFragment.setArguments(bundle);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_part_picker, bookPickerFragment, "bookPickerFragment");
        transaction.commit();
    }

    public static Bundle createBundle(Context context, BookMark bookMark) {
        Bundle bundle = new Bundle();

        String[] values1 = EBookPart.getListParts(context);
        bundle.putStringArray(context.getResources().getString(R.string.string_picker_dialog_part), values1);

        String preset1 = EBookPart.getStrPart(bookMark);
        bundle.putString(context.getResources().getString(R.string.string_picker_dialog_preset_part), preset1);

        String[] values2 = EBookPart.getListFragment(bookMark);
        bundle.putStringArray(context.getResources().getString(R.string.string_picker_dialog_fragment), values2);

        String preset2 = Integer.toString(bookMark.getNumFragment());
        bundle.putString(context.getResources().getString(R.string.string_picker_dialog_preset_fragment), preset2);
        return bundle;
    }

    public interface OnClickDialogPartListener {
        void onClick(final String strPart, final String strFragment);
    }

}
