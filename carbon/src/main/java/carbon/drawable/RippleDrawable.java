package carbon.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import carbon.Carbon;
import carbon.animation.AnimUtils;

/**
 * Created by Marcin on 2014-11-19.
 */
public class RippleDrawable extends Drawable {
    private static final class LogInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            return 1 - (float) Math.pow(400, -input * 1.4);
        }
    }

    private final int alpha;
    private long radiusDuration, opacityDuration;
    private long downTime, upTime;
    private final float density;
    private static final int FADEIN_DURATION = 100;


    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator DECEL_INTERPOLATOR = new LogInterpolator();

    private static final float WAVE_TOUCH_DOWN_ACCELERATION = 1024.0f;
    private static final float WAVE_TOUCH_UP_ACCELERATION = 3400.0f;
    private static final float WAVE_OPACITY_DECAY_VELOCITY = 3.0f;

    private static final long RIPPLE_ENTER_DELAY = 80;


    private Paint paint = new Paint();
    private PointF hotspot = new PointF();
    private int color;
    private Drawable background;
    private float from, to;
    private boolean pressed, useHotspot = true;
    private Style style = Style.Background;

    public static enum Style {
        Over, Background, Borderless
    }

    public RippleDrawable(int color, Context context) {
        this.color = color;
        this.alpha = (color >> 24) / 2;
        density = context.getResources().getDisplayMetrics().density;
    }

    public void onPress() {
        pressed = true;
        Rect bounds = getBounds();
        to = (float) (Math.sqrt((float) bounds.width() * bounds.width() + bounds.height() * bounds.height()) / 2.0f);
        downTime = System.currentTimeMillis();
        if (!useHotspot) {
            hotspot.x = bounds.centerX();
            hotspot.y = bounds.centerY();
        }
        paint.setAntiAlias(Carbon.antiAlias);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        radiusDuration = (long) (1000 * Math.sqrt(to / WAVE_TOUCH_DOWN_ACCELERATION * density) + 0.5);
        opacityDuration = FADEIN_DURATION;

        invalidateSelf();
    }

    public void onRelease() {
        if (pressed) {
            pressed = false;
            upTime = System.currentTimeMillis();

            float rippleInterp = LINEAR_INTERPOLATOR.getInterpolation((upTime - downTime) / (float) radiusDuration);
            from = AnimUtils.lerp(rippleInterp, 0, to);
            Rect bounds = getBounds();
            hotspot.x = AnimUtils.lerp(rippleInterp, hotspot.x, bounds.centerX());
            hotspot.y = AnimUtils.lerp(rippleInterp, hotspot.y, bounds.centerY());
            from = AnimUtils.lerp(rippleInterp, 0, to);
            float remaining = Math.max(0, to - from);

            radiusDuration = (int) (1000 * Math.sqrt(remaining / (WAVE_TOUCH_UP_ACCELERATION + WAVE_TOUCH_DOWN_ACCELERATION) * density) + 0.5);
            opacityDuration = (int) (1000.0f / WAVE_OPACITY_DECAY_VELOCITY + 0.5f);

            invalidateSelf();
        }
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

        if (pressed) {
            // bg
            float highlightInterp = Math.min(LINEAR_INTERPOLATOR.getInterpolation((time - downTime) / (float) FADEIN_DURATION), 1);
            paint.setAlpha((int) (alpha * highlightInterp));
            if (style == Style.Borderless) {
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), to, paint);
            } else {
                canvas.drawRect(bounds, paint);
            }

            // ripple
            float rippleInterp = Math.min(LINEAR_INTERPOLATOR.getInterpolation((time - downTime - RIPPLE_ENTER_DELAY) / (float) radiusDuration), 1);
            float radius = to * rippleInterp;
            float x = AnimUtils.lerp(rippleInterp, hotspot.x, bounds.centerX());
            float y = AnimUtils.lerp(rippleInterp, hotspot.y, bounds.centerY());

            canvas.drawCircle(x, y, radius, paint);

            if (highlightInterp < 1 || rippleInterp < 1)
                invalidateSelf();
        }

        if (!pressed) {
            // bg
            float highlightInterp = Math.min(LINEAR_INTERPOLATOR.getInterpolation((time - upTime) / (float) opacityDuration), 1);
            paint.setAlpha((int) (alpha * (1 - highlightInterp)));
            if (style == Style.Borderless) {
                canvas.drawCircle(bounds.centerX(), bounds.centerY(), to, paint);
            } else {
                canvas.drawRect(bounds, paint);
            }

            // ripple
            float rippleInterp = Math.min(DECEL_INTERPOLATOR.getInterpolation((time - upTime) / (float) radiusDuration), 1);
            float radius = AnimUtils.lerp(rippleInterp, from, to);
            float x = AnimUtils.lerp(rippleInterp, hotspot.x, bounds.centerX());
            float y = AnimUtils.lerp(rippleInterp, hotspot.y, bounds.centerY());

            canvas.drawCircle(x, y, radius, paint);

            if (highlightInterp < 1 || rippleInterp < 1)
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
        if (background != null)
            background.jumpToCurrentState();
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
        this.hotspot.x = x;
        this.hotspot.y = y;
    }
}
