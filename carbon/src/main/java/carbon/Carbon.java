package carbon;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import carbon.animation.AnimUtils;
import carbon.animation.AnimatedView;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleDrawableFroyo;
import carbon.drawable.ripple.RippleDrawableLollipop;
import carbon.drawable.ripple.RippleDrawableMarshmallow;
import carbon.drawable.ripple.RippleView;
import carbon.shadow.ShadowView;
import carbon.widget.InsetView;
import carbon.widget.MaxSizeView;
import carbon.widget.StateAnimatorView;
import carbon.widget.TintedView;
import carbon.widget.TouchMarginView;

/**
 * Created by Marcin on 2014-12-18.
 */
public class Carbon {
    private Carbon() {
    }

    public static boolean antiAlias = true;
    public static boolean dim = true;

    public static final int DEFAULT_TINT_LIST = 0;

    public static float getDip(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public static float getSp(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, context.getResources().getDisplayMetrics());
    }

    public static void initRippleDrawable(RippleView rippleView, TypedArray a, int[] ids) {
        int carbon_rippleColor = ids[0];
        int carbon_rippleStyle = ids[1];
        int carbon_rippleHotspot = ids[2];
        int carbon_rippleRadius = ids[3];

        View view = (View) rippleView;
        if (view.isInEditMode())
            return;

        ColorStateList color = a.getColorStateList(carbon_rippleColor);

        if (color != null) {
            RippleDrawable.Style style = RippleDrawable.Style.values()[a.getInt(carbon_rippleStyle, RippleDrawable.Style.Background.ordinal())];
            boolean useHotspot = a.getBoolean(carbon_rippleHotspot, true);
            int radius = (int) a.getDimension(carbon_rippleRadius, -1);

            rippleView.setRippleDrawable(createRippleDrawable(color, style, view, useHotspot, radius));
        }
    }

