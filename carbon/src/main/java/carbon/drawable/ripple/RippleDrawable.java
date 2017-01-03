package carbon.drawable.ripple;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import carbon.drawable.AlphaDrawable;

/**
 * Created by Marcin on 2014-11-19.
 */
public interface RippleDrawable extends AlphaDrawable{

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
}
