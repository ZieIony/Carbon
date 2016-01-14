package carbon.drawable;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by Marcin on 2015-04-11.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RippleDrawableMarshmallow extends android.graphics.drawable.RippleDrawable implements RippleDrawable {

    private final int color;
    private final Drawable background;
    private Style style;
    private boolean useHotspot;

    public RippleDrawableMarshmallow(int color, Drawable background, Style style) {
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
}
