package com.jithvar.ponpon.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Arvindo Mondal on 6/9/17.
 * Company name Jithvar
 * Email arvindo@jithvar.com
 */
public class DancingScriptRegular extends android.support.v7.widget.AppCompatTextView {
    public DancingScriptRegular(Context context) {
        super(context);
        setFont();
    }

    public DancingScriptRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public DancingScriptRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "font/DancingScriptRegular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
