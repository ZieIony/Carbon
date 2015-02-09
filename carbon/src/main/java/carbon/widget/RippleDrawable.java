package carbon.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
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
    private PointF touch;
    private int color;
    private Drawable background;
    private Interpolator interpolator;
    private float from, to;
    private boolean pressed;

    public void onPress(MotionEvent motionEvent) {
        pressed = true;
        onPress(motionEvent.getX(), motionEvent.getY());
    }

    public void onRelease(MotionEvent motionEvent) {
        if (pressed) {
            pressed = false;
            RippleDrawable.this.onRelease();
        }
    }

    public void onCancel(MotionEvent motionEvent) {
        if (pressed) {
            pressed = false;
            RippleDrawable.this.onRelease();
        }
    }

    public RippleDrawable(int color, Drawable background) {
        this.color = color;
        this.alpha = color>>24;
        this.background = background;
    }

    private void onPress(float x, float y) {
        from = 10;
        to = Math.max(getBounds().width() / 2, getBounds().height() / 2);
        interpolator = new DecelerateInterpolator();
        downTime = System.currentTimeMillis();
        touch = new PointF(x, y);
        paint.setAntiAlias(Carbon.antiAlias);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        duration = LONGPRESS_DURATION;
        invalidateSelf();
    }

    private void onRelease() {
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
            background.setBounds(bounds);
            background.draw(canvas);
        }

        long time = System.currentTimeMillis();

        if (upTime + FADE_DURATION > time) {
            float highlightInterp = interpolator.getInterpolation((time - upTime) / (float) FADE_DURATION);
            paint.setAlpha((int) (alpha * (1 - highlightInterp)));
            canvas.drawRect(bounds, paint);
            invalidateSelf();
        }

        if (downTime > upTime) {
            paint.setAlpha(alpha);
            canvas.drawRect(bounds, paint);
            invalidateSelf();
        }

        if (downTime + duration > time) {
            float rippleInterp = interpolator.getInterpolation((time - downTime) / (float) duration);
            float radius = AnimUtils.lerp(rippleInterp, from, to);
            float x = AnimUtils.lerp(rippleInterp, touch.x, bounds.centerX());
            float y = AnimUtils.lerp(rippleInterp, touch.y, bounds.centerY());

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
    public int getOpacity() {
        return 0;
    }
}
