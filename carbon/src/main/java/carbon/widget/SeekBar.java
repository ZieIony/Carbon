package carbon.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import carbon.Carbon;
import carbon.R;
import carbon.drawable.ripple.RippleDrawable;
import carbon.internal.MathUtils;
import carbon.internal.SeekBarPopup;

public class SeekBar extends View {
    private static float THUMB_RADIUS, THUMB_RADIUS_DRAGGED, STROKE_WIDTH;
    float value = 0.5f;
    float min = 0, max = 1, step = 1;
    float thumbRadius;
    int tickStep = 1;
    boolean tick = true;
    int tickColor = 0;
    boolean showLabel;
    String labelFormat;

    SeekBarPopup popup;

    OnValueChangedListener onValueChangedListener;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int colorControl;

    private Style style;

    DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private ValueAnimator radiusAnimator, valueAnimator;

    public enum Style {
        Continuous, Discrete
    }

    public interface OnValueChangedListener {
        void onValueChanged(SeekBar seekBar, float value);
    }

    public SeekBar(Context context) {
        super(context, null, android.R.attr.seekBarStyle);
        initSeekBar(null, android.R.attr.seekBarStyle);
    }

    public SeekBar(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.seekBarStyle);
        initSeekBar(attrs, android.R.attr.seekBarStyle);
    }

    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSeekBar(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSeekBar(attrs, defStyleAttr);
    }

    private void initSeekBar(AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode())
            return;

        colorControl = Carbon.getThemeColor(getContext(), R.attr.colorControlNormal);

        thumbRadius = THUMB_RADIUS = Carbon.getDip(getContext()) * 8;
        THUMB_RADIUS_DRAGGED = Carbon.getDip(getContext()) * 10;
        STROKE_WIDTH = Carbon.getDip(getContext()) * 2;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SeekBar, defStyleAttr, R.style.carbon_SeekBar);

        setStyle(Style.values()[a.getInt(R.styleable.SeekBar_carbon_barStyle, 0)]);
        setMin(a.getFloat(R.styleable.SeekBar_carbon_min, 0));
        setMax(a.getFloat(R.styleable.SeekBar_carbon_max, 0));
        setStepSize(a.getFloat(R.styleable.SeekBar_carbon_stepSize, 0));
        setValue(a.getFloat(R.styleable.SeekBar_carbon_value, 0));
        setTick(a.getBoolean(R.styleable.SeekBar_carbon_tick, true));
        setTickStep(a.getInt(R.styleable.SeekBar_carbon_tickStep, 1));
        setTickColor(a.getColor(R.styleable.SeekBar_carbon_tickColor, 0));
        setShowLabel(a.getBoolean(R.styleable.SeekBar_carbon_showLabel, false));
        setLabelFormat(a.getString(R.styleable.SeekBar_carbon_labelFormat));

        a.recycle();

        setFocusableInTouchMode(false); // TODO: from theme
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return Math.max((int) Math.ceil(THUMB_RADIUS_DRAGGED * 2), super.getSuggestedMinimumWidth());
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return Math.max((int) Math.ceil(THUMB_RADIUS_DRAGGED * 2), super.getSuggestedMinimumHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = getSuggestedMinimumWidth();
        int desiredHeight = getSuggestedMinimumHeight();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        float v = (value - min) / (max - min);
        int thumbX = (int) (v * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
        int thumbY = getHeight() / 2;

        paint.setStrokeWidth(STROKE_WIDTH);
        if (!isInEditMode())
            paint.setColor(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE);
        if (getPaddingLeft() < thumbX - thumbRadius)
            canvas.drawLine(getPaddingLeft(), thumbY, thumbX - thumbRadius, thumbY, paint);

        paint.setColor(colorControl);
        if (thumbX + thumbRadius < getWidth() - getPaddingLeft())
            canvas.drawLine(thumbX + thumbRadius, thumbY, getWidth() - getPaddingLeft(), thumbY, paint);

        if (style == Style.Discrete && tick) {
            paint.setColor(tickColor);
            float range = (max - min) / step;
            for (int i = 0; i < range; i += tickStep)
                canvas.drawCircle(i / range * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft(), getHeight() / 2, STROKE_WIDTH / 2, paint);
            canvas.drawCircle(getWidth() - getPaddingRight(), getHeight() / 2, STROKE_WIDTH / 2, paint);
        }

        if (!isInEditMode())
            paint.setColor(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE);
        canvas.drawCircle(thumbX, thumbY, thumbRadius, paint);

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    public void setShowLabel(boolean showLabel) {
        this.showLabel = showLabel;
        if (showLabel)
            popup = new SeekBarPopup(getContext());
    }

    public boolean getShowLabel() {
        return showLabel;
    }

    public void setLabelFormat(String format) {
        labelFormat = format;
    }

    public String getLabelFormat() {
        return labelFormat;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max > min) {
            this.max = max;
        } else {
            this.max = min + step;
        }
        this.value = MathUtils.constrain(value, min, max);
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        if (min < max) {
            this.min = min;
        } else if (this.max > step) {
            this.min = max - step;
        } else {
            this.min = 0;
        }
        this.value = MathUtils.constrain(value, min, max);
    }

    private int stepValue(float v) {
        return (int) (Math.floor((v - min + step / 2) / step) * step + min);
    }

    public float getValue() {
        if (style == Style.Discrete)
            return stepValue(value);
        return value;
    }

    public void setValue(float value) {
        if (style == Style.Discrete) {
            this.value = stepValue(MathUtils.constrain(value, min, max));
        } else {
            this.value = MathUtils.constrain(value, min, max);
        }
    }

    public float getStepSize() {
        return step;
    }

    public void setStepSize(float step) {
        if (step > 0) {
            this.step = step;
        } else {
            this.step = 1;
        }
    }

    public boolean hasTick() {
        return tick;
    }

    public void setTick(boolean tick) {
        this.tick = tick;
    }

    public int getTickStep() {
        return tickStep;
    }

    public void setTickStep(int tickStep) {
        this.tickStep = tickStep;
    }

    public int getTickColor() {
        return tickColor;
    }

    public void setTickColor(int tickColor) {
        this.tickColor = tickColor;
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }


    // -------------------------------
    // ripple
    // -------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (radiusAnimator != null)
                radiusAnimator.end();
            radiusAnimator = ValueAnimator.ofFloat(thumbRadius, THUMB_RADIUS_DRAGGED);
            radiusAnimator.setDuration(200);
            radiusAnimator.setInterpolator(interpolator);
            radiusAnimator.addUpdateListener(animation -> {
                thumbRadius = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            radiusAnimator.start();
            ViewParent parent = getParent();
            if (parent != null)
                parent.requestDisallowInterceptTouchEvent(true);
            if (showLabel)
                popup.show(this);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            if (style == Style.Discrete) {
                float val = (float) Math.floor((value - min + step / 2) / step) * step + min;
                if (valueAnimator != null)
                    valueAnimator.cancel();
                valueAnimator = ValueAnimator.ofFloat(value, val);
                valueAnimator.setDuration(200);
                valueAnimator.setInterpolator(interpolator);
                valueAnimator.addUpdateListener(animation -> {
                    value = (float) animation.getAnimatedValue();
                    int thumbX = (int) ((value - min) / (max - min) * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
                    int thumbY = getHeight() / 2;
                    int radius = rippleDrawable.getRadius();
                    rippleDrawable.setBounds(thumbX - radius, thumbY - radius, thumbX + radius, thumbY + radius);
                    postInvalidate();
                });
                valueAnimator.start();
            }
            if (radiusAnimator != null)
                radiusAnimator.end();
            radiusAnimator = ValueAnimator.ofFloat(thumbRadius, THUMB_RADIUS);
            radiusAnimator.setDuration(200);
            radiusAnimator.setInterpolator(interpolator);
            radiusAnimator.addUpdateListener(animation -> {
                thumbRadius = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            radiusAnimator.start();
            ViewParent parent = getParent();
            if (parent != null)
                parent.requestDisallowInterceptTouchEvent(false);
            if (showLabel)
                popup.dismiss();
        }

        float v = (event.getX() - getPaddingLeft()) / (getWidth() - getPaddingLeft() - getPaddingRight());
        v = Math.max(0, Math.min(v, 1));
        float newValue = v * (max - min) + min;

        int thumbX = (int) (v * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
        int thumbY = getHeight() / 2;
        int radius = rippleDrawable.getRadius();

        if (showLabel) {
            int[] location = new int[2];
            getLocationInWindow(location);
            popup.setText(String.format(labelFormat, newValue));
            popup.update(thumbX + location[0] - popup.getBubbleWidth() / 2, thumbY - radius + location[1] - popup.getHeight());
        }

        if (rippleDrawable != null) {
            rippleDrawable.setHotspot(event.getX(), event.getY());
            rippleDrawable.setBounds(thumbX - radius, thumbY - radius, thumbX + radius, thumbY + radius);
        }

        postInvalidate();
        if (newValue != value && onValueChangedListener != null) {
            if (style == Style.Discrete) {
                int sv = stepValue(newValue);
                if (stepValue(value) != sv)
                    onValueChangedListener.onValueChanged(this, sv);
            } else {
                onValueChangedListener.onValueChanged(this, newValue);
            }
        }
        value = newValue;
        super.onTouchEvent(event);
        return true;
    }
}