    public static RippleDrawable createRippleDrawable(ColorStateList color, RippleDrawable.Style style, View view, boolean useHotspot, int radius) {
        RippleDrawable rippleDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rippleDrawable = new RippleDrawableMarshmallow(color, style == RippleDrawable.Style.Background ? view.getBackground() : null, style);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rippleDrawable = new RippleDrawableLollipop(color, style == RippleDrawable.Style.Background ? view.getBackground() : null, style);
        } else {
            rippleDrawable = new RippleDrawableFroyo(color, style == RippleDrawable.Style.Background ? view.getBackground() : null, style);
        }
        rippleDrawable.setCallback(view);
        rippleDrawable.setHotspotEnabled(useHotspot);
        rippleDrawable.setRadius(radius);
        return rippleDrawable;
    }

    public static RippleDrawable createRippleDrawable(ColorStateList color, RippleDrawable.Style style, View view, Drawable background, boolean useHotspot, int radius) {
        RippleDrawable rippleDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rippleDrawable = new RippleDrawableMarshmallow(color, background, style);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rippleDrawable = new RippleDrawableLollipop(color, background, style);
        } else {
            rippleDrawable = new RippleDrawableFroyo(color, background, style);
        }
        rippleDrawable.setCallback(view);
        rippleDrawable.setHotspotEnabled(useHotspot);
        rippleDrawable.setRadius(radius);
        return rippleDrawable;
    }

    public static void initTouchMargin(TouchMarginView view, TypedArray a, int[] ids) {
        int carbon_touchMargin = ids[0];
        int carbon_touchMarginLeft = ids[1];
        int carbon_touchMarginTop = ids[2];
        int carbon_touchMarginRight = ids[3];
        int carbon_touchMarginBottom = ids[4];

        int touchMarginAll = (int) a.getDimension(carbon_touchMargin, 0);
        int left = (int) a.getDimension(carbon_touchMarginLeft, touchMarginAll);
        int top = (int) a.getDimension(carbon_touchMarginTop, touchMarginAll);
        int right = (int) a.getDimension(carbon_touchMarginRight, touchMarginAll);
        int bottom = (int) a.getDimension(carbon_touchMarginBottom, touchMarginAll);
        view.setTouchMargin(left, top, right, bottom);
    }

    public static void initInset(InsetView view, TypedArray a, int[] ids) {
        int carbon_inset = ids[0];
        int carbon_insetLeft = ids[1];
        int carbon_insetTop = ids[2];
        int carbon_insetRight = ids[3];
        int carbon_insetBottom = ids[4];
        int carbon_insetColor = ids[5];

        int insetAll = (int) a.getDimension(carbon_inset, InsetView.INSET_NULL);
        int left = (int) a.getDimension(carbon_insetLeft, insetAll);
        int top = (int) a.getDimension(carbon_insetTop, insetAll);
        int right = (int) a.getDimension(carbon_insetRight, insetAll);
        int bottom = (int) a.getDimension(carbon_insetBottom, insetAll);
        view.setInset(left, top, right, bottom);

        view.setInsetColor(a.getColor(carbon_insetColor, 0));
    }

    public static void initMaxSize(MaxSizeView view, TypedArray a, int[] ids) {
        int carbon_maxWidth = ids[0];
        int carbon_maxHeight = ids[1];

        int width = (int) a.getDimension(carbon_maxWidth, Integer.MAX_VALUE);
        int height = (int) a.getDimension(carbon_maxHeight, Integer.MAX_VALUE);
        view.setMaximumWidth(width);
        view.setMaximumHeight(height);
    }

    public static void initTint(TintedView view, TypedArray a, int[] ids) {
        int carbon_tint = ids[0];
        int carbon_tintMode = ids[1];
        int carbon_backgroundTint = ids[2];
        int carbon_backgroundTintMode = ids[3];
        int carbon_animateColorChanges = ids[4];

        if (a.hasValue(carbon_tint)) {
            TypedValue value = new TypedValue();
            a.getValue(carbon_tint, value);
            if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT) {
                view.setTint(value.data);
            } else {
                view.setTint(a.getColorStateList(carbon_tint));
            }
        }
        view.setTintMode(TintedView.modes[a.getInt(carbon_tintMode, 1)]);

        if (a.hasValue(carbon_backgroundTint)) {
            TypedValue value = new TypedValue();
            a.getValue(carbon_backgroundTint, value);
            if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT) {
                view.setBackgroundTint(value.data);
            } else {
                view.setBackgroundTint(a.getColorStateList(carbon_backgroundTint));
            }
        }
        view.setBackgroundTintMode(TintedView.modes[a.getInt(carbon_backgroundTintMode, 1)]);

        if (a.hasValue(carbon_animateColorChanges))
            view.setAnimateColorChangesEnabled(a.getBoolean(carbon_animateColorChanges, false));
    }

    static Paint paint = new Paint();

    @Deprecated
    public static void drawDebugInfo(ViewGroup viewGroup, Canvas canvas) {
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        Rect rect = new Rect();
        float vertLine = Math.min(10, viewGroup.getWidth() / 3);
        float horzLine = Math.min(10, viewGroup.getHeight() / 3);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            viewGroup.getChildAt(i).getHitRect(rect);
            paint.setColor(0x7fff0000);
            canvas.drawRect(rect, paint);
            //paint.setStrokeWidth(2);
            //canvas.drawLine(rect.left,rect.top,rect.left+vertLine,rect.top,paint);

            viewGroup.getChildAt(i).getDrawingRect(rect);
            rect.offset(viewGroup.getChildAt(i).getLeft(), viewGroup.getChildAt(i).getTop());
            paint.setColor(0x7f00ff00);
            canvas.drawRect(rect, paint);
        }

        float step = viewGroup.getResources().getDimension(R.dimen.carbon_grid8);
        paint.setColor(0x7f000000);
        for (float x = 8.5f; x < viewGroup.getWidth(); x += step) {
            canvas.drawLine(x, 0, x, viewGroup.getHeight(), paint);
        }
        for (float y = 8.5f; y < viewGroup.getHeight(); y += step) {
            canvas.drawLine(0, y, viewGroup.getWidth(), y, paint);
        }
    }

    public static void initAnimations(AnimatedView view, TypedArray a, int[] ids) {
        int carbon_inAnimation = ids[0];
        int carbon_outAnimation = ids[1];

        view.setInAnimation(AnimUtils.Style.values()[a.getInt(carbon_inAnimation, 0)]);
        view.setOutAnimation(AnimUtils.Style.values()[a.getInt(carbon_outAnimation, 0)]);
    }

    public static void initElevation(ShadowView view, TypedArray a, int id) {
        float elevation = a.getDimension(id, 0);
        view.setElevation(elevation);
        if (elevation > 0)
            AnimUtils.setupElevationAnimator(((StateAnimatorView) view).getStateAnimator(), view);
    }

    public static int getThemeColor(Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedvalueattr = new TypedValue();
        theme.resolveAttribute(attr, typedvalueattr, true);
        return typedvalueattr.resourceId != 0 ? context.getResources().getColor(typedvalueattr.resourceId) : typedvalueattr.data;
    }

    @Deprecated
    public static boolean isDebugModeEnabled(Context context) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedvalueattr = new TypedValue();
        theme.resolveAttribute(R.attr.carbon_debugMode, typedvalueattr, true);
        return typedvalueattr.resourceId != 0 ? context.getResources().getBoolean(typedvalueattr.resourceId) : false;
    }
}
