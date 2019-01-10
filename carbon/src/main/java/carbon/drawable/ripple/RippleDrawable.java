package carbon.drawable.ripple;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.core.graphics.drawable.TintAwareDrawable;
import carbon.Carbon;
import carbon.drawable.AlphaDrawable;

public interface RippleDrawable extends AlphaDrawable, TintAwareDrawable {

    Drawable getBackground();

    enum Style {
        Over, Background, Borderless
    }

    boolean setState(int[] stateSet);

    void draw(Canvas canvas);

    void setAlpha(int i);

    int getAlpha();

    void setColorFilter(ColorFilter colorFilter);

    void jumpToCurrentState();

    int getOpacity();

    Style getStyle();

    boolean isHotspotEnabled();

    void setHotspotEnabled(boolean useHotspot);

    void setBounds(int left, int top, int right, int bottom);

    void setBounds(Rect bounds);

    void setHotspot(float x, float y);

    boolean isStateful();

    void setCallback(Drawable.Callback cb);

    ColorStateList getColor();

    void setRadius(int radius);

    int getRadius();

    static RippleDrawable create(ColorStateList color, RippleDrawable.Style style, View view, boolean useHotspot, int radius) {
        RippleDrawable rippleDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rippleDrawable = new RippleDrawableMarshmallow(color, style == RippleDrawable.Style.Background ? view.getBackground() : null, style);
        } else if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            rippleDrawable = new RippleDrawableLollipop(color, style == RippleDrawable.Style.Background ? view.getBackground() : null, style);
        } else {
            rippleDrawable = new RippleDrawableICS(color, style == RippleDrawable.Style.Background ? view.getBackground() : null, style);
        }
        rippleDrawable.setCallback(view);
        rippleDrawable.setHotspotEnabled(useHotspot);
        rippleDrawable.setRadius(radius);
        return rippleDrawable;
    }

    static RippleDrawable create(ColorStateList color, RippleDrawable.Style style, View view, Drawable background, boolean useHotspot, int radius) {
        RippleDrawable rippleDrawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rippleDrawable = new RippleDrawableMarshmallow(color, background, style);
        } else if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            rippleDrawable = new RippleDrawableLollipop(color, background, style);
        } else {
            rippleDrawable = new RippleDrawableICS(color, background, style);
        }
        rippleDrawable.setCallback(view);
        rippleDrawable.setHotspotEnabled(useHotspot);
        rippleDrawable.setRadius(radius);
        return rippleDrawable;
    }
}
