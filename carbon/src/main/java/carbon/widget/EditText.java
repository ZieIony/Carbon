package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.DefaultColorStateList;
import carbon.drawable.DefaultNormalColorStateList;
import carbon.drawable.VectorDrawable;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.Roboto;
import carbon.internal.TypefaceUtils;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowShape;
import carbon.shadow.ShadowView;

import static com.nineoldandroids.view.animation.AnimatorProxy.NEEDS_PROXY;
import static com.nineoldandroids.view.animation.AnimatorProxy.wrap;

/**
 * Created by Marcin on 2015-02-14.
 */
public class EditText extends android.widget.EditText implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView, TintedView {

    private Field mIgnoreActionUpEventField;
    private Object editor;

    public enum LabelStyle {
        Floating, Persistent, Hint
    }

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    int DIVIDER_PADDING;
    int cursorColor;

    private Pattern pattern;
    private String errorMessage;
    TextPaint errorPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    boolean afterFirstInteraction = false;

    private int matchingView;
    int minCharacters;
    int maxCharacters = Integer.MAX_VALUE;
    TextPaint counterPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    String label;
    TextPaint labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    LabelStyle labelStyle;

    int internalPaddingTop = 0, internalPaddingBottom = 0;

    private BitmapShader dashPathShader;
    private float labelFrac = 0;
    private boolean underline = true;
    private boolean valid = true;
    boolean required = false, showPasswordButtonEnabled, clearButtonEnabled;

    Drawable clearButton, showPasswordButton;

    float PADDING_ERROR, PADDING_LABEL;

