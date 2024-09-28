package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


@SuppressLint("AppCompatCustomView")
public class ButtonEditTwCenMTFont extends Button {

    public ButtonEditTwCenMTFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ButtonEditTwCenMTFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonEditTwCenMTFont(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

             //   "fonts/MS Reference Sans Serif.ttf");
                "fonts/Montserrat-SemiBold.ttf");
        setTypeface(tf);
    }

}
