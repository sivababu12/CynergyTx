package com.cilpl.clusters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;


@SuppressLint("AppCompatCustomView")
public class EditextTwCenMTBold extends EditText {

    public EditextTwCenMTBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditextTwCenMTBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditextTwCenMTBold(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),

                "fonts/refsanb.ttf");
        setTypeface(tf);
    }

}
