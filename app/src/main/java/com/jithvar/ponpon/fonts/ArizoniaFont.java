package com.jithvar.ponpon.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Arvindo Mondal on 29/8/17.
 * Company name Jithvar
 * Email arvindo@jithvar.com
 */
public class ArizoniaFont extends android.support.v7.widget.AppCompatTextView {

    public ArizoniaFont(Context context) {
        super(context);
        setArizoniaFont();
    }

    public ArizoniaFont(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setArizoniaFont();
    }

    public ArizoniaFont(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setArizoniaFont();
    }

    private void setArizoniaFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "font/arizonia.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
