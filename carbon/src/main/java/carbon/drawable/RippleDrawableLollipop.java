package carbon.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Marcin on 2015-04-11.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RippleDrawableLollipop extends android.graphics.drawable.RippleDrawable implements RippleDrawable {

    private final int color;
    private final Drawable background;
    private Style style;
    private boolean useHotspot;
    private int radius;

    public RippleDrawableLollipop(int color, Drawable background, Style style) {
        super(ColorStateList.valueOf(color), background, style == Style.Borderless ? null : new ColorDrawable(0xffffffff));
        this.style = style;
        this.color = color;
        this.background = background;
    }

    @Override
    public Drawable getBackground() {
        return background;
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public boolean isHotspotEnabled() {
        return useHotspot;
    }

    @Override
    public void setHotspotEnabled(boolean useHotspot) {
        this.useHotspot = useHotspot;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setRadius(int radius) {
        this.radius = radius;
        try {
            Method setMaxRadiusMethod = android.graphics.drawable.RippleDrawable.class.getDeclaredMethod("setMaxRadius", int.class);
            setMaxRadiusMethod.invoke(this, radius);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getRadius() {
        return radius;
    }
}
