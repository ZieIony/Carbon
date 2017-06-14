package carbon.drawable;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import carbon.widget.ProgressBar;

public abstract class ProgressDrawable extends Drawable {
    private static final long DEFAULT_SWEEP_DURATION = 800;
    private static final long DEFAULT_SWEEP_OFFSET = 500;
    private long sweepDuration = DEFAULT_SWEEP_DURATION;
    private long sweepDelay = DEFAULT_SWEEP_OFFSET;
    final long startTime = System.currentTimeMillis();

    Paint forePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ColorStateList tint = ColorStateList.valueOf(Color.RED);
    private PorterDuff.Mode tintMode;

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
    public boolean setState(@NonNull int[] stateSet) {
        return super.setState(stateSet);
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

    public float getBarPadding() {
        return barPadding;
    }

    public void setBarPadding(float barPadding) {
        this.barPadding = barPadding;
    }

    public void setTint(ColorStateList list) {
        tint = list;
        updateTint();
    }

    @Override
    public void setTint(int tintColor) {
        this.tint = ColorStateList.valueOf(tintColor);
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode tintMode) {
        this.tintMode = tintMode;
        updateTint();
    }

    private void updateTint() {
        if (tint != null && tintMode != null) {
            int color = tint.getColorForState(getState(), tint.getDefaultColor());
            setColorFilter(new PorterDuffColorFilter(color, tintMode));
            setAlpha(Color.alpha(color));
        } else {
            setColorFilter(null);
            setAlpha(255);
        }
    }
}
