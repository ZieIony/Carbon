package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import carbon.internal.SeekBarPopup;

public class RangeSeekBar extends View {
    private static float THUMB_RADIUS, THUMB_RADIUS_DRAGGED, STROKE_WIDTH;
    float value = 0.3f, value2 = 0.7f;  // value < value2
    float min = 0, max = 1, step = 1;
    float thumbRadius, thumbRadius2;
    int tickStep = 1;
    boolean tick = true;
    int tickColor = 0;
    boolean showLabel;
    String labelFormat;

    SeekBarPopup popup;

    OnValueChangedListener onValueChangedListener;

    int draggedThumb = -1;

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private int colorControl;

    private Style style;

    DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private ValueAnimator radiusAnimator, valueAnimator;

    public enum Style {
        Continuous, Discrete
    }

    public interface OnValueChangedListener {
        void onValueChanged(RangeSeekBar seekBar, float value, float value2);
    }

    public RangeSeekBar(Context context) {
        super(context, null, android.R.attr.seekBarStyle);
        initSeekBar(null, android.R.attr.seekBarStyle);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs, android.R.attr.seekBarStyle);
        initSeekBar(attrs, android.R.attr.seekBarStyle);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSeekBar(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RangeSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSeekBar(attrs, defStyleAttr);
    }

    private void initSeekBar(AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode())
            return;

        colorControl = Carbon.getThemeColor(getContext(), R.attr.colorControlNormal);

