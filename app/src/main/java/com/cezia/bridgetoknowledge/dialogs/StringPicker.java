package com.cezia.bridgetoknowledge.dialogs;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class StringPicker extends LinearLayout {
    private OnChangeOfDataOfPicker mListener;

    private Object mInstance;

    private Class<?> mClazz;

    private String[] mValues;

    private static final int SDK_VERSION;

    private static final String PICKER_CLASS;
    private static final int DELAY_PICKER = 500;

    static {
        SDK_VERSION = Build.VERSION.SDK_INT;
        PICKER_CLASS = SDK_VERSION < Build.VERSION_CODES.FROYO ?
                "com.android.internal.widget.NumberPicker" : "android.widget.NumberPicker";
    }

    private boolean flActionMove = false;

    public StringPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        mListener = (OnChangeOfDataOfPicker) context;
    }

    void setCurrent(final int current) {
        String methodName = isUnderHoneyComb() ? "setCurrent" : "setValue";
        try {
            Method method = mClazz.getMethod(methodName, int.class);
            method.invoke(mInstance, current);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int getCurrent() {
        String methodName = isUnderHoneyComb() ? "getCurrent" : "getValue";
        try {
            Method method = mClazz.getMethod(methodName);
            return (Integer) method.invoke(mInstance);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    String getCurrentValue() {
        return mValues[getCurrent()];
    }

    void setListener() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            try {
                mClazz.getMethod("setOnScrollListener", NumberPicker.OnScrollListener.class).
                        invoke(mInstance, new NumberPicker.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChange(NumberPicker view, int scrollState) {
                                        if (scrollState == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                                            getCurrentValueViaDelay(DELAY_PICKER);
                                            flActionMove = false;
                                        }
                                    }
                                }
                        );
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setValues(final String[] values) {
        mValues = values;

        if (isUnderHoneyComb()) {
            try {
                Method method = mClazz.getMethod("setRange", int.class, int.class, String[].class);
                method.invoke(mInstance, 0, values.length - 1, values);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                mClazz.getMethod("setMaxValue", int.class).invoke(mInstance, values.length - 1);
                mClazz.getMethod("setMinValue", int.class).invoke(mInstance, 0);
                mClazz.getMethod("setDisplayedValues", String[].class).invoke(mInstance, new Object[]{values});
                mClazz.getMethod("setValue", int.class).invoke(mInstance, 0);
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setEnabled(boolean enabled) {
        try {
            mClazz.getMethod("setEnabled", boolean.class).invoke(mInstance, enabled);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void init(final Context context, final AttributeSet attrs) {
        try {
            Class<?> clazz = context.getClassLoader().loadClass(PICKER_CLASS);
            Constructor<?> constructor = clazz.getConstructor(Context.class, AttributeSet.class);
            mInstance = constructor.newInstance(context, attrs);
            mClazz = mInstance.getClass();

            String methodName = "setDescendantFocusability";
            Method method = mClazz.getMethod(methodName, int.class);
            method.invoke(mInstance, NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        addView((View) mInstance);
        setOrientation(VERTICAL);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            actionMasked = ev.getActionMasked();
            switch (actionMasked) {
                case MotionEvent.ACTION_MOVE:
                    flActionMove = true;
                    break;
                case MotionEvent.ACTION_UP:
                    if (!flActionMove) {
                        getCurrentValueViaDelay(DELAY_PICKER);
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void getCurrentValueViaDelay(int delayMs) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListener.onChangeOfDataOfPicker(StringPicker.this, getCurrentValue());
            }
        }, delayMs);
    }

    public static boolean isUnderHoneyComb() {
        return SDK_VERSION < Build.VERSION_CODES.HONEYCOMB;
    }

    public interface OnChangeOfDataOfPicker {
        void onChangeOfDataOfPicker(StringPicker stringPicker, String strCurrValue);
    }
}
