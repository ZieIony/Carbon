package carbon;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;

/**
 * Created by Marcin on 2014-12-18.
 */
public class Carbon {
    private Carbon() {
    }

    public static boolean antiAlias = true;
    public static boolean dim = true;

    public static float getDip(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public static float getSp(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, context.getResources().getDisplayMetrics());
    }

    public static void initRippleDrawable(View view, AttributeSet attrs, int defStyleAttr) {
        if (view.isInEditMode())
            return;
        TypedArray a = view.getContext().obtainStyledAttributes(attrs, R.styleable.Carbon, defStyleAttr, 0);
        int color = a.getColor(R.styleable.Carbon_carbon_rippleColor, 0);
        RippleDrawable.Style style = RippleDrawable.Style.values()[a.getInt(R.styleable.Carbon_carbon_rippleStyle, RippleDrawable.Style.Background.ordinal())];
        boolean useHotspot = a.getBoolean(R.styleable.Carbon_carbon_rippleHotspot, true);

        if (color != 0) {
            RippleDrawable rippleDrawable;
            rippleDrawable = new RippleDrawable(color);
            rippleDrawable.setCallback(view);
            rippleDrawable.setHotspotEnabled(useHotspot);
            rippleDrawable.setStyle(style);

            if (style == RippleDrawable.Style.Borderless) {
                rippleDrawable.setCallback(view);
            } else if (style == RippleDrawable.Style.Background) {
                rippleDrawable.setBackground(view.getBackground());
                view.setBackgroundDrawable(rippleDrawable);
            } else {
                rippleDrawable.setCallback(view);
            }
            ((RippleView) view).setRippleDrawable(rippleDrawable);
        }

        a.recycle();
    }
}
