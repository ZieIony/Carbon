package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import carbon.Carbon;
import carbon.R;
import carbon.drawable.CircularProgressDrawable;
import carbon.drawable.ProgressBarDrawable;
import carbon.drawable.ProgressDrawable;
import carbon.view.View;

public class ProgressBar extends View {
    private ProgressDrawable drawable;

    public enum Style {
        BarDeterminate, BarIndeterminate, BarQuery, CircularDeterminate, CircularIndeterminate
    }

    public ProgressBar(Context context) {
        super(context);
        initProgressBar(null, android.R.attr.progressBarStyle);
    }

    public ProgressBar(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ProgressBar, android.R.attr.progressBarStyle, R.styleable.ProgressBar_carbon_theme), attrs, android.R.attr.progressBarStyle);
        initProgressBar(attrs, android.R.attr.progressBarStyle);
    }

    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ProgressBar, defStyleAttr, R.styleable.ProgressBar_carbon_theme), attrs, defStyleAttr);
        initProgressBar(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.ProgressBar, defStyleAttr, R.styleable.ProgressBar_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initProgressBar(attrs, defStyleAttr);
    }

    private void initProgressBar(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressBar, defStyleAttr, R.style.carbon_ProgressBar);
        Style style = Style.values()[a.getInt(R.styleable.ProgressBar_carbon_progressStyle, 0)];
        if (style == Style.BarDeterminate || style == Style.BarIndeterminate || style == Style.BarQuery) {
            setDrawable(new ProgressBarDrawable());
        } else {
            setDrawable(new CircularProgressDrawable());
        }
        updateTint();
        drawable.setStyle(style);

        drawable.setBarWidth(a.getDimension(R.styleable.ProgressBar_carbon_barWidth, 5));

        a.recycle();

        if (getVisibility() == VISIBLE) {
            setBarWidth(getBarWidth() + getBarPadding());
            setBarPadding(0);
        } else {
            setBarPadding(getBarWidth() + getBarPadding());
            setBarWidth(0);
        }
    }

    public void setProgress(float progress) {
        drawable.setProgress(progress);
    }

    public float getProgress() {
        return drawable.getProgress();
    }

    public float getBarWidth() {
        return drawable.getBarWidth();
    }

    public void setBarWidth(float arcWidth) {
        drawable.setBarWidth(arcWidth);
    }

    public void setBarPadding(float padding) {
        drawable.setBarPadding(padding);
    }

    public float getBarPadding() {
        return drawable.getBarPadding();
    }

    public void setDrawable(ProgressDrawable newDrawable) {
        this.drawable = newDrawable;

        if (drawable != null)
            drawable.setCallback(null);

        if (newDrawable != null)
            newDrawable.setCallback(this);
    }

    public ProgressDrawable getDrawable() {
        return drawable;
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || who == drawable;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (drawable != null)
            drawable.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        if (drawable != null)
            drawable.setBounds(0, 0, getWidth(), getHeight());
    }

    protected void updateTint() {
        if (tint != null && tintMode != null) {
            int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
            if (drawable != null) {
                drawable.setTint(color);
                drawable.setTintMode(tintMode);
            }
        } else {
            if (drawable != null)
                drawable.setTintList(null);
        }
    }
}
