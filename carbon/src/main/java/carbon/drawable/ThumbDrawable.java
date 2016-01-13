package carbon.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import carbon.R;

/**
 * Created by Marcin on 2016-01-13.
 */
public class ThumbDrawable extends Drawable {
    RippleDrawable ripple;

    public ThumbDrawable(View view, int color) {
        if (color != 0) {
            RippleDrawable rippleDrawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rippleDrawable = new RippleDrawableLollipop(color, null, RippleDrawable.Style.Borderless);
            } else {
                rippleDrawable = new RippleDrawableCompat(color, null, view.getContext(), RippleDrawable.Style.Borderless);
            }
            rippleDrawable.setCallback(view);
            rippleDrawable.setHotspotEnabled(false);
            rippleDrawable.setRadius((int) (view.getContext().getResources().getDimension(R.dimen.carbon_1dip) * 30));
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void setAlpha(int i) {
        ripple.setAlpha(i);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public void jumpToCurrentState() {
        if (ripple != null)
            ripple.jumpToCurrentState();
        super.jumpToCurrentState();
    }

    @Override
    public boolean setState(int[] stateSet) {
        if (ripple != null)
            ripple.setState(stateSet);
        return super.setState(stateSet);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
