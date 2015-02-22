package carbon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import carbon.animation.RippleStateAnimator;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.widget.StateAnimatorView;
import carbon.widget.TouchMarginView;

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

    public static void initRippleDrawable(RippleView rippleView, AttributeSet attrs, int defStyleAttr) {
        View view = (View) rippleView;
        if (view.isInEditMode())
            return;
        TypedArray a = view.getContext().obtainStyledAttributes(attrs, R.styleable.Carbon, defStyleAttr, 0);
        int color = a.getColor(R.styleable.Carbon_carbon_rippleColor, 0);

        if (color != 0) {
            RippleDrawable.Style style = RippleDrawable.Style.values()[a.getInt(R.styleable.Carbon_carbon_rippleStyle, RippleDrawable.Style.Background.ordinal())];
            boolean useHotspot = a.getBoolean(R.styleable.Carbon_carbon_rippleHotspot, true);

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
            rippleView.setRippleDrawable(rippleDrawable);
            ((StateAnimatorView) view).addStateAnimator(new RippleStateAnimator(rippleView));
        }

        a.recycle();
    }

    public static void initTouchMargin(TouchMarginView view, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = ((View) view).getContext().obtainStyledAttributes(attrs, R.styleable.Carbon, defStyleAttr, 0);

        int touchMarginAll = (int) a.getDimension(R.styleable.Carbon_carbon_touchMargin, 0);
        if (touchMarginAll > 0) {
            view.setTouchMargin(new Rect(touchMarginAll, touchMarginAll, touchMarginAll, touchMarginAll));
        } else {
            int top = (int) a.getDimension(R.styleable.Carbon_carbon_touchMarginTop, 0);
            int left = (int) a.getDimension(R.styleable.Carbon_carbon_touchMarginLeft, 0);
            int right = (int) a.getDimension(R.styleable.Carbon_carbon_touchMarginRight, 0);
            int bottom = (int) a.getDimension(R.styleable.Carbon_carbon_touchMarginBottom, 0);
            if (top > 0 || left > 0 || right > 0 || bottom > 0)
                view.setTouchMargin(left, top, right, bottom);
        }

        a.recycle();

    }

}
