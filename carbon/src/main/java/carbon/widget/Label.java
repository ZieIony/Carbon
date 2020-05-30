package carbon.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

import org.jetbrains.annotations.NotNull;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.view.AllCapsTransformationMethod;
import carbon.view.TextAppearanceView;
import carbon.view.View;

public class Label extends View implements TextAppearanceView {

    private CharSequence text = "";
    private ColorStateList textColor;
    private StaticLayout layout;
    private TransformationMethod transformationMethod;
    private int gravity;
    private float spacingMul = 1, spacingAdd = 0;
    ValueAnimator.AnimatorUpdateListener textColorAnimatorListener = animation -> postInvalidate();

    public Label(Context context) {
        super(context, null, R.attr.carbon_labelStyle);
        initLabel(null, R.attr.carbon_labelStyle, R.style.carbon_Label);
    }

    public Label(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_labelStyle);
        initLabel(attrs, R.attr.carbon_labelStyle, R.style.carbon_Label);
    }

    public Label(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLabel(attrs, defStyleAttr, R.style.carbon_Label);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Label(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLabel(attrs, defStyleAttr, defStyleRes);
    }

    private void initLabel(AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Label, defStyleAttr, defStyleRes);

        int ap = a.getResourceId(R.styleable.Label_android_textAppearance, -1);
        if (ap != -1)
            Carbon.setTextAppearance(this, ap, a.hasValue(R.styleable.Label_android_textColor), true);

        if (a.hasValue(R.styleable.Label_android_text))
            setText(a.getString(R.styleable.Label_android_text));
        setAllCaps(a.getBoolean(R.styleable.Label_android_textAllCaps, false));
        setGravity(a.getInt(R.styleable.Label_android_gravity, Gravity.START));
        Carbon.initHtmlText(this, a, R.styleable.Label_carbon_htmlText);

        ColorStateList textColor = Carbon.getDefaultColorStateList(this, a, R.styleable.Label_android_textColor);
        if (textColor != null) {
            setTextColor(textColor);
        } else {
            textColor = a.getColorStateList(R.styleable.Label_android_textColor);
            if (textColor != null)
                setTextColor(textColor);
        }

        a.recycle();
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
        layout = null;
    }

    public int getGravity() {
        return gravity;
    }

    public void setText(@NotNull CharSequence text) {
        this.text = text;
        layout = null;
    }

    @NotNull
    public CharSequence getText() {
        return text;
    }

    public void setTextColor(@NonNull ColorStateList colors) {
        this.textColor = isAnimateColorChangesEnabled() && !(colors instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(colors, textColorAnimatorListener) : colors;
    }

    public void setTextColor(int textColor) {
        this.textColor = ColorStateList.valueOf(textColor);
    }

    public ColorStateList getTextColor() {
        return textColor;
    }

    public void setTextSize(float textSize) {
        paint.setTextSize(textSize);
    }

    @Override
    public void setAllCaps(boolean allCaps) {
        transformationMethod = allCaps ? new AllCapsTransformationMethod(getContext()) : null;
        layout = null;
    }

    public void setLineHeight(int lineHeight) {
        final int fontHeight = getPaint().getFontMetricsInt(null);
        if (lineHeight != fontHeight)
            setLineSpacing(lineHeight - fontHeight, 1f);
    }

    public void setLineSpacing(float add, float mult) {
        if (spacingAdd != add || spacingMul != mult) {
            spacingAdd = add;
            spacingMul = mult;

            if (layout != null) {
                layout = null;
                requestLayout();
                invalidate();
            }
        }
    }

    @Override
    public void setTypeface(@NotNull Typeface typeface, int style) {
        paint.setTypeface(typeface);
    }

    public void setTextAppearance(@NonNull Context context, @StyleRes int resId) {
        Carbon.setTextAppearance(this, resId, false, true);
    }

    public void setTextAppearance(@StyleRes int resId) {
        Carbon.setTextAppearance(this, resId, false, true);
    }

    @NotNull
    @Override
    public TextPaint getPaint() {
        return paint;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(Label.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(Label.class.getName());
        info.setText(text);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (textColor instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) textColor).setState(getDrawableState());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layout = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ensureLayout();

        int saveCount = canvas.save();
        if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.CENTER_VERTICAL) {
            canvas.translate(getPaddingLeft(), (getHeight() - getPaddingTop() - getPaddingBottom() - layout.getHeight()) / 2.0f + getPaddingTop());
        } else if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            canvas.translate(getPaddingLeft(), getHeight() - getPaddingBottom() - layout.getHeight());
        } else {
            canvas.translate(getPaddingLeft(), getPaddingTop());
        }
        if (textColor != null)
            paint.setColor(textColor.getColorForState(getDrawableState(), textColor.getDefaultColor()));
        layout.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    private void ensureLayout() {
        if (layout == null) {
            Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
            if ((GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this)) & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.RIGHT) {
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
            } else if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL) {
                alignment = Layout.Alignment.ALIGN_CENTER;
            }
            CharSequence transformedText = transformationMethod != null ? transformationMethod.getTransformation(text, this) : text;
            layout = new StaticLayout(transformedText, paint, getWidth() - getPaddingLeft() - getPaddingRight(), alignment, spacingMul, spacingAdd, false);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = getPaddingLeft() + getPaddingRight();
        int height = getPaddingTop() + getPaddingBottom();

        CharSequence transformedText = transformationMethod != null ? transformationMethod.getTransformation(text, this) : text;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            layout = new StaticLayout(transformedText, paint, Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            int maxWidth = 0;
            for (int i = 0; i < layout.getLineCount(); i++)
                maxWidth = (int) Math.ceil(Math.max(maxWidth, layout.getLineWidth(i)));
            width += maxWidth;

            width = Math.max(width, getSuggestedMinimumWidth());
            if (widthMode == MeasureSpec.AT_MOST)
                width = Math.min(widthSize, width);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            layout = new StaticLayout(transformedText, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            height += layout.getHeight();
            height = Math.max(height, getSuggestedMinimumHeight());
            if (heightMode == MeasureSpec.AT_MOST)
                height = Math.min(height, heightSize);
        }

        setMeasuredDimension(width, height);
    }

    @Override
    public int getBaseline() {
        ensureLayout();
        int baseline = layout.getLineBaseline(0);
        if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.CENTER_VERTICAL && layout != null) {
            return (int) (baseline + (getHeight() - getPaddingTop() - getPaddingBottom() - layout.getHeight()) / 2.0f + getPaddingTop());
        } else if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM && layout != null) {
            return baseline + getHeight() - getPaddingBottom() - layout.getHeight();
        } else {
            return baseline + getPaddingTop();
        }
    }


}
