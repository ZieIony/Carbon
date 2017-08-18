package carbon;

import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.TintAwareDrawable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import java.security.InvalidParameterException;

import carbon.animation.AnimUtils;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.drawable.AlphaDrawable;
import carbon.drawable.ColorStateListDrawable;
import carbon.drawable.DefaultAccentColorStateList;
import carbon.drawable.DefaultColorStateList;
import carbon.drawable.DefaultIconColorAccentInverseStateList;
import carbon.drawable.DefaultIconColorAccentStateList;
import carbon.drawable.DefaultIconColorInverseStateList;
import carbon.drawable.DefaultIconColorPrimaryInverseStateList;
import carbon.drawable.DefaultIconColorPrimaryStateList;
import carbon.drawable.DefaultIconColorStateList;
import carbon.drawable.DefaultPrimaryColorStateList;
import carbon.drawable.DefaultTextColorAccentStateList;
import carbon.drawable.DefaultTextColorPrimaryStateList;
import carbon.drawable.DefaultTextPrimaryColorInverseStateList;
import carbon.drawable.DefaultTextPrimaryColorStateList;
import carbon.drawable.DefaultTextSecondaryColorInverseStateList;
import carbon.drawable.DefaultTextSecondaryColorStateList;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.Menu;
import carbon.shadow.ShadowView;
import carbon.view.AutoSizeTextView;
import carbon.view.InsetView;
import carbon.view.MaxSizeView;
import carbon.view.StateAnimatorView;
import carbon.view.StrokeView;
import carbon.view.TintedView;
import carbon.view.TouchMarginView;
import carbon.widget.AutoSizeTextMode;

import static carbon.view.RevealView.MAX_RADIUS;

public class Carbon {
    private static final long DEFAULT_REVEAL_DURATION = 200;
    private static long defaultRevealDuration = DEFAULT_REVEAL_DURATION;

    public static final boolean IS_LOLLIPOP = Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH;

    public static PorterDuffXfermode CLEAR_MODE = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    public static final int INVALID_INDEX = -1;

    private Carbon() {
    }

