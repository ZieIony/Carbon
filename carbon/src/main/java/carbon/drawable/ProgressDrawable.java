package carbon.drawable;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import carbon.widget.ProgressBar;

/**
 * Created by Marcin on 2015-02-28.
 */
public abstract class ProgressDrawable extends Drawable {
    static final long DEFAULT_SWEEP_DURATION = 800;
    static final long DEFAULT_SWEEP_OFFSET = 500;
    long sweepDuration = DEFAULT_SWEEP_DURATION;
    long sweepDelay = DEFAULT_SWEEP_OFFSET;
    final long startTime = System.currentTimeMillis();
    Paint forePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ColorStateList barColor = ColorStateList.valueOf(Color.RED);
    float width = 5;
    float progress;
    float barPadding;

    ProgressBar.Style style;

    @Override
    public void setAlpha(int alpha) {
        forePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        forePaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    @Override
    public boolean setState(int[] stateSet) {
        boolean result = super.setState(stateSet);
        updateColors();
        return result;
    }

    private void updateColors() {
        forePaint.setColor(barColor.getColorForState(getState(), barColor.getDefaultColor()));
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

    public long getSweepDelay() {
        return sweepDelay;
    }

    public void setSweepDelay(long sweepDelay) {
        this.sweepDelay = sweepDelay;
    }

    public float getBarWidth() {
        return width;
    }

    public void setBarWidth(float width) {
        this.width = width;
    }

    public ProgressBar.Style getStyle() {
        return style;
    }

    public void setStyle(ProgressBar.Style style) {
        this.style = style;
    }

    public ColorStateList getBarColor() {
        return barColor;
    }

    public void setBarColor(int color) {
        barColor = ColorStateList.valueOf(color);
        updateColors();
    }

    public void setBarColor(ColorStateList barColor) {
        this.barColor = barColor;
        updateColors();
    }

    public float getBarPadding() {
        return barPadding;
    }

    public void setBarPadding(float barPadding) {
        this.barPadding = barPadding;
    }
}
