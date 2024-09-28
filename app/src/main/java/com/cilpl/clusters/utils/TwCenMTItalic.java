package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class TwCenMTItalic extends TextView {

    public TwCenMTItalic(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TwCenMTItalic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwCenMTItalic(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

                "fonts/Italic____.ttf");
        setTypeface(tf);
    }

}