        thumbRadius = thumbRadius2 = THUMB_RADIUS = Carbon.getDip(getContext()) * 8;
        THUMB_RADIUS_DRAGGED = Carbon.getDip(getContext()) * 10;
        STROKE_WIDTH = Carbon.getDip(getContext()) * 2;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SeekBar, defStyleAttr, R.style.carbon_SeekBar);

        setStyle(Style.values()[a.getInt(R.styleable.SeekBar_carbon_barStyle, 0)]);
        setMin(a.getFloat(R.styleable.SeekBar_carbon_min, 0));
        setMax(a.getFloat(R.styleable.SeekBar_carbon_max, 0));
        setStepSize(a.getFloat(R.styleable.SeekBar_carbon_stepSize, 0));
        setValue(a.getFloat(R.styleable.SeekBar_carbon_value, 0));
        setValue2(a.getFloat(R.styleable.SeekBar_carbon_value2, 0));
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
        float v2 = (value2 - min) / (max - min);
        int thumbX = (int) (v * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
        int thumbY = getHeight() / 2;
        int thumbX2 = (int) (v2 * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());

        paint.setStrokeWidth(STROKE_WIDTH);

        paint.setColor(colorControl);
        if (getPaddingLeft() + thumbRadius < thumbX - thumbRadius)
            canvas.drawLine(getPaddingLeft(), thumbY, thumbX - thumbRadius, thumbY, paint);
        if (thumbX2 + thumbRadius2 < getWidth() - getPaddingLeft() - thumbRadius2)
            canvas.drawLine(thumbX2 + thumbRadius2, thumbY, getWidth() - getPaddingLeft(), thumbY, paint);

        if (!isInEditMode())
            paint.setColor(tint != null ? tint.getColorForState(getDrawableState(), tint.getDefaultColor()) : Color.WHITE);
        if (thumbX + thumbRadius2 < thumbX2 - thumbRadius)
            canvas.drawLine(thumbX + thumbRadius, thumbY, thumbX2 - thumbRadius2, thumbY, paint);

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
        canvas.drawCircle(thumbX2, thumbY, thumbRadius2, paint);

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
        this.value = Math.max(min, Math.min(value, max));
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
        this.value = Math.max(min, Math.min(value, max));
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
            this.value = stepValue(Math.max(min, Math.min(value, max)));
        } else {
            this.value = Math.max(min, Math.min(value, max));
        }
    }

    public float getValue2() {
        if (style == Style.Discrete)
            return stepValue(value2);
        return value2;
    }

    public void setValue2(float value2) {
        if (style == Style.Discrete) {
            this.value2 = stepValue(Math.max(min, Math.min(value2, max)));
        } else {
            this.value2 = Math.max(min, Math.min(value2, max));
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
        float v = (value - min) / (max - min);
        float v2 = (value2 - min) / (max - min);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int thumbX = (int) (v * (getWidth() - getPaddingLeft() - getPaddingRight() - thumbRadius * 2) + getPaddingLeft() + thumbRadius);
            int thumbX2 = (int) (v2 * (getWidth() - getPaddingLeft() - getPaddingRight() - thumbRadius2 * 2) + getPaddingLeft() + thumbRadius2);
            if (Math.abs(event.getX() - thumbX) < Math.abs(event.getX() - thumbX2)) {
                draggedThumb = 1;
                if (radiusAnimator != null)
                    radiusAnimator.end();
                radiusAnimator = ValueAnimator.ofFloat(thumbRadius, THUMB_RADIUS_DRAGGED);
                radiusAnimator.setDuration(200);
                radiusAnimator.setInterpolator(interpolator);
                radiusAnimator.addUpdateListener(animation -> {
                    thumbRadius = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                radiusAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        radiusAnimator = null;
                    }
                });
                radiusAnimator.start();
            } else {
                draggedThumb = 2;
                if (radiusAnimator != null)
                    radiusAnimator.end();
                radiusAnimator = ValueAnimator.ofFloat(thumbRadius2, THUMB_RADIUS_DRAGGED);
                radiusAnimator.setDuration(200);
                radiusAnimator.setInterpolator(interpolator);
                radiusAnimator.addUpdateListener(animation -> {
                    thumbRadius2 = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                radiusAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        radiusAnimator = null;
                    }
                });
                radiusAnimator.start();
            }
            ViewParent parent = getParent();
            if (parent != null)
                parent.requestDisallowInterceptTouchEvent(true);
            if (showLabel)
                popup.show(this);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            if (draggedThumb == 1) {
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
                radiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        thumbRadius = (float) animation.getAnimatedValue();
                        postInvalidate();
                    }
                });
                radiusAnimator.start();
            } else {
                if (style == Style.Discrete) {
                    float val2 = (float) Math.floor((value2 - min + step / 2) / step) * step + min;
                    if (valueAnimator != null)
                        valueAnimator.cancel();
                    valueAnimator = ValueAnimator.ofFloat(value2, val2);
                    valueAnimator.setDuration(200);
                    valueAnimator.setInterpolator(interpolator);
                    valueAnimator.addUpdateListener(animation -> {
                        value2 = (float) animation.getAnimatedValue();
                        int thumbX = (int) ((value2 - min) / (max - min) * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
                        int thumbY = getHeight() / 2;
                        int radius = rippleDrawable.getRadius();
                        rippleDrawable.setBounds(thumbX - radius, thumbY - radius, thumbX + radius, thumbY + radius);
                        postInvalidate();
                    });
                    valueAnimator.start();
                }
                if (radiusAnimator != null)
                    radiusAnimator.end();
                radiusAnimator = ValueAnimator.ofFloat(thumbRadius2, THUMB_RADIUS);
                radiusAnimator.setDuration(200);
                radiusAnimator.setInterpolator(interpolator);
                radiusAnimator.addUpdateListener(animation -> {
                    thumbRadius2 = (float) animation.getAnimatedValue();
                    postInvalidate();
                });
                radiusAnimator.start();
            }
            //draggedThumb = -1;
            ViewParent parent = getParent();
            if (parent != null)
                parent.requestDisallowInterceptTouchEvent(false);
            if (showLabel)
                popup.dismiss();
        }

        if (draggedThumb == 1) {
            v = (event.getX() - getPaddingLeft()) / (getWidth() - getPaddingLeft() - getPaddingRight());
            v = Math.max(0, Math.min(v, 1));
        } else if (draggedThumb == 2) {
            v2 = (event.getX() - getPaddingLeft()) / (getWidth() - getPaddingLeft() - getPaddingRight());
            v2 = Math.max(0, Math.min(v2, 1));
        }

        if (v > v2) {
            draggedThumb = 3 - draggedThumb;
            float t = v;
            v = v2;
            v2 = t;
            t = thumbRadius;
            thumbRadius = thumbRadius2;
            thumbRadius2 = t;
        }
        float newValue = v * (max - min) + min;
        float newValue2 = v2 * (max - min) + min;

        int thumbX = 0;
        if (draggedThumb == 1) {
            thumbX = (int) (v * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
        } else if (draggedThumb == 2) {
            thumbX = (int) (v2 * (getWidth() - getPaddingLeft() - getPaddingRight()) + getPaddingLeft());
        }
        int thumbY = getHeight() / 2;
        int radius = rippleDrawable.getRadius();

        if (showLabel && draggedThumb > 0) {
            int[] location = new int[2];
            getLocationInWindow(location);
            popup.setText(String.format(labelFormat, draggedThumb == 1 ? newValue : newValue2));
            popup.update(thumbX + location[0] - popup.getBubbleWidth() / 2, thumbY - radius + location[1] - popup.getHeight());
        }

        if (rippleDrawable != null) {
            rippleDrawable.setHotspot(event.getX(), event.getY());
            rippleDrawable.setBounds(thumbX - radius, thumbY - radius, thumbX + radius, thumbY + radius);
        }

        postInvalidate();
        if ((newValue != value || newValue2 != value2) && onValueChangedListener != null) {
            if (style == Style.Discrete) {
                int sv = stepValue(newValue);
                int sv2 = stepValue(newValue2);
                if (stepValue(value) != sv || stepValue(value2) != sv2)
                    onValueChangedListener.onValueChanged(this, sv, sv2);
            } else {
                onValueChangedListener.onValueChanged(this, newValue, newValue2);
            }
        }
        value = newValue;
        value2 = newValue2;
        super.onTouchEvent(event);
        return true;
    }

}
