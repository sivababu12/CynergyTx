package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class TextViewRobotoBlack extends TextView {

    public TextViewRobotoBlack(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewRobotoBlack(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRobotoBlack(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

                "fonts/Roboto-BoldCondensed.ttf");
        setTypeface(tf);
    }

}
