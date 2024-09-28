package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class CenturyGothicTtfFont extends TextView {

    public CenturyGothicTtfFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CenturyGothicTtfFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CenturyGothicTtfFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

                "fonts/Century Gothic.ttf");
                //"fonts/Arial Nova Light.ttf");
                //"fonts/Arial Nova W07 Bold.ttf");
        setTypeface(tf);
    }

}
