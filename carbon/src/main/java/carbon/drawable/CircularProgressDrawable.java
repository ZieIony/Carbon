package carbon.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.core.graphics.drawable.TintAwareDrawable;

import carbon.widget.ProgressView;

public class CircularProgressDrawable extends ProgressDrawable implements TintAwareDrawable {
    private static final long DEFAULT_SWEEP_DURATION = 3000;
    private static final long DEFAULT_ANGLE_DURATION = 1000;
    private long sweepDuration = DEFAULT_SWEEP_DURATION;
    private long angleDuration = DEFAULT_ANGLE_DURATION;
    Interpolator interpolator2 = new DecelerateInterpolator();
    Interpolator interpolator = new AccelerateDecelerateInterpolator();

    public CircularProgressDrawable() {
        forePaint.setStyle(Paint.Style.STROKE);
        forePaint.setColor(Color.WHITE);
    }

    @Override
    public int getMinimumWidth() {
        return (int) getBarWidth() * 2;
    }

    @Override
    public int getMinimumHeight() {
        return (int) getBarWidth() * 2;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        forePaint.setStrokeWidth(width);
        RectF boundsF = new RectF(bounds);
        boundsF.inset(width / 2 + barPadding + 0.1f, width / 2 + barPadding + 0.1f);

        if (style != ProgressView.Style.CircularDeterminate) {
            long time = System.currentTimeMillis() - startTime;
            float t = (float) (time % angleDuration) / angleDuration;
            float t2 = (float) (time % sweepDuration) / sweepDuration;
            float bar = Math.min((t - t2 + 1) % 1, (t2 - t + 1) % 1);
            bar = interpolator.getInterpolation(bar) * 2 * 300 + 30;

            canvas.drawArc(boundsF, (t * 360 - bar / 2 + 360) % 360, bar, false, forePaint);
        } else {
            long time = System.currentTimeMillis() - startTime;
            float t = Math.min((float) time / angleDuration, 1);

            canvas.drawArc(boundsF, interpolator2.getInterpolation(t) * 360 - 90, progress * 360, false, forePaint);
        }
        invalidateSelf();
    }
}
