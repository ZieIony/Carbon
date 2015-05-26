package carbon.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import carbon.widget.ProgressBar;

/**
 * Created by Mbarin on 2015-02-08.
 */
public class ProgressBarDrawable extends ProgressDrawable {
    private static final long DEFAULT_SWEEP_DURATION = 800;
    private static final long DEFAULT_SWEEP_OFFSET = 500;
    private long sweepDuration = DEFAULT_SWEEP_DURATION;
    private long sweepDelay = DEFAULT_SWEEP_OFFSET;

    Interpolator interpolator = new AccelerateDecelerateInterpolator();

    public ProgressBarDrawable() {
        forePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();

        if (style == ProgressBar.Style.BarIndeterminate) {
            long time = (System.currentTimeMillis() - startTime) % (sweepDuration + sweepDelay);
            float t = (float) (time) / (sweepDuration);
            float t2 = Math.max(0, (float) (time - sweepDelay) / sweepDuration);
            float end = interpolator.getInterpolation(t2);
            //canvas.drawRect(0, 0, bounds.width(), bounds.height(), backPaint);
            canvas.drawRect(end * bounds.width(), 0, t * bounds.width(), bounds.height(), forePaint);
        } else if (style == ProgressBar.Style.BarQuery) {
            long time = (System.currentTimeMillis() - startTime) % (sweepDuration + sweepDelay);
            float t = 1 - (float) (time) / (sweepDuration);
            float t2 = Math.max(0, (float) (time - sweepDelay) / sweepDuration);
            float end = 1 - interpolator.getInterpolation(t2);
            //canvas.drawRect(0, 0, bounds.width(), bounds.height(), backPaint);
            canvas.drawRect(t * bounds.width(), 0, end * bounds.width(), bounds.height(), forePaint);
        } else {
            //canvas.drawRect(0, 0, bounds.width(), bounds.height(), backPaint);
            canvas.drawRect(0, 0, progress * bounds.width(), bounds.height(), forePaint);
        }
        invalidateSelf();
    }
}
