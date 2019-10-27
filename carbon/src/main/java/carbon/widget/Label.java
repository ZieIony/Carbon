package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;

import carbon.Carbon;
import carbon.R;
import carbon.internal.AllCapsTransformationMethod;
import carbon.view.TextAppearanceView;
import carbon.view.View;

public class Label extends View implements TextAppearanceView {

    private CharSequence text;
    private ColorStateList textColor;
    private StaticLayout layout;
    private TransformationMethod transformationMethod;

    public Label(Context context) {
        super(context, null, R.attr.carbon_labelStyle);
        initLabel(null, R.attr.carbon_labelStyle, R.style.carbon_Label);
    }

    public Label(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_labelStyle);
        initLabel(attrs, R.attr.carbon_labelStyle, R.style.carbon_Label);
    }

    public Label(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLabel(attrs, defStyleAttr, R.style.carbon_Label);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Label(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLabel(attrs, defStyleAttr, defStyleRes);
    }

    public void initLabel(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            setTextColor(null);
            return;
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Label, defStyleAttr, defStyleRes);

        int ap = a.getResourceId(R.styleable.Label_android_textAppearance, -1);
        if (ap != -1)
            Carbon.setTextAppearance(this, ap, a.hasValue(R.styleable.Label_android_textColor), true);

        setText(a.getString(R.styleable.Label_android_text));
        setAllCaps(a.getBoolean(R.styleable.Label_android_textAllCaps, false));

        Carbon.initDefaultTextColor(this, a, R.styleable.Label_android_textColor);

        a.recycle();
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }

    public void setTextColor(ColorStateList textColor) {
        this.textColor = textColor != null ? textColor : ColorStateList.valueOf(Carbon.getThemeColor(getContext(), android.R.attr.textColorPrimary));
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
    }

    @Override
    public void setTypeface(Typeface typeface, int style) {
        paint.setTypeface(typeface);
    }

    public void setTextAppearance(@NonNull Context context, int resid) {
        Carbon.setTextAppearance(this, resid, false, true);
    }

    public void setTextAppearance(int resid) {
        Carbon.setTextAppearance(this, resid, false, true);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(Label.class.getName());
    }

    /*@Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(CheckBox.class.getName());
        info.setCheckable(true);
        info.setChecked(mChecked);
    }*/

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layout = new StaticLayout(transformationMethod != null ? transformationMethod.getTransformation(text, this) : text, paint, getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getPaddingLeft(), Math.max(getPaddingTop(), (getHeight() - layout.getHeight()) / 2.0f));
        paint.setColor(textColor.getColorForState(getDrawableState(), textColor.getDefaultColor()));
        layout.draw(canvas);
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

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            layout = new StaticLayout(transformationMethod != null ? transformationMethod.getTransformation(text, this) : text, paint, Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            int maxWidth = 0;
            for (int i = 0; i < layout.getLineCount(); i++)
                maxWidth = (int) Math.max(maxWidth, layout.getLineWidth(i));
            width += maxWidth;

            width = Math.max(width, getSuggestedMinimumWidth());
            if (widthMode == MeasureSpec.AT_MOST)
                width = Math.min(widthSize, width);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            layout = new StaticLayout(transformationMethod != null ? transformationMethod.getTransformation(text, this) : text, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            height += layout.getHeight();
            height = Math.max(height, getSuggestedMinimumHeight());
            if (heightMode == MeasureSpec.AT_MOST)
                height = Math.min(height, heightSize);
        }

        setMeasuredDimension(width, height);
    }

}
