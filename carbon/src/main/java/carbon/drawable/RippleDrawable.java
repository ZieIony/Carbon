package carbon.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import carbon.Carbon;
import carbon.animation.AnimUtils;

/**
 * Created by Marcin on 2014-11-19.
 */
public class RippleDrawable extends Drawable {
    private final int alpha;
    private long duration;
    private long downTime, upTime;
    private static final int LONGPRESS_DURATION = 4000;
    private static final int RIPPLE_DURATION = 400;
    private static final int FADE_DURATION = 300;

    private Paint paint = new Paint();
    private PointF hotspot;
    private int color;
    private Drawable background;
    private Interpolator interpolator;
    private float from, to;
    private boolean pressed, useHotspot;
    private Style style;

    public static enum Style {
        Over, Background, Borderless
    }

    public void onRelease() {
        if (pressed) {
            pressed = false;
            internalOnRelease();
        }
    }

    public RippleDrawable(int color) {
        this.color = color;
        this.alpha = color >> 24;
    }

    public void onPress() {
        pressed = true;
        from = 10;
        Rect bounds = getBounds();
        if (style == Style.Borderless) {
            to = (float) (Math.sqrt((float) bounds.width() * bounds.width() + bounds.height() * bounds.height()) / 2.0f);
        } else {
            to = Math.max(bounds.width(), bounds.height()) / 2.0f;
        }
        interpolator = new DecelerateInterpolator();
        downTime = System.currentTimeMillis();
        if (!useHotspot) {
            hotspot = new PointF(bounds.centerX(), bounds.centerY());
        }
        paint.setAntiAlias(Carbon.antiAlias);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        duration = LONGPRESS_DURATION;
        invalidateSelf();
    }

    private void internalOnRelease() {
        long time = System.currentTimeMillis();
        float animFrac = (time - downTime) / (float) duration;
        duration = RIPPLE_DURATION;
        downTime = time - (long) (duration * animFrac);
        upTime = System.currentTimeMillis();
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (background != null) {
            int saveCount = canvas.save(Canvas.CLIP_SAVE_FLAG);
            canvas.clipRect(bounds);
            background.draw(canvas);
            canvas.restoreToCount(saveCount);
        }

        long time = System.currentTimeMillis();

        if (upTime + FADE_DURATION > time) {
            float highlightInterp = interpolator.getInterpolation((time - upTime) / (float) FADE_DURATION);
            paint.setAlpha((int) (alpha * (1 - highlightInterp)));
            if (style == Style.Borderless) {
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), to, paint);
            } else {
                canvas.drawRect(bounds, paint);
            }
            invalidateSelf();
        }

        if (downTime > upTime) {
            paint.setAlpha(alpha);
            if (style == Style.Borderless) {
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), to, paint);
            } else {
                canvas.drawRect(bounds, paint);
            }
            invalidateSelf();
        }

        if (downTime + duration > time) {
            float rippleInterp = interpolator.getInterpolation((time - downTime) / (float) duration);
            float radius = AnimUtils.lerp(rippleInterp, from, to);
            float x = AnimUtils.lerp(rippleInterp, hotspot.x, bounds.centerX());
            float y = AnimUtils.lerp(rippleInterp, hotspot.y, bounds.centerY());

            paint.setAlpha((int) (alpha * (1 - rippleInterp)));
            canvas.drawCircle(x, y, radius, paint);
            invalidateSelf();
        }
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public boolean isHotspotEnabled() {
        return useHotspot;
    }

    public void setHotspotEnabled(boolean useHotspot) {
        this.useHotspot = useHotspot;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        if (background != null)
            background.setBounds(left, top, right, bottom);
    }

    @Override
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
        if (background != null)
            background.setBounds(bounds);
    }

    public Drawable getBackground() {
        return background;
    }

    public void setBackground(Drawable background) {
        this.background = background;
    }

    public PointF getHotspot() {
        return hotspot;
    }

    public void setHotspot(float x, float y) {
        this.hotspot = new PointF(x, y);
    }
}
