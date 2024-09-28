package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class TwCenMTFont extends TextView {

    public TwCenMTFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TwCenMTFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwCenMTFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

              //  "fonts/MS Reference Sans Serif.ttf");
                "fonts/Montserrat-Regular.ttf");
                //"fonts/Arial Nova Light.ttf");
                //"fonts/Arial Nova W07 Bold.ttf");
        setTypeface(tf);
    }

}