    OnValidateListener validateListener;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            afterFirstInteraction = true;
            validateInternalEvent();
            try {
                int start = getSelectionStart() - 1;
                char c;
                StringBuilder builder = new StringBuilder();
                while (start >= 0 && Character.isLetterOrDigit(c = s.charAt(start--))) {
                    builder.insert(0, c);
                }
            } catch (Exception e) {
            }
        }
    };

    public EditText(Context context) {
        super(context, null);
        initEditText(null, android.R.attr.editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEditText(attrs, android.R.attr.editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEditText(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initEditText(attrs, defStyleAttr);
    }

    private static int[] rippleIds = new int[]{
            R.styleable.EditText_carbon_rippleColor,
            R.styleable.EditText_carbon_rippleStyle,
            R.styleable.EditText_carbon_rippleHotspot,
            R.styleable.EditText_carbon_rippleRadius
    };
    private static int[] animationIds = new int[]{
            R.styleable.EditText_carbon_inAnimation,
            R.styleable.EditText_carbon_outAnimation
    };
    private static int[] touchMarginIds = new int[]{
            R.styleable.EditText_carbon_touchMargin,
            R.styleable.EditText_carbon_touchMarginLeft,
            R.styleable.EditText_carbon_touchMarginTop,
            R.styleable.EditText_carbon_touchMarginRight,
            R.styleable.EditText_carbon_touchMarginBottom
    };
    private static int[] tintIds = new int[]{
            R.styleable.EditText_carbon_tint,
            R.styleable.EditText_carbon_tintMode,
            R.styleable.EditText_carbon_backgroundTint,
            R.styleable.EditText_carbon_backgroundTintMode,
            R.styleable.EditText_carbon_animateColorChanges
    };

    public void initEditText(AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode())
            return;

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EditText, defStyleAttr, 0);

            int ap = a.getResourceId(R.styleable.EditText_android_textAppearance, -1);
            if (ap != -1)
                setTextAppearanceInternal(ap);

            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.EditText_carbon_textAllCaps) {
                    setAllCaps(a.getBoolean(attr, false));
                } else if (!isInEditMode() && attr == R.styleable.EditText_carbon_fontPath) {
                    String path = a.getString(attr);
                    Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                    setTypeface(typeface);
                } else if (attr == R.styleable.EditText_carbon_fontFamily) {
                    int textStyle = a.getInt(R.styleable.EditText_android_textStyle, 0);
                    Typeface typeface = TypefaceUtils.getTypeface(getContext(), a.getString(attr), textStyle);
                    setTypeface(typeface);
                }
            }

            setCursorColor(a.getColor(R.styleable.EditText_carbon_cursorColor, 0));

            setPattern(a.getString(R.styleable.EditText_carbon_pattern));
            DIVIDER_PADDING = (int) getResources().getDimension(R.dimen.carbon_paddingHalf);

            if (!isInEditMode())
                setError(a.getString(R.styleable.EditText_carbon_errorMessage));
            setMatchingView(a.getResourceId(R.styleable.EditText_carbon_matchingView, 0));
            setMinCharacters(a.getInt(R.styleable.EditText_carbon_minCharacters, 0));
            setMaxCharacters(a.getInt(R.styleable.EditText_carbon_maxCharacters, Integer.MAX_VALUE));
            setLabelStyle(LabelStyle.values()[a.getInt(R.styleable.EditText_carbon_labelStyle, a.getBoolean(R.styleable.EditText_carbon_floatingLabel, false) ? 0 : 2)]);
            setLabel(a.getString(R.styleable.EditText_carbon_label));
            if (labelStyle == LabelStyle.Floating && label == null && getHint() != null)
                label = getHint().toString();
            setUnderline(a.getBoolean(R.styleable.EditText_carbon_underline, true));
            setRequired(a.getBoolean(R.styleable.EditText_carbon_required, false));
            setShowPasswordButtonEnabled(a.getBoolean(R.styleable.EditText_carbon_showPasswordButton, false));
            setClearButtonEnabled(a.getBoolean(R.styleable.EditText_carbon_clearButton, false));

            Carbon.initRippleDrawable(this, a, rippleIds);
            Carbon.initTint(this, a, tintIds);
            Carbon.initElevation(this, a, R.styleable.EditText_carbon_elevation);
            Carbon.initAnimations(this, a, animationIds);
            Carbon.initTouchMargin(this, a, touchMarginIds);
            setCornerRadius((int) a.getDimension(R.styleable.TextView_carbon_cornerRadius, 0));

            a.recycle();
        } else {
            setTint(0);
        }

        if (!isInEditMode()) {
            errorPaint.setTypeface(TypefaceUtils.getTypeface(getContext(), Roboto.Regular));
            errorPaint.setTextSize(getResources().getDimension(R.dimen.carbon_errorTextSize));
            errorPaint.setColor(tint.getColorForState(new int[]{R.attr.carbon_state_invalid}, tint.getDefaultColor()));

            labelPaint.setTypeface(TypefaceUtils.getTypeface(getContext(), Roboto.Regular));
            labelPaint.setTextSize(getResources().getDimension(R.dimen.carbon_labelTextSize));

            counterPaint.setTypeface(TypefaceUtils.getTypeface(getContext(), Roboto.Regular));
            counterPaint.setTextSize(getResources().getDimension(R.dimen.carbon_charCounterTextSize));
        }

        try {
            Field mHighlightPaintField = android.widget.TextView.class.getDeclaredField("mHighlightPaint");
            mHighlightPaintField.setAccessible(true);
            mHighlightPaintField.set(this, new Paint() {
                @Override
                public void setColor(int color) {
                    if (getSelectionStart() == getSelectionEnd()) {
                        super.setColor(cursorColor);
                    } else {
                        super.setColor(color);
                    }
                }
            });
        } catch (Exception e) {

        }

        addTextChangedListener(textWatcher);

        int underlineWidth = getResources().getDimensionPixelSize(R.dimen.carbon_1dip);
        Bitmap dashPathBitmap = Bitmap.createBitmap(underlineWidth * 4, underlineWidth, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(dashPathBitmap);
        paint.setColor(0xffffffff);
        paint.setAlpha(255);
        c.drawCircle(dashPathBitmap.getHeight() / 2.0f, dashPathBitmap.getHeight() / 2.0f, dashPathBitmap.getHeight() / 2.0f, paint);
        dashPathShader = new BitmapShader(dashPathBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        PADDING_ERROR = getResources().getDimension(R.dimen.carbon_paddingHalf);
        PADDING_LABEL = getResources().getDimension(R.dimen.carbon_paddingHalf);

        if (isFocused() && getText().length() > 0)
            labelFrac = 1;

        initSelectionHandle();

        validateInternalEvent();

        if (getElevation() > 0)
            AnimUtils.setupElevationAnimator(stateAnimator, this);
    }

    private void initSelectionHandle() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            try {
                final Field fEditor = android.widget.TextView.class.getDeclaredField("mEditor");
                fEditor.setAccessible(true);
                editor = fEditor.get(this);

                mIgnoreActionUpEventField = editor.getClass().getDeclaredField("mIgnoreActionUpEvent");
                mIgnoreActionUpEventField.setAccessible(true);

                final Field fSelectHandleLeft = editor.getClass().getDeclaredField("mSelectHandleLeft");
                final Field fSelectHandleRight = editor.getClass().getDeclaredField("mSelectHandleRight");
                final Field fSelectHandleCenter = editor.getClass().getDeclaredField("mSelectHandleCenter");

                fSelectHandleLeft.setAccessible(true);
                fSelectHandleRight.setAccessible(true);
                fSelectHandleCenter.setAccessible(true);

                VectorDrawable leftHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_left);
                leftHandle.setTint(Carbon.getThemeColor(getContext(), R.attr.colorAccent));
                fSelectHandleLeft.set(editor, leftHandle);

                VectorDrawable rightHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_right);
                rightHandle.setTint(Carbon.getThemeColor(getContext(), R.attr.colorAccent));
                fSelectHandleRight.set(editor, rightHandle);

                VectorDrawable middleHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_middle);
                middleHandle.setTint(Carbon.getThemeColor(getContext(), R.attr.colorAccent));
                fSelectHandleCenter.set(editor, middleHandle);
            } catch (final Exception ignored) {
            }
        } else {
            try {
                final Field fSelectHandleLeft = android.widget.TextView.class.getDeclaredField("mSelectHandleLeft");
                final Field fSelectHandleRight = android.widget.TextView.class.getDeclaredField("mSelectHandleRight");
                final Field fSelectHandleCenter = android.widget.TextView.class.getDeclaredField("mSelectHandleCenter");

                fSelectHandleLeft.setAccessible(true);
                fSelectHandleRight.setAccessible(true);
                fSelectHandleCenter.setAccessible(true);

                VectorDrawable leftHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_left);
                leftHandle.setTint(Carbon.getThemeColor(getContext(), R.attr.colorAccent));
                fSelectHandleLeft.set(this, leftHandle);

                VectorDrawable rightHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_right);
                rightHandle.setTint(Carbon.getThemeColor(getContext(), R.attr.colorAccent));
                fSelectHandleRight.set(this, rightHandle);

                VectorDrawable middleHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_middle);
                middleHandle.setTint(Carbon.getThemeColor(getContext(), R.attr.colorAccent));
                fSelectHandleCenter.set(this, middleHandle);
            } catch (final Exception ignored) {
            }

        }
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
    }

    public int getCursorColor() {
        return cursorColor;
    }

    public boolean hasUnderline() {
        return underline;
    }

    public void setUnderline(boolean drawUnderline) {
        this.underline = drawUnderline;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isShowPasswordButtonEnabled() {
        return showPasswordButtonEnabled;
    }

    public void setShowPasswordButtonEnabled(boolean b) {
        this.showPasswordButtonEnabled = b;
        if (b) {
            setClearButtonEnabled(false);
            VectorDrawable vectorDrawable = new VectorDrawable(getResources(), R.raw.carbon_clear);
            this.showPasswordButton = (Drawable) Carbon.createRippleDrawable(ColorStateList.valueOf(Carbon.getThemeColor(getContext(), R.attr.carbon_rippleColor)), RippleDrawable.Style.Borderless, this, vectorDrawable, false, 0);
            setCompoundDrawablesWithIntrinsicBounds(null, null, showPasswordButton, null);
        } else {
            this.showPasswordButton = null;
            setCompoundDrawables(null, null, null, null);
        }
    }

    public boolean isClearButtonEnabled() {
        return clearButtonEnabled;
    }

    public void setClearButtonEnabled(boolean b) {
        this.clearButtonEnabled = b;
        if (b) {
            setShowPasswordButtonEnabled(false);
            VectorDrawable vectorDrawable = new VectorDrawable(getResources(), R.raw.carbon_visibility_off);
            clearButton = (Drawable) Carbon.createRippleDrawable(ColorStateList.valueOf(Carbon.getThemeColor(getContext(), R.attr.carbon_rippleColor)), RippleDrawable.Style.Borderless, this, vectorDrawable, false, 0);
            setCompoundDrawablesWithIntrinsicBounds(null, null, clearButton, null);
        } else {
            clearButton = null;
            setCompoundDrawables(null, null, null, null);
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
    }

    public void validate() {
        afterFirstInteraction = true;
        validateInternal();
        postInvalidate();
    }

    private void validateInternal() {
        String s = getText().toString();
        // dictionary suggestions vs s.length()>0
        /*try {
            Field mTextField = getText().getClass().getDeclaredField("mText");
            mTextField.setAccessible(true);
            s = new String((char[])mTextField.get(getText()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
        boolean drawPatternError = false, drawMatchingViewError = false;
        if (pattern != null)
            drawPatternError = afterFirstInteraction && !pattern.matcher(s).matches();
        if (matchingView != 0) {
            View view = getRootView().findViewById(matchingView);
            if (view instanceof TextView) {
                TextView matchingTextView = (TextView) view;
                if (afterFirstInteraction && !matchingTextView.getText().toString().equals(getText().toString()))
                    drawMatchingViewError = true;
            }
        }

        boolean counterError = afterFirstInteraction && (minCharacters > 0 && s.length() < minCharacters || maxCharacters < Integer.MAX_VALUE && s.length() > maxCharacters);

        boolean requiredError = required && s.length() == 0;

        valid = !counterError && !drawMatchingViewError && !drawPatternError && !requiredError;

        refreshDrawableState();

        if (labelStyle == LabelStyle.Floating)
            animateFloatingLabel(isFocused() && s.length() > 0);
    }

    private void validateInternalEvent() {
        validateInternal();
        fireOnValidateEvent();
        postInvalidate();
    }

    public void setOnValidateListener(OnValidateListener listener) {
        this.validateListener = listener;
    }

    private void fireOnValidateEvent() {
        if (validateListener != null)
            validateListener.onValidate(canShowError());
    }

    /**
     * Changes text transformation method to caps.
     *
     * @param allCaps if true, TextView will automatically capitalize all characters
     */
    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        super.setTextColor(animateColorChanges && !(colors instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(colors, textColorAnimatorListener) : colors);
    }

    @Override
    public void setTextAppearance(@NonNull Context context, int resid) {
        super.setTextAppearance(context, resid);
        setTextAppearanceInternal(resid);
    }

    public void setTextAppearance(int resid) {
        super.setTextAppearance(getContext(), resid);
        setTextAppearanceInternal(resid);
    }

    private void setTextAppearanceInternal(int resid) {
        TypedArray appearance = getContext().obtainStyledAttributes(resid, R.styleable.TextAppearance);
        if (appearance != null) {
            for (int i = 0; i < appearance.getIndexCount(); i++) {
                int attr = appearance.getIndex(i);
                if (attr == R.styleable.TextAppearance_carbon_textAllCaps) {
                    setAllCaps(appearance.getBoolean(R.styleable.TextAppearance_carbon_textAllCaps, true));
                } else if (!isInEditMode() && attr == R.styleable.TextAppearance_carbon_fontPath) {
                    String path = appearance.getString(attr);
                    Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                    setTypeface(typeface);
                } else if (attr == R.styleable.TextAppearance_carbon_fontFamily) {
                    int textStyle = appearance.getInt(R.styleable.TextAppearance_android_textStyle, 0);
                    Typeface typeface = TypefaceUtils.getTypeface(getContext(), appearance.getString(attr), textStyle);
                    setTypeface(typeface);
                }
            }
            appearance.recycle();
        }
    }

    @Override
    public void setError(CharSequence text) {
        if (text == null) {
            valid = true;
            errorMessage = null;
        } else {
            errorMessage = text.toString();
            valid = false;
        }
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        this.setError(error);
    }

    public void draw2(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (isInEditMode())
            return;

        CharSequence hint = getHint();
        if (required && hint.charAt(hint.length() - 1) != '*')
            setHint(hint + " *");
        int paddingTop = getPaddingTop() + internalPaddingTop;
        int paddingBottom = getPaddingBottom() + internalPaddingBottom;

        if (isFocused() && isEnabled()) {
            paint.setStrokeWidth(2 * getResources().getDimension(R.dimen.carbon_1dip));
        } else {
            paint.setStrokeWidth(getResources().getDimension(R.dimen.carbon_1dip));
        }
        if (underline) {
            //if (isEnabled()) {
            //paint.setShader(null);
            paint.setColor(backgroundTint.getColorForState(getDrawableState(), backgroundTint.getDefaultColor()));
            canvas.drawLine(getScrollX() + getPaddingLeft(), getHeight() - paddingBottom + DIVIDER_PADDING, getScrollX() + getWidth() - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING, paint);
         /*   } else {
                Matrix matrix = new Matrix();
                matrix.postTranslate(0, getHeight() - paddingBottom + DIVIDER_PADDING - paint.getStrokeWidth() / 2.0f);
                dashPathShader.setLocalMatrix(matrix);
                //paint.setShader(dashPathShader);
                canvas.drawRect(getPaddingLeft(), getHeight() - paddingBottom + DIVIDER_PADDING - paint.getStrokeWidth() / 2.0f,
                        getWidth() - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING + paint.getStrokeWidth() / 2.0f, paint);
            }*/
        }

        if (!valid && errorMessage != null)
            canvas.drawText(errorMessage, getScrollX() + getPaddingLeft(), getHeight() - paddingBottom + DIVIDER_PADDING + PADDING_ERROR + errorPaint.getTextSize(), errorPaint);

        if (label != null) {
            if (labelStyle == LabelStyle.Floating) {
                labelPaint.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
                labelPaint.setAlpha((int) (255 * labelFrac));
                canvas.drawText(label, getScrollX() + getPaddingLeft(), paddingTop + labelPaint.getTextSize() * (1 - labelFrac) - PADDING_LABEL, labelPaint);
                if (required && !valid) {
                    float off = labelPaint.measureText(label + " ");
                    labelPaint.setColor(tint.getColorForState(new int[]{R.attr.carbon_state_invalid}, tint.getDefaultColor()));
                    labelPaint.setAlpha((int) (255 * labelFrac));
                    canvas.drawText("*", getScrollX() + getPaddingLeft() + off, paddingTop + labelPaint.getTextSize() * (1 - labelFrac) - PADDING_LABEL, labelPaint);
                }
            } else if (labelStyle == LabelStyle.Persistent) {
                labelPaint.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
                canvas.drawText(label, getScrollX() + getPaddingLeft(), paddingTop - PADDING_LABEL, labelPaint);
                if (required && !valid) {
                    float off = labelPaint.measureText(label + " ");
                    labelPaint.setColor(tint.getColorForState(new int[]{R.attr.carbon_state_invalid}, tint.getDefaultColor()));
                    labelPaint.setAlpha((int) (255 * labelFrac));
                    canvas.drawText("*", getScrollX() + getPaddingLeft() + off, paddingTop - PADDING_LABEL, labelPaint);
                }
            }
        }

        counterPaint.setColor(tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
        int length = getText().length();
        if (minCharacters > 0 && maxCharacters < Integer.MAX_VALUE) {
            String text = length + " / " + minCharacters + "-" + maxCharacters;
            canvas.drawText(text, getScrollX() + getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING + PADDING_ERROR + counterPaint.getTextSize(), counterPaint);
        } else if (minCharacters > 0) {
            String text = length + " / " + minCharacters + "+";
            canvas.drawText(text, getScrollX() + getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING + PADDING_ERROR + counterPaint.getTextSize(), counterPaint);
        } else if (maxCharacters < Integer.MAX_VALUE) {
            String text = length + " / " + maxCharacters;
            canvas.drawText(text, getScrollX() + getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING + PADDING_ERROR + counterPaint.getTextSize(), counterPaint);
        }

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    @Deprecated
    public boolean isFloatingLabelEnabled() {
        return labelStyle == LabelStyle.Floating;
    }

    @Deprecated
    public void setFloatingLabelEnabled(boolean showFloatingLabel) {
        this.labelStyle = showFloatingLabel ? LabelStyle.Floating : LabelStyle.Hint;
    }

    private boolean canShowError() {
        return (pattern != null || matchingView != 0 || !valid) && errorMessage != null || minCharacters > 0 || maxCharacters < Integer.MAX_VALUE;
    }

    public boolean isValid() {
        return valid;
    }

    public String getPattern() {
        return pattern.pattern();
    }

    public void setPattern(final String pattern) {
        if (pattern == null) {
            this.pattern = null;
        } else {
            this.pattern = Pattern.compile(pattern);
        }
    }

    public void setMatchingView(int viewId) {
        this.matchingView = viewId;
    }

    public int getMinCharacters() {
        return minCharacters;
    }

    public void setMinCharacters(int minCharacters) {
        this.minCharacters = minCharacters;
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    private void animateFloatingLabel(boolean visible) {
        ValueAnimator animator;
        if (visible) {
            animator = ValueAnimator.ofFloat(labelFrac, 1);
            animator.setDuration((long) ((1 - labelFrac) * 200));
        } else {
            animator = ValueAnimator.ofFloat(labelFrac, 0);
            animator.setDuration((long) (labelFrac * 200));
        }
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                labelFrac = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (labelStyle == LabelStyle.Floating)
            animateFloatingLabel(focused && getText().length() > 0);
        if (!focused) {
            afterFirstInteraction = true;
            validateInternalEvent();
        }
    }


    // -------------------------------
    // corners
    // -------------------------------

    private int cornerRadius;
    private Path cornersMask;
    private static PorterDuffXfermode pdMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

    /**
     * Gets the corner radius. If corner radius is equal to 0, rounded corners are turned off. Shadows work faster when corner radius is less than 2.5dp.
     *
     * @return corner radius, equal to or greater than 0.
     */
    public int getCornerRadius() {
        return cornerRadius;
    }

    /**
     * Sets the corner radius. If corner radius is equal to 0, rounded corners are turned off. Shadows work faster when corner radius is less than 2.5dp.
     *
     * @param cornerRadius
     */
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidateShadow();
        initCorners();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        invalidateShadow();

        if (getWidth() == 0 || getHeight() == 0)
            return;

        initCorners();

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    private void initCorners() {
        if (cornerRadius > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setClipToOutline(true);
                setOutlineProvider(ShadowShape.viewOutlineProvider);
            } else {
                cornersMask = new Path();
                cornersMask.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (cornerRadius > 0 && getWidth() > 0 && getHeight() > 0 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            draw2(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);

            paint.setXfermode(pdMode);
            canvas.drawPath(cornersMask, paint);

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            draw2(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        if (labelStyle != LabelStyle.Hint)
            internalPaddingTop = (int) (PADDING_LABEL + labelPaint.getTextSize());
        if (canShowError()) {
            internalPaddingBottom = (int) (errorPaint.getTextSize());
            if (!underline)
                internalPaddingBottom += PADDING_ERROR;
        }
        setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public int getPaddingBottom() {
        return super.getPaddingBottom() - internalPaddingBottom;
    }

    @Override
    public int getPaddingTop() {
        return super.getPaddingTop() - internalPaddingTop;
    }

    int getInternalPaddingTop() {
        return internalPaddingTop;
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top + internalPaddingTop, right, bottom + internalPaddingBottom);
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;
    private Transformation t = new Transformation();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clearButton != null && event.getX() > getWidth() - clearButton.getBounds().width()) {
            clearButton.setState(getDrawableState());
        }
        if (showPasswordButton != null && event.getX() > getWidth() - showPasswordButton.getBounds().width()) {
            showPasswordButton.setState(getDrawableState());
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        Animation a = getAnimation();
        if (a != null) {
            a.getTransformation(event.getEventTime(), t);
            float[] loc = new float[]{event.getX(), event.getY()};
            t.getMatrix().mapPoints(loc);
            event.setLocation(loc[0], loc[1]);
        }
        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            rippleDrawable.setHotspot(event.getX(), event.getY());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    @Override
    public void setRippleDrawable(RippleDrawable newRipple) {
        if (rippleDrawable != null) {
            rippleDrawable.setCallback(null);
            if (rippleDrawable.getStyle() == RippleDrawable.Style.Background)
                super.setBackgroundDrawable(rippleDrawable.getBackground());
        }

        if (newRipple != null) {
            newRipple.setCallback(this);
            newRipple.setBounds(0, 0, getWidth(), getHeight());
            if (newRipple.getStyle() == RippleDrawable.Style.Background)
                super.setBackgroundDrawable((Drawable) newRipple);
        }

        rippleDrawable = newRipple;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || rippleDrawable == who;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate();

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).invalidate();
    }

    @Override
    public void invalidate(@NonNull Rect dirty) {
        super.invalidate(dirty);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate(dirty);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).invalidate(dirty);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate(l, t, r, b);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).invalidate(l, t, r, b);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate();

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).invalidate();
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds) {
        super.postInvalidateDelayed(delayMilliseconds);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate();

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom) {
        super.postInvalidate(left, top, right, bottom);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate(left, top, right, bottom);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidate(left, top, right, bottom);
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (background instanceof RippleDrawable) {
            setRippleDrawable((RippleDrawable) background);
            return;
        }

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Background) {
            rippleDrawable.setCallback(null);
            rippleDrawable = null;
        }
        super.setBackgroundDrawable(background);
        updateTint();
    }


    // -------------------------------
    // elevation
    // -------------------------------

    private float elevation = 0;
    private float translationZ = 0;
    private Shadow shadow;

    @Override
    public float getElevation() {
        return elevation;
    }

    @Override
    public synchronized void setElevation(float elevation) {
        if (elevation == this.elevation)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.setElevation(elevation);
        this.elevation = elevation;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    public synchronized void setTranslationZ(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.setTranslationZ(translationZ);
        this.translationZ = translationZ;
        if (getParent() != null)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public ShadowShape getShadowShape() {
        if (cornerRadius == getWidth() / 2 && getWidth() == getHeight())
            return ShadowShape.CIRCLE;
        if (cornerRadius > 0)
            return ShadowShape.ROUND_RECT;
        return ShadowShape.RECT;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public Shadow getShadow() {
        float elevation = getElevation() + getTranslationZ();
        if (elevation >= 0.01f && getWidth() > 0 && getHeight() > 0) {
            if (shadow == null || shadow.elevation != elevation)
                shadow = ShadowGenerator.generateShadow(this, elevation);
            return shadow;
        }
        return null;
    }

    @Override
    public void invalidateShadow() {
        shadow = null;
        if (getParent() != null && getParent() instanceof View)
            ((View) getParent()).postInvalidate();
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin;

    @Override
    public void setTouchMargin(int left, int top, int right, int bottom) {
        touchMargin = new Rect(left, top, right, bottom);
    }

    @Override
    public void setTouchMarginLeft(int margin) {
        touchMargin.left = margin;
    }

    @Override
    public void setTouchMarginTop(int margin) {
        touchMargin.top = margin;
    }

    @Override
    public void setTouchMarginRight(int margin) {
        touchMargin.right = margin;
    }

    @Override
    public void setTouchMarginBottom(int margin) {
        touchMargin.bottom = margin;
    }

    @Override
    public Rect getTouchMargin() {
        return touchMargin;
    }

    public void getHitRect(@NonNull Rect outRect) {
        if (touchMargin == null) {
            super.getHitRect(outRect);
            return;
        }
        outRect.set(getLeft() - touchMargin.left, getTop() - touchMargin.top, getRight() + touchMargin.right, getBottom() + touchMargin.bottom);
    }


    // -------------------------------
    // state animators
    // -------------------------------

    private StateAnimator stateAnimator = new StateAnimator(this);

    @Override
    public StateAnimator getStateAnimator() {
        return stateAnimator;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (rippleDrawable != null && rippleDrawable.getStyle() != RippleDrawable.Style.Background)
            rippleDrawable.setState(getDrawableState());
        if (stateAnimator != null)
            stateAnimator.setState(getDrawableState());
        ColorStateList textColors = getTextColors();
        if (textColors instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) textColors).setState(getDrawableState());
        if (tint != null && tint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) tint).setState(getDrawableState());
        if (backgroundTint != null && backgroundTint instanceof AnimatedColorStateList)
            ((AnimatedColorStateList) backgroundTint).setState(getDrawableState());
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        drawableState[drawableState.length - 1] = valid ? -R.attr.carbon_state_invalid : R.attr.carbon_state_invalid;
        return drawableState;
    }

    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim = AnimUtils.Style.None, outAnim = AnimUtils.Style.None;
    private Animator animator;

    public void setVisibility(final int visibility) {
        if (visibility == View.VISIBLE && (getVisibility() != View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (inAnim != AnimUtils.Style.None) {
                animator = AnimUtils.animateIn(this, inAnim, new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        animator = null;
                        clearAnimation();
                    }
                });
            }
            super.setVisibility(visibility);
        } else if (visibility != View.VISIBLE && (getVisibility() == View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (outAnim == AnimUtils.Style.None) {
                super.setVisibility(visibility);
                return;
            }
            animator = AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator a) {
                    if (((ValueAnimator) a).getAnimatedFraction() == 1)
                        EditText.super.setVisibility(visibility);
                    animator = null;
                    clearAnimation();
                }
            });
        }
    }

    public void setVisibilityImmediate(final int visibility) {
        super.setVisibility(visibility);
    }

    public Animator getAnimator() {
        return animator;
    }

    public AnimUtils.Style getOutAnimation() {
        return outAnim;
    }

    public void setOutAnimation(AnimUtils.Style outAnim) {
        this.outAnim = outAnim;
    }

    public AnimUtils.Style getInAnimation() {
        return inAnim;
    }

    public void setInAnimation(AnimUtils.Style inAnim) {
        this.inAnim = inAnim;
    }


    // -------------------------------
    // tint
    // -------------------------------

    ColorStateList tint;
    PorterDuff.Mode tintMode;
    ColorStateList backgroundTint;
    PorterDuff.Mode backgroundTintMode;
    boolean animateColorChanges;
    ValueAnimator.AnimatorUpdateListener tintAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            updateTint();
            ViewCompat.postInvalidateOnAnimation(EditText.this);
        }
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            updateBackgroundTint();
            ViewCompat.postInvalidateOnAnimation(EditText.this);
        }
    };
    ValueAnimator.AnimatorUpdateListener textColorAnimatorListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            setHintTextColor(getHintTextColors());
        }
    };

    @Override
    public void setTint(ColorStateList list) {
        this.tint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        if (color == 0) {
            setTint(new DefaultNormalColorStateList(getContext()));
        } else {
            setTint(ColorStateList.valueOf(color));
        }
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

    private void updateTint() {
        Drawable[] drawables = getCompoundDrawables();
        if (tint != null && tintMode != null) {
            int color = tint.getColorForState(getDrawableState(), tint.getDefaultColor());
            for (Drawable d : drawables)
                if (d != null)
                    d.setColorFilter(new PorterDuffColorFilter(color, tintMode));
        } else {
            for (Drawable d : drawables)
                if (d != null)
                    d.setColorFilter(null);
        }
    }

    @Override
    public void setTintMode(@NonNull PorterDuff.Mode mode) {
        this.tintMode = mode;
        updateTint();
    }

    @Override
    public PorterDuff.Mode getTintMode() {
        return tintMode;
    }

    @Override
    public void setBackgroundTint(ColorStateList list) {
        this.backgroundTint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, backgroundTintAnimatorListener) : list;
        updateBackgroundTint();
    }

    @Override
    public void setBackgroundTint(int color) {
        if (color == 0) {
            setBackgroundTint(new DefaultColorStateList(getContext()));
        } else {
            setBackgroundTint(ColorStateList.valueOf(color));
        }
    }

    @Override
    public ColorStateList getBackgroundTint() {
        return backgroundTint;
    }

    private void updateBackgroundTint() {
        if (getBackground() == null)
            return;
        if (backgroundTint != null && backgroundTintMode != null) {
            int color = backgroundTint.getColorForState(getDrawableState(), backgroundTint.getDefaultColor());
            getBackground().setColorFilter(new PorterDuffColorFilter(color, backgroundTintMode));
        } else {
            getBackground().setColorFilter(null);
        }
    }

    @Override
    public void setBackgroundTintMode(@Nullable PorterDuff.Mode mode) {
        this.backgroundTintMode = mode;
        updateBackgroundTint();
    }

    @Override
    public PorterDuff.Mode getBackgroundTintMode() {
        return backgroundTintMode;
    }

    public boolean isAnimateColorChangesEnabled() {
        return animateColorChanges;
    }

    public void setAnimateColorChangesEnabled(boolean animateColorChanges) {
        this.animateColorChanges = animateColorChanges;
        if (tint != null && !(tint instanceof AnimatedColorStateList))
            setTint(AnimatedColorStateList.fromList(tint, tintAnimatorListener));
        if (backgroundTint != null && !(backgroundTint instanceof AnimatedColorStateList))
            setBackgroundTint(AnimatedColorStateList.fromList(backgroundTint, backgroundTintAnimatorListener));
        if (!(getTextColors() instanceof AnimatedColorStateList))
            setTextColor(AnimatedColorStateList.fromList(getTextColors(), textColorAnimatorListener));
    }


    // -------------------------------
    // transformations
    // -------------------------------

    public float getAlpha() {
        return NEEDS_PROXY ? wrap(this).getAlpha() : super.getAlpha();
    }

    public void setAlpha(float alpha) {
        if (NEEDS_PROXY) {
            wrap(this).setAlpha(alpha);
        } else {
            super.setAlpha(alpha);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getPivotX() {
        return NEEDS_PROXY ? wrap(this).getPivotX() : super.getPivotX();
    }

    public void setPivotX(float pivotX) {
        if (NEEDS_PROXY) {
            wrap(this).setPivotX(pivotX);
        } else {
            super.setPivotX(pivotX);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getPivotY() {
        return NEEDS_PROXY ? wrap(this).getPivotY() : super.getPivotY();
    }

    public void setPivotY(float pivotY) {
        if (NEEDS_PROXY) {
            wrap(this).setPivotY(pivotY);
        } else {
            super.setPivotY(pivotY);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getRotation() {
        return NEEDS_PROXY ? wrap(this).getRotation() : super.getRotation();
    }

    public void setRotation(float rotation) {
        if (NEEDS_PROXY) {
            wrap(this).setRotation(rotation);
        } else {
            super.setRotation(rotation);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getRotationX() {
        return NEEDS_PROXY ? wrap(this).getRotationX() : super.getRotationX();
    }

    public void setRotationX(float rotationX) {
        if (NEEDS_PROXY) {
            wrap(this).setRotationX(rotationX);
        } else {
            super.setRotationX(rotationX);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getRotationY() {
        return NEEDS_PROXY ? wrap(this).getRotationY() : super.getRotationY();
    }

    public void setRotationY(float rotationY) {
        if (NEEDS_PROXY) {
            wrap(this).setRotationY(rotationY);
        } else {
            super.setRotationY(rotationY);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getScaleX() {
        return NEEDS_PROXY ? wrap(this).getScaleX() : super.getScaleX();
    }

    public void setScaleX(float scaleX) {
        if (NEEDS_PROXY) {
            wrap(this).setScaleX(scaleX);
        } else {
            super.setScaleX(scaleX);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getScaleY() {
        return NEEDS_PROXY ? wrap(this).getScaleY() : super.getScaleY();
    }

    public void setScaleY(float scaleY) {
        if (NEEDS_PROXY) {
            wrap(this).setScaleY(scaleY);
        } else {
            super.setScaleY(scaleY);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getTranslationX() {
        return NEEDS_PROXY ? wrap(this).getTranslationX() : super.getTranslationX();
    }

    public void setTranslationX(float translationX) {
        if (NEEDS_PROXY) {
            wrap(this).setTranslationX(translationX);
        } else {
            super.setTranslationX(translationX);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getTranslationY() {
        return NEEDS_PROXY ? wrap(this).getTranslationY() : super.getTranslationY();
    }

    public void setTranslationY(float translationY) {
        if (NEEDS_PROXY) {
            wrap(this).setTranslationY(translationY);
        } else {
            super.setTranslationY(translationY);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }

    public float getX() {
        return NEEDS_PROXY ? wrap(this).getX() : super.getX();
    }

    public void setX(float x) {
        if (NEEDS_PROXY) {
            wrap(this).setX(x);
        } else {
            super.setX(x);
        }
    }

    public float getY() {
        return NEEDS_PROXY ? wrap(this).getY() : super.getY();
    }

    public void setY(float y) {
        if (NEEDS_PROXY) {
            wrap(this).setY(y);
        } else {
            super.setY(y);
        }
        if (elevation + translationZ > 0 && getParent() != null && getParent() instanceof View)
            ((View) getParent()).invalidate();
    }
}
