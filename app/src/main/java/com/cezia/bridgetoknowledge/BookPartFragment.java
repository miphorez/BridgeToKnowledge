package com.cezia.bridgetoknowledge;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class BookPartFragment extends Fragment {
    private long partId;
    private boolean flRestoreView;
    private boolean flRestoreMode = true;

    private int lastBookPosition;

    BookPartFont fontBookPart;
    BookPartPosition positionBookPart;

    interface ChangeBookPartListener {
        void changeBookPart(long id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        flRestoreView = false;
        if (savedInstanceState != null) {
            partId = savedInstanceState.getLong("partId");
            flRestoreView = true;
        }
        return inflater.inflate(R.layout.fragment_book_part, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("partId", partId);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView textView = getTextViewBookPart();
        if (textView != null) {
            BookPart bookPart = BookPart.parts[(int) partId];
            textView.setText(bookPart.getText());
            fontBookPart = new BookPartFont(getContext(), getTextViewBookPart());
            positionBookPart = new BookPartPosition(getContext(), getScrollViewBookPart());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        changeBookPartForListener(partId);
        //восстановить размер текста
        TextView textView = getTextViewBookPart();
        if (textView != null) {
            fontBookPart.setTextSize();
        }
        //восстановить позицию в тексте
        if (flRestoreMode) {
            lastBookPosition = positionBookPart.loadPrefLastBookPosition();
            final ScrollView scrollView = getScrollViewBookPart();
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, lastBookPosition);
                }
            });
        }
        flRestoreMode = true;
    }

    private void changeBookPartForListener(long id) {
        positionBookPart.savePrefLastPart(id);
        Context context = getContext();
        ChangeBookPartListener listener = (ChangeBookPartListener) context;
        View view = getView();
        if (view != null) {
            if (listener != null) {
                listener.changeBookPart(id);
            }
        }
    }

    void setBookPart(long id, boolean modeRestore) {
        this.partId = id;
        this.flRestoreMode = modeRestore;
    }

    long getBookPartId() {
        return partId;
    }

    int getMaxBookPartId() {
        return BookPart.MAX_NUM_PART;
    }

    boolean isFlRestoreView() {
        return flRestoreView;
    }

    void setFlRestoreView(boolean flRestoreView) {
        this.flRestoreView = flRestoreView;
    }

    private TextView getTextViewBookPart() {
        TextView textView = null;
        View view = getView();
        if (view != null) {
            textView = (TextView) view.findViewById(R.id.book_part_view);
        }
        return textView;
    }

    private ScrollView getScrollViewBookPart() {
        ScrollView scrollView = null;
        View view = getView();
        if (view != null) {
            scrollView = (ScrollView) view.findViewById(R.id.scroll_part_view);
        }
        return scrollView;
    }

}
