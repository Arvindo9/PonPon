package com.jithvar.ponpon.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Arvindo Mondal on 6/9/17.
 * Company name Jithvar
 * Email arvindo@jithvar.com
 */
public class DancingScriptBold  extends android.support.v7.widget.AppCompatTextView  {

    public DancingScriptBold(Context context) {
        super(context);
        setArizoniaFont();
    }

    public DancingScriptBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setArizoniaFont();
    }

    public DancingScriptBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setArizoniaFont();
    }

    private void setArizoniaFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "font/DancingScriptBold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
