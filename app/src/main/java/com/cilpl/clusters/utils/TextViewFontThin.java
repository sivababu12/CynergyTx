package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class TextViewFontThin extends TextView {

    public TextViewFontThin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewFontThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewFontThin(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Avignon Book.ttf");
                //"fonts/Roboto-Thin.ttf");
        setTypeface(tf);
    }

}
