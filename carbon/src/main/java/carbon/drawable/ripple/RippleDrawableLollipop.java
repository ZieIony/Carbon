package carbon.drawable.ripple;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RippleDrawableLollipop extends android.graphics.drawable.RippleDrawable implements RippleDrawable {

    private final ColorStateList color;
    private final Drawable background;
    private Style style;
    private boolean useHotspot;
    private int radius;

    public RippleDrawableLollipop(ColorStateList color, Drawable background, Style style) {
        super(color, background, style == Style.Borderless ? null : new ColorDrawable(0xffffffff));
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
    public ColorStateList getColor() {
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
