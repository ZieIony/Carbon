package pl.zielony.carbon.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Marcin on 2015-02-08.
 */
public class CircularProgressDrawable extends Drawable {
    private static final long DEFAULT_SWEEP_DURATION = 3000;
    private static final long DEFAULT_ANGLE_DURATION = 1000;
    private long sweepDuration = DEFAULT_SWEEP_DURATION;
    private long angleDuration = DEFAULT_ANGLE_DURATION;
    private final long startTime;
    Paint paint = new Paint();
    int arcColor = Color.RED;
    int arcBackground = Color.GRAY;
    float width = 5;
    Interpolator interpolator2 = new DecelerateInterpolator();
    Interpolator interpolator = new AccelerateDecelerateInterpolator();
    float progress;

    boolean indeterminate;

    public CircularProgressDrawable() {
        startTime = System.currentTimeMillis();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        paint.setStrokeWidth(width);
        RectF boundsF = new RectF(bounds);
        boundsF.inset(width / 2, width / 2);
        paint.setXfermode(null);

        if (indeterminate) {
            long time = System.currentTimeMillis() - startTime;
            float t = (float) (time % angleDuration) / angleDuration;
            float t2 = (float) (time % sweepDuration) / sweepDuration;
            float arc = Math.min((t - t2 + 1) % 1, (t2 - t + 1) % 1);
            arc = interpolator.getInterpolation(arc) * 2 * 300 + 30;
            paint.setColor(arcBackground);
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), Math.min(boundsF.centerX(), boundsF.centerY()) - width / 2, paint);
            paint.setColor(arcColor);
            canvas.drawArc(boundsF, (t * 360 - arc / 2 + 360) % 360, arc, false, paint);
        } else {
            long time = System.currentTimeMillis() - startTime;
            float t = Math.min((float) time / angleDuration, 1);
            paint.setColor(arcBackground);
            canvas.drawCircle(bounds.centerX(), bounds.centerY(), Math.min(boundsF.centerX(), boundsF.centerY()) - width / 2, paint);
            paint.setColor(arcColor);
            canvas.drawArc(boundsF, interpolator2.getInterpolation(t) * 360 - 90, progress * 360, false, paint);
        }
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    public void setProgress(float progress) {
        this.progress = Math.max(0, Math.min(progress, 1));
    }

    public float getProgress() {
        return progress;
    }

    public long getSweepDuration() {
        return sweepDuration;
    }

    public void setSweepDuration(long sweepDuration) {
        this.sweepDuration = sweepDuration;
    }

    public long getAngleDuration() {
        return angleDuration;
    }

    public void setAngleDuration(long angleDuration) {
        this.angleDuration = angleDuration;
    }

    public int getArcColor() {
        return arcColor;
    }

    public void setArcColor(int color) {
        this.arcColor = color;
    }

    public float getWidth() {
        return width;
    }

    public void setArcWidth(float width) {
        this.width = width;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public int getArcBackground() {
        return arcBackground;
    }

    public void setArcBackground(int arcBackground) {
        this.arcBackground = arcBackground;
    }

}