    public static float getDip(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());
    }

    public static float getSp(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 1, context.getResources().getDisplayMetrics());
    }

    public static ColorStateList getDefaultColorStateList(View view, TypedArray a, int id) {
        if (!a.hasValue(id))
            return null;
        TypedValue value = new TypedValue();
        a.getValue(id, value);
        if (value.data != 0x12345678)
            return null;

        Context context = view.getContext();
        if (view.isInEditMode())
            return context.getResources().getColorStateList(R.drawable.carbon_defaultcolorstatelist);

        if (value.resourceId == R.color.carbon_defaultColor) {
            return new DefaultColorStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultColorPrimary) {
            return new DefaultPrimaryColorStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultColorAccent) {
            return new DefaultAccentColorStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultIconColor) {
            return new DefaultIconColorStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultIconColorInverse) {
            return new DefaultIconColorInverseStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultIconColorAccent) {
            return new DefaultIconColorAccentStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultIconColorAccentInverse) {
            return new DefaultIconColorAccentInverseStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultIconColorPrimary) {
            return new DefaultIconColorPrimaryStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultIconColorPrimaryInverse) {
            return new DefaultIconColorPrimaryInverseStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultTextPrimaryColor) {
            return new DefaultTextPrimaryColorStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultTextSecondaryColor) {
            return new DefaultTextSecondaryColorStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultTextPrimaryColorInverse) {
            return new DefaultTextPrimaryColorInverseStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultTextSecondaryColorInverse) {
            return new DefaultTextSecondaryColorInverseStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultTextColorPrimary) {
            return new DefaultTextColorPrimaryStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultTextColorAccent) {
            return new DefaultTextColorAccentStateList(context);
        } else if (value.resourceId == R.color.carbon_defaultRippleColor) {
            int c = Carbon.getThemeColor(context, R.attr.carbon_rippleColor);
            return ColorStateList.valueOf(0x12000000 | (c & 0xffffff));
        } else if (value.resourceId == R.color.carbon_defaultRippleColorPrimary) {
            int c = Carbon.getThemeColor(context, R.attr.colorPrimary);
            return ColorStateList.valueOf(0x12000000 | (c & 0xffffff));
        } else if (value.resourceId == R.color.carbon_defaultRippleColorAccent) {
            int c = Carbon.getThemeColor(context, R.attr.colorAccent);
            return ColorStateList.valueOf(0x12000000 | (c & 0xffffff));
        }

        return null;
    }

    public static void initDefaultBackground(View view, TypedArray a, int id) {
        ColorStateList color = getDefaultColorStateList(view, a, id);
        if (color != null)
            view.setBackgroundDrawable(new ColorStateListDrawable(AnimatedColorStateList.fromList(color, animation -> view.postInvalidate())));
    }

    public static void initDefaultTextColor(TextView view, TypedArray a, int id) {
        ColorStateList color = getDefaultColorStateList(view, a, id);
        if (color != null)
            view.setTextColor(AnimatedColorStateList.fromList(color, animation -> view.postInvalidate()));
    }

    public static void initRippleDrawable(RippleView rippleView, TypedArray a, int[] ids) {
        int carbon_rippleColor = ids[0];
        int carbon_rippleStyle = ids[1];
        int carbon_rippleHotspot = ids[2];
        int carbon_rippleRadius = ids[3];

        View view = (View) rippleView;
        if (view.isInEditMode())
            return;

        ColorStateList color = getDefaultColorStateList(view, a, carbon_rippleColor);
        if (color == null)
            color = a.getColorStateList(carbon_rippleColor);

        if (color != null) {
            RippleDrawable.Style style = RippleDrawable.Style.values()[a.getInt(carbon_rippleStyle, RippleDrawable.Style.Background.ordinal())];
            boolean useHotspot = a.getBoolean(carbon_rippleHotspot, true);
            int radius = (int) a.getDimension(carbon_rippleRadius, -1);

            rippleView.setRippleDrawable(RippleDrawable.create(color, style, view, useHotspot, radius));
        }
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
            ColorStateList color = getDefaultColorStateList((View) view, a, carbon_tint);
            if (color == null)
                color = a.getColorStateList(carbon_tint);
            if (color != null)
                view.setTintList(AnimatedColorStateList.fromList(color, animation -> ((View) view).postInvalidate()));
        }
        view.setTintMode(TintedView.modes[a.getInt(carbon_tintMode, 1)]);

        if (a.hasValue(carbon_backgroundTint)) {
            ColorStateList color = getDefaultColorStateList((View) view, a, carbon_backgroundTint);
            if (color == null)
                color = a.getColorStateList(carbon_backgroundTint);
            if (color != null)
                view.setBackgroundTintList(AnimatedColorStateList.fromList(color, animation -> ((View) view).postInvalidate()));
        }
        view.setBackgroundTintMode(TintedView.modes[a.getInt(carbon_backgroundTintMode, 1)]);

        if (a.hasValue(carbon_animateColorChanges))
            view.setAnimateColorChangesEnabled(a.getBoolean(carbon_animateColorChanges, false));
    }

    public static void initAnimations(AnimatedView view, TypedArray a, int[] ids) {
        if (((View) view).isInEditMode())
            return;

        int carbon_inAnimation = ids[0];
        if (a.hasValue(carbon_inAnimation)) {
            TypedValue typedValue = new TypedValue();
            a.getValue(carbon_inAnimation, typedValue);
            if (typedValue.resourceId != 0) {
                view.setInAnimator(AnimatorInflater.loadAnimator(((View) view).getContext(), typedValue.resourceId));
            } else {
                view.setInAnimator(AnimUtils.Style.values()[typedValue.data].getInAnimator());
            }
        }

        int carbon_outAnimation = ids[1];
        if (a.hasValue(carbon_outAnimation)) {
            TypedValue typedValue = new TypedValue();
            a.getValue(carbon_outAnimation, typedValue);
            if (typedValue.resourceId != 0) {
                view.setOutAnimator(AnimatorInflater.loadAnimator(((View) view).getContext(), typedValue.resourceId));
            } else {
                view.setOutAnimator(AnimUtils.Style.values()[typedValue.data].getOutAnimator());
            }
        }
    }

    public static void initElevation(ShadowView view, TypedArray a, int[] ids) {
        int carbon_elevation = ids[0];
        int carbon_shadowColor = ids[1];

        float elevation = a.getDimension(carbon_elevation, 0);
        view.setElevation(elevation);
        if (elevation > 0)
            AnimUtils.setupElevationAnimator(((StateAnimatorView) view).getStateAnimator(), view);
        view.setElevationShadowColor(a.getColorStateList(carbon_shadowColor));
    }

    public static void initHtmlText(android.widget.TextView textView, TypedArray a, int id) {
        String string = a.getString(id);
        if (string != null)
            textView.setText(Html.fromHtml(string));
    }

    /**
     * @param context context
     * @param attr    attribute to get from the current theme
     * @return color from the current theme
     */
    public static int getThemeColor(Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValue = new TypedValue();
        theme.resolveAttribute(attr, typedValue, true);
        return typedValue.resourceId != 0 ? context.getResources().getColor(typedValue.resourceId) : typedValue.data;
    }

    public static Context getThemedContext(Context context, AttributeSet attributeSet, int[] attrs, int defStyleAttr, int attr) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, attrs, defStyleAttr, 0);
        if (a.hasValue(attr)) {
            int themeId = a.getResourceId(attr, 0);
            a.recycle();
            return new CarbonContextThemeWrapper(context, themeId);
        }
        return context;
    }

    public static int getDrawableAlpha(Drawable background) {
        if (background == null)
            return 255;
        background = background.getCurrent();
        if (background instanceof ColorDrawable)
            return ((ColorDrawable) background).getAlpha();
        if (background instanceof AlphaDrawable)
            return ((AlphaDrawable) background).getAlpha();
        return 255;
    }

    public static float getBackgroundTintAlpha(View child) {
        if (!(child instanceof TintedView))
            return 255;
        ColorStateList tint = ((TintedView) child).getBackgroundTint();
        if (tint == null)
            return 255;
        int color = tint.getColorForState(child.getDrawableState(), tint.getDefaultColor());
        return (color >> 24) & 0xff;
    }

    public static long getDefaultRevealDuration() {
        return defaultRevealDuration;
    }

    public static void setDefaultRevealDuration(long defaultRevealDuration) {
        Carbon.defaultRevealDuration = defaultRevealDuration;
    }

    public static void initStroke(StrokeView strokeView, TypedArray a, int[] ids) {
        int carbon_stroke = ids[0];
        int carbon_strokeWidth = ids[1];

        View view = (View) strokeView;
        ColorStateList color = getDefaultColorStateList(view, a, carbon_stroke);
        if (color == null)
            color = a.getColorStateList(carbon_stroke);

        if (color != null)
            strokeView.setStroke(AnimatedColorStateList.fromList(color, animation -> view.postInvalidate()));
        strokeView.setStrokeWidth(a.getDimension(carbon_strokeWidth, 0));
    }

    public static void initAutoSizeText(AutoSizeTextView view, TypedArray a, int[] ids) {
        int carbon_autoSizeText = ids[0];
        int carbon_autoSizeMinTextSize = ids[1];
        int carbon_autoSizeMaxTextSize = ids[2];
        int carbon_autoSizeStepGranularity = ids[3];
        view.setAutoSizeText(AutoSizeTextMode.values()[a.getInt(carbon_autoSizeText, 0)]);
        view.setMinTextSize(a.getDimension(carbon_autoSizeMinTextSize, 0));
        view.setMaxTextSize(a.getDimension(carbon_autoSizeMaxTextSize, 0));
        view.setAutoSizeStepGranularity(a.getDimension(carbon_autoSizeStepGranularity, 1));
    }

    public static int getThemeResId(Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedValueAttr = new TypedValue();
        theme.resolveAttribute(attr, typedValueAttr, true);
        return typedValueAttr.resourceId;
    }

    public static Menu getMenu(Context context, int resId) {
        Context contextWrapper = CarbonContextWrapper.wrap(context);
        Menu menu = new Menu(contextWrapper);
        MenuInflater inflater = new MenuInflater(contextWrapper);
        inflater.inflate(resId, menu);
        return menu;
    }

    public static Menu getMenu(Context context, android.view.Menu baseMenu) {
        Context contextWrapper = CarbonContextWrapper.wrap(context);
        Menu menu = new Menu(contextWrapper);
        for (int i = 0; i < baseMenu.size(); i++) {
            android.view.MenuItem menuItem = baseMenu.getItem(i);
            menu.add(menuItem.getGroupId(), menuItem.getItemId(), menuItem.getOrder(), menuItem.getTitle()).setIcon(menuItem.getIcon()).setVisible(menuItem.isVisible()).setEnabled(menuItem.isEnabled());
        }
        return menu;
    }

    public static float getRevealRadius(View view, int x, int y, float radius) {
        if (radius >= 0)
            return radius;
        if (radius != MAX_RADIUS)
            throw new InvalidParameterException("radius should be RevealView.MAX_RADIUS, 0.0f or a positive float");
        int w = Math.max(view.getWidth() - x, x);
        int h = Math.max(view.getHeight() - y, y);
        return (float) Math.sqrt(w * w + h * h);
    }

    public static void setTint(Drawable drawable, int tint) {
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTint(tint);
        } else {
            drawable.setColorFilter(new PorterDuffColorFilter(tint, PorterDuff.Mode.MULTIPLY));
        }
    }

    public static void setTintList(Drawable drawable, ColorStateList tint) {
        if (drawable instanceof TintAwareDrawable) {
            ((TintAwareDrawable) drawable).setTintList(tint);
        } else {
            drawable.setColorFilter(tint == null ? null : new PorterDuffColorFilter(tint.getColorForState(drawable.getState(), tint.getDefaultColor()), PorterDuff.Mode.MULTIPLY));
        }
    }

    public static void setTintMode(Drawable drawable, PorterDuff.Mode mode) {
        if (drawable instanceof TintAwareDrawable)
            ((TintAwareDrawable) drawable).setTintMode(mode);
    }

    public static void throwReflectionError() {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        throw new RuntimeException("This feature is implemented using reflection. " +
                "If you see this exception, something in your setup is not standard. " +
                "Please create an issue on https://github.com/ZieIony/Carbon/issues. " +
                "Please provide at least the following information: \n" +
                " - device: " + Build.MANUFACTURER + " " + Build.MODEL + ", API " + Build.VERSION.SDK_INT + "\n" +
                " - method: " + stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + "(...)\n");
    }

}
