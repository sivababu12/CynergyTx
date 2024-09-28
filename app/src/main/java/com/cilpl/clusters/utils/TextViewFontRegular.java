package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class TextViewFontRegular extends TextView {

    public TextViewFontRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewFontRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewFontRegular(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

                "fonts/Montserrat-Bold.ttf");
        setTypeface(tf);
    }

}
