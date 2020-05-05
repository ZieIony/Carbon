package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import carbon.R;
import carbon.drawable.CircularProgressDrawable;
import carbon.drawable.ProgressBarDrawable;
import carbon.drawable.ProgressDrawable;
import carbon.view.View;

public class ProgressView extends View {
    private ProgressDrawable drawable;

    public enum Style {
        BarDeterminate, BarIndeterminate, BarQuery, CircularDeterminate, CircularIndeterminate
    }

    public ProgressView(Context context) {
        super(context);
        initProgressBar(null, R.attr.carbon_progressViewStyle, R.style.carbon_ProgressView);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_progressViewStyle);
        initProgressBar(attrs, R.attr.carbon_progressViewStyle, R.style.carbon_ProgressView);
    }

    public ProgressView(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProgressBar(attrs, defStyleAttr, R.style.carbon_ProgressView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressView(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initProgressBar(attrs, defStyleAttr, defStyleRes);
    }

    private void initProgressBar(AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView, defStyleAttr, defStyleRes);
        Style style = Style.values()[a.getInt(R.styleable.ProgressView_carbon_progressStyle, 0)];
        if (style == Style.BarDeterminate || style == Style.BarIndeterminate || style == Style.BarQuery) {
            setDrawable(new ProgressBarDrawable());
        } else {
            setDrawable(new CircularProgressDrawable());
        }
        updateTint();
        drawable.setStyle(style);

        drawable.setBarWidth(a.getDimension(R.styleable.ProgressView_carbon_barWidth, 5));

        setProgress(a.getFloat(R.styleable.ProgressView_carbon_progress, 0.0f));

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
    protected void onMeasureInternal(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = getSuggestedMinimumWidth();
            if (widthMode == MeasureSpec.AT_MOST)
                width = Math.min(widthSize, width);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getSuggestedMinimumHeight();
            if (heightMode == MeasureSpec.AT_MOST)
                height = Math.min(height, heightSize);
        }

        setMeasuredDimension(width, height);
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
