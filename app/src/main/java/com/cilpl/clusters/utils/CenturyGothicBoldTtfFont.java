package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


@SuppressLint("AppCompatCustomView")
public class CenturyGothicBoldTtfFont extends TextView {

    public CenturyGothicBoldTtfFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CenturyGothicBoldTtfFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CenturyGothicBoldTtfFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

                "fonts/GOTHICB0.TTF");
                //"fonts/Arial Nova Light.ttf");
                //"fonts/Arial Nova W07 Bold.ttf");
        setTypeface(tf);
    }

}
