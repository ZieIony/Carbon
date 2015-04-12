package carbon.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by Marcin on 2014-11-19.
 */
public interface RippleDrawable {

    void setBackground(Drawable drawable);

    Drawable getBackground();

    public static enum Style {
        Over, Background, Borderless
    }

    public boolean setState(int[] stateSet);

    public void draw(Canvas canvas);

    public void setAlpha(int i);

    public void setColorFilter(ColorFilter colorFilter);

    public void jumpToCurrentState();

    public int getOpacity();

    public Style getStyle();

    public void setStyle(Style style);

    public boolean isHotspotEnabled();

    public void setHotspotEnabled(boolean useHotspot);

    public void setBounds(int left, int top, int right, int bottom);

    public void setBounds(Rect bounds);

    public void setHotspot(float x, float y);

    public boolean isStateful();

    public void setCallback(Drawable.Callback cb);

    public int getColor();
}
