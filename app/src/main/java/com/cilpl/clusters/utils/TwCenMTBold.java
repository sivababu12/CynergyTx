package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class TwCenMTBold extends TextView {

    public TwCenMTBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TwCenMTBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwCenMTBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

                "fonts/Montserrat-SemiBold.ttf");
//                "fonts/refsanb.ttf");
                //"fonts/Arial Nova W07 Bold.ttf");
        setTypeface(tf);
    }

}
