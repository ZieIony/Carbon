package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.UnderlineDrawable;
import carbon.drawable.VectorDrawable;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.AllCapsTransformationMethod;
import carbon.internal.RevealAnimator;
import carbon.internal.SimpleTextWatcher;
import carbon.internal.TypefaceUtils;
import carbon.shadow.Shadow;
import carbon.shadow.ShadowGenerator;
import carbon.shadow.ShadowShape;
import carbon.shadow.ShadowView;
import carbon.view.AutoSizeTextView;
import carbon.view.InputView;
import carbon.view.RevealView;
import carbon.view.StateAnimatorView;
import carbon.view.TintedView;
import carbon.view.TouchMarginView;
import carbon.view.VisibleView;

@SuppressLint("AppCompatCustomView")
public class EditText extends android.widget.EditText
        implements ShadowView, RippleView, TouchMarginView, StateAnimatorView, AnimatedView, TintedView, InputView, AutoSizeTextView, RevealView, VisibleView {

    private Field mIgnoreActionUpEventField;
    private Object editor;

    boolean required = false;
    private int minCharacters;
    private int maxCharacters = Integer.MAX_VALUE;

    TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    private int cursorColor;

    private Pattern pattern;
    private int matchingView;

    private boolean valid = true;

    private CharSequence prefix, suffix;
    private StaticLayout prefixLayout, suffixLayout;
    private int prefixPadding, prefixTextPadding, suffixPadding, suffixTextPadding;

    private List<OnValidateListener> validateListeners = new ArrayList<>();
    private boolean clearFocusOnTouchOutside = false;

    public EditText(Context context) {
        super(context, null);
        initEditText(null, android.R.attr.editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.EditText, android.R.attr.editTextStyle, R.styleable.EditText_carbon_theme), attrs);
        initEditText(attrs, android.R.attr.editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.EditText, defStyleAttr, R.styleable.EditText_carbon_theme), attrs, defStyleAttr);
        initEditText(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.EditText, defStyleAttr, R.styleable.EditText_carbon_theme), attrs, defStyleAttr, defStyleRes);
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
    private static int[] elevationIds = new int[]{
            R.styleable.EditText_carbon_elevation,
            R.styleable.EditText_carbon_tintMode,
    };
    private static int[] autoSizeTextIds = new int[]{
            R.styleable.EditText_carbon_autoSizeText,
            R.styleable.EditText_carbon_autoSizeMinTextSize,
            R.styleable.EditText_carbon_autoSizeMaxTextSize,
            R.styleable.EditText_carbon_autoSizeStepGranularity
    };

    private void initEditText(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EditText, defStyleAttr, R.style.carbon_EditText);

        int ap = a.getResourceId(R.styleable.EditText_android_textAppearance, -1);
        if (ap != -1)
            setTextAppearanceInternal(ap, a.hasValue(R.styleable.EditText_android_textColor));

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (!isInEditMode() && attr == R.styleable.EditText_carbon_fontPath) {
                String path = a.getString(attr);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                setTypeface(typeface);
            } else if (attr == R.styleable.EditText_carbon_fontFamily) {
                int textStyle = a.getInt(R.styleable.EditText_android_textStyle, 0);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), a.getString(attr), textStyle);
                setTypeface(typeface);
            } else if (attr == R.styleable.EditText_android_textAllCaps) {
                setAllCaps(a.getBoolean(attr, true));
            }
        }

        setCursorColor(a.getColor(R.styleable.EditText_carbon_cursorColor, 0));

        setPattern(a.getString(R.styleable.EditText_carbon_pattern));
        setMinCharacters(a.getInt(R.styleable.EditText_carbon_minCharacters, 0));
        setMaxCharacters(a.getInt(R.styleable.EditText_carbon_maxCharacters, Integer.MAX_VALUE));
        setRequired(a.getBoolean(R.styleable.EditText_carbon_required, false));

        setPrefix(a.getString(R.styleable.EditText_carbon_prefix));
        setSuffix(a.getString(R.styleable.EditText_carbon_suffix));

        setMatchingView(a.getResourceId(R.styleable.EditText_carbon_matchingView, 0));

        Carbon.initDefaultTextColor(this, a, R.styleable.EditText_android_textColor);

        Carbon.initRippleDrawable(this, a, rippleIds);
        Carbon.initTint(this, a, tintIds);
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        setCornerRadius(a.getDimension(R.styleable.EditText_carbon_cornerRadius, 0));
        Carbon.initHtmlText(this, a, R.styleable.EditText_carbon_htmlText);
        Carbon.initAutoSizeText(this, a, autoSizeTextIds);

        TypedValue bg = new TypedValue();
        a.getValue(R.styleable.EditText_android_background, bg);
        if (bg.resourceId == R.color.carbon_defaultColor) {
            float underlineWidth = getResources().getDimensionPixelSize(R.dimen.carbon_1dip);
            UnderlineDrawable underlineDrawable = new UnderlineDrawable();
            underlineDrawable.setThickness(underlineWidth);
            underlineDrawable.setPaddingBottom(getPaddingBottom() - getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf) + underlineWidth / 2);
            setBackgroundDrawable(underlineDrawable);
        }

        a.recycle();

        initSelectionHandle();

        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                validateInternalEvent();
            }
        });
        validateInternalEvent();

        if (getElevation() > 0)
            AnimUtils.setupElevationAnimator(stateAnimator, this);

        setSelection(length());
    }

    private void initSelectionHandle() {
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
            leftHandle.setColorFilter(Carbon.getThemeColor(getContext(), R.attr.colorAccent), PorterDuff.Mode.MULTIPLY);
            fSelectHandleLeft.set(editor, leftHandle);

            VectorDrawable rightHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_right);
            rightHandle.setColorFilter(Carbon.getThemeColor(getContext(), R.attr.colorAccent), PorterDuff.Mode.MULTIPLY);
            fSelectHandleRight.set(editor, rightHandle);

            VectorDrawable middleHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_middle);
            middleHandle.setColorFilter(Carbon.getThemeColor(getContext(), R.attr.colorAccent), PorterDuff.Mode.MULTIPLY);
            fSelectHandleCenter.set(editor, middleHandle);
        } catch (final Exception ignored) {
        }
    }

    public void setCursorColor(int cursorColor) {
        this.cursorColor = cursorColor;
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

            Field mEditorField = android.widget.TextView.class.getDeclaredField("mEditor");
            mEditorField.setAccessible(true);
            Object mEditor = mEditorField.get(this);
            Field mCursorDrawableField = mEditor.getClass().getDeclaredField("mCursorDrawable");
            mCursorDrawableField.setAccessible(true);
            Drawable[] mCursorDrawable = (Drawable[]) mCursorDrawableField.get(mEditor);

            Drawable drawable = getResources().getDrawable(R.drawable.carbon_textcursor);
            drawable.setColorFilter(new PorterDuffColorFilter(cursorColor, PorterDuff.Mode.MULTIPLY));
            mCursorDrawable[0] = drawable;

            Drawable drawable2 = getResources().getDrawable(R.drawable.carbon_textcursor);
            drawable2.setColorFilter(new PorterDuffColorFilter(cursorColor, PorterDuff.Mode.MULTIPLY));
            mCursorDrawable[1] = drawable2;
        } catch (Exception e) {
            Carbon.throwReflectionError();
        }
    }

    public int getCursorColor() {
        return cursorColor;
    }

    public CharSequence getPrefix() {
        return prefix;
    }

    public void setPrefix(CharSequence prefix) {
        this.prefix = prefix;
        if (TextUtils.isEmpty(prefix)) {
            prefixLayout = null;
            return;
        }

        TextPaint textPaint = new TextPaint(getPaint());
        textPaint.setColor(getHintTextColors().getDefaultColor());
        prefixLayout = new StaticLayout(prefix, textPaint, Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        prefixPadding = (int) prefixLayout.getLineWidth(0);
        prefixTextPadding = getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf);

        super.setPadding(getPaddingLeft() + prefixPadding + prefixTextPadding, getPaddingTop(), getPaddingRight() + suffixPadding + suffixTextPadding, getPaddingBottom());
    }

    public CharSequence getSuffix() {
        return suffix;
    }

    public void setSuffix(CharSequence suffix) {
        this.suffix = suffix;
        if (TextUtils.isEmpty(suffix)) {
            suffixLayout = null;
            return;
        }

        int padLeft = getPaddingLeft() - prefixPadding - prefixTextPadding;
        int padRight = getPaddingRight() - suffixPadding - suffixTextPadding;

        TextPaint textPaint = new TextPaint(getPaint());
        textPaint.setColor(getHintTextColors().getDefaultColor());
        suffixLayout = new StaticLayout(suffix, textPaint, Integer.MAX_VALUE, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        suffixPadding = (int) suffixLayout.getLineWidth(0);
        suffixTextPadding = getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf);

        super.setPadding(padLeft + prefixPadding + prefixTextPadding, getPaddingTop(), padRight + suffixPadding + suffixTextPadding, getPaddingBottom());
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

    public boolean isRequired() {
        return required;
    }

    /**
     * Sets it the underlying InputView has to be not empty. Adds an asterisk to hint text and label
     * text
     *
     * @param required
     */
    public void setRequired(boolean required) {
        this.required = required;
        validate();
    }

    public void validate() {
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
        boolean drawPatternOk = true, drawMatchingViewError = false, requiredOk = !required || !s.isEmpty();
        boolean counterError = (minCharacters > 0 && s.length() < minCharacters || maxCharacters < Integer.MAX_VALUE && s.length() > maxCharacters);

        if (pattern != null)
            drawPatternOk = pattern.matcher(s).matches();
        if (matchingView != 0) {
            View view = getRootView().findViewById(matchingView);
            if (view instanceof TextView) {
                TextView matchingTextView = (TextView) view;
                if (!matchingTextView.getText().toString().equals(getText().toString()))
                    drawMatchingViewError = true;
            }
        }

        valid = requiredOk && !drawMatchingViewError && drawPatternOk && !counterError;

        refreshDrawableState();
    }

    private void validateInternalEvent() {
        validateInternal();
        fireOnValidateEvent();
        postInvalidate();
    }

    public void addOnValidateListener(@NonNull OnValidateListener listener) {
        validateListeners.add(listener);
    }

    public void removeOnValidateListener(@NonNull OnValidateListener listener) {
        validateListeners.remove(listener);
    }

    public void clearOnValidateListeners() {
        validateListeners.clear();
    }

    private void fireOnValidateEvent() {
        for (OnValidateListener validateListener : validateListeners)
            validateListener.onValidate(valid);
    }

    RevealAnimator revealAnimator;

    public Point getLocationOnScreen() {
        int[] outLocation = new int[2];
        super.getLocationOnScreen(outLocation);
        return new Point(outLocation[0], outLocation[1]);
    }

    public Point getLocationInWindow() {
        int[] outLocation = new int[2];
        super.getLocationInWindow(outLocation);
        return new Point(outLocation[0], outLocation[1]);
    }

    public Animator createCircularReveal(android.view.View hotspot, float startRadius, float finishRadius) {
        int[] location = new int[2];
        hotspot.getLocationOnScreen(location);
        int[] myLocation = new int[2];
        getLocationOnScreen(myLocation);
        return createCircularReveal(location[0] - myLocation[0] + hotspot.getWidth() / 2, location[1] - myLocation[1] + hotspot.getHeight() / 2, startRadius, finishRadius);
    }

    @Override
    public Animator createCircularReveal(int x, int y, float startRadius, float finishRadius) {
        startRadius = Carbon.getRevealRadius(this, x, y, startRadius);
        finishRadius = Carbon.getRevealRadius(this, x, y, finishRadius);
        if (Carbon.IS_LOLLIPOP && renderingMode == RenderingMode.Auto) {
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(this, x, y, startRadius, finishRadius);
            circularReveal.setDuration(Carbon.getDefaultRevealDuration());
            return circularReveal;
        } else {
            revealAnimator = new RevealAnimator(x, y, startRadius, finishRadius);
            revealAnimator.setDuration(Carbon.getDefaultRevealDuration());
            revealAnimator.addUpdateListener(animation -> {
                RevealAnimator reveal = ((RevealAnimator) animation);
                reveal.radius = (float) reveal.getAnimatedValue();
                reveal.mask.reset();
                reveal.mask.addCircle(reveal.x, reveal.y, Math.max((Float) reveal.getAnimatedValue(), 1), Path.Direction.CW);
                postInvalidate();
            });
            revealAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    revealAnimator = null;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    revealAnimator = null;
                }
            });
            return revealAnimator;
        }
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
        setTextAppearanceInternal(resid, false);
    }

    public void setTextAppearance(int resid) {
        super.setTextAppearance(getContext(), resid);
        setTextAppearanceInternal(resid, false);
    }

    private void setTextAppearanceInternal(int resid, boolean hasTextColor) {
        TypedArray appearance = getContext().obtainStyledAttributes(resid, R.styleable.TextAppearance);
        if (appearance != null) {
            for (int i = 0; i < appearance.getIndexCount(); i++) {
                int attr = appearance.getIndex(i);
                if (!isInEditMode() && attr == R.styleable.TextAppearance_carbon_fontPath) {
                    String path = appearance.getString(attr);
                    Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                    setTypeface(typeface);
                } else if (attr == R.styleable.TextAppearance_carbon_fontFamily) {
                    int textStyle = appearance.getInt(R.styleable.TextAppearance_android_textStyle, 0);
                    Typeface typeface = TypefaceUtils.getTypeface(getContext(), appearance.getString(attr), textStyle);
                    setTypeface(typeface);
                } else if (attr == R.styleable.TextAppearance_android_textAllCaps) {
                    setAllCaps(appearance.getBoolean(attr, true));
                } else if (!hasTextColor && attr == R.styleable.TextAppearance_android_textColor) {
                    Carbon.initDefaultTextColor(this, appearance, attr);
                }
            }
            appearance.recycle();
        }
    }

    public void drawInternal(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (prefixLayout != null) {
            canvas.translate(getPaddingLeft() - prefixPadding - prefixTextPadding, 0);
            prefixLayout.draw(canvas);
            canvas.translate(-getPaddingLeft() + prefixPadding + prefixTextPadding, 0);
        }
        if (suffixLayout != null) {
            canvas.translate(getWidth() - getPaddingLeft() - getPaddingRight() + suffixPadding + suffixTextPadding, 0);
            suffixLayout.draw(canvas);
            canvas.translate(-getWidth() + getPaddingLeft() + getPaddingRight() - suffixPadding - suffixTextPadding, 0);
        }

        if (isFocused() && isEnabled()) {
            paint.setStrokeWidth(2 * getResources().getDimension(R.dimen.carbon_1dip));
        } else {
            paint.setStrokeWidth(getResources().getDimension(R.dimen.carbon_1dip));
        }

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    @Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isEmpty() {
        return getText().length() == 0;
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

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            if (clearFocusOnTouchOutside) {
                PopupWindow popupWindow = new PopupWindow(getContext());
                popupWindow.setBackgroundDrawable(new ColorDrawable(0x7fff0000));
                popupWindow.setTouchable(true);
                popupWindow.setAnimationStyle(0);
                View view = new View(getContext());
                view.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                popupWindow.setContentView(view);
                popupWindow.setTouchInterceptor((v, event) -> {
                    popupWindow.dismiss();
                    ViewGroup rootView = (ViewGroup) getRootView();
                    boolean focusable = rootView.isFocusable();
                    boolean focusableInTouchMode = rootView.isFocusableInTouchMode();
                    int focusability = rootView.getDescendantFocusability();
                    rootView.setFocusable(true);
                    rootView.setFocusableInTouchMode(true);
                    rootView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                    rootView.requestFocus();
                    rootView.setDescendantFocusability(focusability);
                    //rootView.setFocusableInTouchMode(focusableInTouchMode);
                    //rootView.setFocusable(focusable);
                    rootView.dispatchTouchEvent(event);
                    return true;
                });
                popupWindow.setWidth(getRootView().getWidth());
                popupWindow.setHeight(getRootView().getHeight());
                popupWindow.showAtLocation(getRootView(), Gravity.START | Gravity.TOP, 0, 0);
            }
        }
        if (!focused)
            validateInternalEvent();
    }

    public void setClearFocusOnTouchOutside(boolean enabled) {
        clearFocusOnTouchOutside = enabled;
    }


    // -------------------------------
    // corners
    // -------------------------------

    private float cornerRadius;
    private Path cornersMask;

    /**
     * Gets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @return corner radius, equal to or greater than 0.
     */
    public float getCornerRadius() {
        return cornerRadius;
    }

    /**
     * Sets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @param cornerRadius
     */
    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        if (getWidth() > 0 && getHeight() > 0)
            updateCorners();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        updateCorners();

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    private void updateCorners() {
        if (cornerRadius > 0) {
            cornerRadius = Math.min(cornerRadius, Math.min(getWidth(), getHeight()) / 2.0f);
            if (Carbon.IS_LOLLIPOP && renderingMode == RenderingMode.Auto) {
                setClipToOutline(true);
                setOutlineProvider(ShadowShape.viewOutlineProvider);
            } else {
                cornersMask = new Path();
                cornersMask.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), cornerRadius, cornerRadius, Path.Direction.CW);
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
            }
        } else if (Carbon.IS_LOLLIPOP) {
            setOutlineProvider(ViewOutlineProvider.BOUNDS);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(@NonNull Canvas canvas) {
        if (cornerRadius > 0 && getWidth() > 0 && getHeight() > 0 && (!Carbon.IS_LOLLIPOP || renderingMode == RenderingMode.Software)) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            drawInternal(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);

            paint.setXfermode(Carbon.CLEAR_MODE);
            canvas.drawPath(cornersMask, paint);

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            drawInternal(canvas);
            if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
                rippleDrawable.draw(canvas);
        }
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
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
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return super.verifyDrawable(who) || rippleDrawable == who;
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        super.invalidateDrawable(drawable);
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate(@NonNull Rect dirty) {
        super.invalidate(dirty);
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        invalidateParentIfNeeded();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateParentIfNeeded();
    }

    private void invalidateParentIfNeeded() {
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
        postInvalidateParentIfNeededDelayed(delayMilliseconds);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
        postInvalidateParentIfNeededDelayed(delayMilliseconds);
    }

    private void postInvalidateParentIfNeededDelayed(long delayMilliseconds) {
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);

        if (getElevation() > 0 || getCornerRadius() > 0)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);
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
        updateBackgroundTint();
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        updateTint();
    }


    // -------------------------------
    // elevation
    // -------------------------------

    private float elevation = 0;
    private float translationZ = 0;
    private Shadow ambientShadow, spotShadow;
    private ColorStateList shadowColor;
    private PorterDuffColorFilter shadowColorFilter;
    private RectF shadowMaskRect = new RectF();

    @Override
    public float getElevation() {
        return elevation;
    }

    @Override
    public void setElevation(float elevation) {
        if (Carbon.IS_LOLLIPOP) {
            if (shadowColor == null && renderingMode == RenderingMode.Auto) {
                super.setElevation(elevation);
                super.setTranslationZ(translationZ);
            } else {
                super.setElevation(0);
                super.setTranslationZ(0);
            }
        } else if (elevation != this.elevation && getParent() != null) {
            ((View) getParent()).postInvalidate();
        }
        this.elevation = elevation;
    }

    @Override
    public float getTranslationZ() {
        return translationZ;
    }

    public void setTranslationZ(float translationZ) {
        if (translationZ == this.translationZ)
            return;
        if (Carbon.IS_LOLLIPOP) {
            if (shadowColor == null && renderingMode == RenderingMode.Auto) {
                super.setTranslationZ(translationZ);
            } else {
                super.setTranslationZ(0);
            }
        } else if (translationZ != this.translationZ && getParent() != null) {
            ((View) getParent()).postInvalidate();
        }
        this.translationZ = translationZ;
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
    public boolean hasShadow() {
        return getElevation() + getTranslationZ() >= 0.01f && getWidth() > 0 && getHeight() > 0;
    }

    @Override
    public void drawShadow(Canvas canvas) {
        float alpha = getAlpha() * Carbon.getDrawableAlpha(getBackground()) / 255.0f * Carbon.getBackgroundTintAlpha(this) / 255.0f;
        if (alpha == 0)
            return;

        if (!hasShadow())
            return;

        float z = getElevation() + getTranslationZ();
        if (ambientShadow == null || ambientShadow.elevation != z || ambientShadow.cornerRadius != cornerRadius) {
            ambientShadow = ShadowGenerator.generateShadow(this, z / getResources().getDisplayMetrics().density / 4);
            spotShadow = ShadowGenerator.generateShadow(this, z / getResources().getDisplayMetrics().density);
        }

        int saveCount = 0;
        boolean maskShadow = getBackground() != null && alpha != 1;
        boolean r = revealAnimator != null && revealAnimator.isRunning();
        if (maskShadow) {
            saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        } else if (r) {
            saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
            canvas.clipRect(
                    getLeft() + revealAnimator.x - revealAnimator.radius, getTop() + revealAnimator.y - revealAnimator.radius,
                    getLeft() + revealAnimator.x + revealAnimator.radius, getTop() + revealAnimator.y + revealAnimator.radius);
        }

        paint.setAlpha((int) (Shadow.ALPHA * alpha));

        Matrix matrix = getMatrix();

        canvas.save();
        canvas.translate(this.getLeft(), this.getTop());
        canvas.concat(matrix);
        ambientShadow.draw(canvas, this, paint, shadowColorFilter);
        canvas.restore();

        canvas.save();
        canvas.translate(this.getLeft(), this.getTop() + z / 2);
        canvas.concat(matrix);
        spotShadow.draw(canvas, this, paint, shadowColorFilter);
        canvas.restore();

        if (saveCount != 0) {
            canvas.translate(this.getLeft(), this.getTop());
            canvas.concat(matrix);
            paint.setXfermode(Carbon.CLEAR_MODE);
        }
        if (maskShadow) {
            shadowMaskRect.set(0, 0, getWidth(), getHeight());
            canvas.drawRoundRect(shadowMaskRect, cornerRadius, cornerRadius, paint);
        }
        if (r) {
            canvas.drawPath(revealAnimator.mask, paint);
        }
        if (saveCount != 0) {
            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        }
    }

    @Override
    public void setElevationShadowColor(ColorStateList shadowColor) {
        this.shadowColor = shadowColor;
        shadowColorFilter = shadowColor != null ? new PorterDuffColorFilter(shadowColor.getColorForState(getDrawableState(), shadowColor.getDefaultColor()), PorterDuff.Mode.MULTIPLY) : Shadow.DEFAULT_FILTER;
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public void setElevationShadowColor(int color) {
        shadowColor = ColorStateList.valueOf(color);
        shadowColorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY);
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public ColorStateList getElevationShadowColor() {
        return shadowColor;
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

    final RectF tmpHitRect = new RectF();

    public void getHitRect(@NonNull Rect outRect) {
        Matrix matrix = getMatrix();
        if (matrix.isIdentity()) {
            outRect.set(getLeft(), getTop(), getRight(), getBottom());
        } else {
            tmpHitRect.set(0, 0, getWidth(), getHeight());
            matrix.mapRect(tmpHitRect);
            outRect.set((int) tmpHitRect.left + getLeft(), (int) tmpHitRect.top + getTop(),
                    (int) tmpHitRect.right + getLeft(), (int) tmpHitRect.bottom + getTop());
        }
        outRect.left -= touchMargin.left;
        outRect.top -= touchMargin.top;
        outRect.right += touchMargin.right;
        outRect.bottom += touchMargin.bottom;
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

    private Animator inAnim = null, outAnim = null;
    private Animator animator;

    public Animator animateVisibility(final int visibility) {
        if (visibility == View.VISIBLE && (getVisibility() != View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (inAnim != null) {
                animator = inAnim;
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator a) {
                        a.removeListener(this);
                        animator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator a) {
                        a.removeListener(this);
                        animator = null;
                    }
                });
                animator.start();
            }
            setVisibility(visibility);
        } else if (visibility != View.VISIBLE && (getVisibility() == View.VISIBLE || animator != null)) {
            if (animator != null)
                animator.cancel();
            if (outAnim == null) {
                setVisibility(visibility);
                return null;
            }
            animator = outAnim;
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator a) {
                    if (((ValueAnimator) a).getAnimatedFraction() == 1)
                        setVisibility(visibility);
                    a.removeListener(this);
                    animator = null;
                }

                @Override
                public void onAnimationCancel(Animator a) {
                    a.removeListener(this);
                    animator = null;
                }
            });
            animator.start();
        } else {
            setVisibility(visibility);
        }
        return animator;
    }

    public Animator getAnimator() {
        return animator;
    }

    public Animator getOutAnimator() {
        return outAnim;
    }

    public void setOutAnimator(Animator outAnim) {
        if (this.outAnim != null)
            this.outAnim.setTarget(null);
        this.outAnim = outAnim;
        if (outAnim != null)
            outAnim.setTarget(this);
    }

    public Animator getInAnimator() {
        return inAnim;
    }

    public void setInAnimator(Animator inAnim) {
        if (this.inAnim != null)
            this.inAnim.setTarget(null);
        this.inAnim = inAnim;
        if (inAnim != null)
            inAnim.setTarget(this);
    }


    // -------------------------------
    // tint
    // -------------------------------

    ColorStateList tint;
    PorterDuff.Mode tintMode;
    ColorStateList backgroundTint;
    PorterDuff.Mode backgroundTintMode;
    boolean animateColorChanges;
    ValueAnimator.AnimatorUpdateListener tintAnimatorListener = animation -> {
        updateTint();
        ViewCompat.postInvalidateOnAnimation(this);
    };
    ValueAnimator.AnimatorUpdateListener backgroundTintAnimatorListener = animation -> {
        updateBackgroundTint();
        ViewCompat.postInvalidateOnAnimation(this);
    };
    ValueAnimator.AnimatorUpdateListener textColorAnimatorListener = animation -> setHintTextColor(getHintTextColors());

    @Override
    public void setTint(ColorStateList list) {
        if (list != null) {
            tint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        } else {
            tint = null;
        }
        updateTint();
    }

    @Override
    public void setTint(int color) {
        setTint(ColorStateList.valueOf(color));
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
        setBackgroundTint(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getBackgroundTint() {
        return backgroundTint;
    }

    private void updateBackgroundTint() {
        Drawable background = getBackground();
        if (background == null)
            return;

        DrawableCompat.setTintList(background, tint);
        DrawableCompat.setTintMode(background, backgroundTintMode);
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
    // auto size
    // -------------------------------

    private AutoSizeTextMode autoSizeText = AutoSizeTextMode.None;
    private float minTextSize, maxTextSize, autoSizeStepGranularity;
    private float[] autoSizeStepPresets;

    private RectF textRect = new RectF();
    private RectF availableSpaceRect = new RectF();
    private float spacingMult = 1.0f;
    private float spacingAdd = 0.0f;
    private int maxLines = -1;

    @NonNull
    public AutoSizeTextMode getAutoSizeText() {
        return autoSizeText;
    }

    public void setAutoSizeText(@NonNull AutoSizeTextMode autoSizeText) {
        this.autoSizeText = autoSizeText;
        adjustTextSize();
    }

    @Override
    public void setText(final CharSequence text, BufferType type) {
        super.setText(text, type);
        adjustTextSize();
    }

    @Override
    public void setTextSize(float size) {
        super.setTextSize(size);
        adjustTextSize();
    }

    @Override
    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        this.maxLines = maxLines;
        adjustTextSize();
    }

    @Override
    public void setSingleLine() {
        super.setSingleLine();
        adjustTextSize();
    }

    @Override
    public void setSingleLine(boolean singleLine) {
        super.setSingleLine(singleLine);
        if (!singleLine)
            super.setMaxLines(-1);
        adjustTextSize();
    }

    @Override
    public void setLines(int lines) {
        super.setLines(lines);
        adjustTextSize();
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        adjustTextSize();
    }

    @Override
    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        spacingMult = mult;
        spacingAdd = add;
    }

    private void initAutoSize() {
        if (autoSizeText == AutoSizeTextMode.Uniform && minTextSize > 0 && maxTextSize > 0) {
            autoSizeStepPresets = new float[(int) Math.ceil((maxTextSize - minTextSize) / autoSizeStepGranularity) + 1];
            for (int i = 0; i < autoSizeStepPresets.length - 1; i++)
                autoSizeStepPresets[i] = minTextSize + autoSizeStepGranularity * i;
            autoSizeStepPresets[autoSizeStepPresets.length - 1] = maxTextSize;
        }
    }

    public float getMinTextSize() {
        return minTextSize;
    }

    public void setMinTextSize(float minTextSize) {
        this.minTextSize = minTextSize;
        autoSizeStepPresets = null;
        adjustTextSize();
    }

    public float getMaxTextSize() {
        return maxTextSize;
    }

    public int getAutoSizeStepGranularity() {
        return (int) autoSizeStepGranularity;
    }

    public void setAutoSizeStepGranularity(int autoSizeStepGranularity) {
        setAutoSizeStepGranularity((float) autoSizeStepGranularity);
    }

    public void setAutoSizeStepGranularity(float autoSizeStepGranularity) {
        this.autoSizeStepGranularity = autoSizeStepGranularity;
        autoSizeStepPresets = null;
        adjustTextSize();
    }

    public void setMaxTextSize(float maxTextSize) {
        this.maxTextSize = maxTextSize;
        autoSizeStepPresets = null;
        adjustTextSize();
    }

    private void adjustTextSize() {
        if (autoSizeText == AutoSizeTextMode.None || minTextSize <= 0 || maxTextSize <= 0 || getMeasuredWidth() == 0 || getMeasuredHeight() == 0)
            return;
        if (autoSizeStepPresets == null)
            initAutoSize();
        availableSpaceRect.right = getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
        availableSpaceRect.bottom = getMeasuredHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop();
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, binarySearch(availableSpaceRect));
    }

    private float binarySearch(RectF availableSpace) {
        int lastBest = 0;
        int lo = 0;
        int hi = autoSizeStepPresets.length - 1;
        int mid;
//        for (int i = 0; i < autoSizeStepPresets.length; i++) {
//            if (testSize(autoSizeStepPresets[i], availableSpace)) {
//                lastBest = i;
//            } else {
//                break;
//            }
//        }
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            boolean fits = testSize(autoSizeStepPresets[mid], availableSpace);
            if (fits) {
                lastBest = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return autoSizeStepPresets[lastBest];

    }

    public boolean testSize(float suggestedSize, RectF availableSpace) {
        paint.setTextSize(suggestedSize);
        paint.setTypeface(getTypeface());
        String text = getText().toString();
        if (maxLines == 1) {
            textRect.bottom = paint.getFontSpacing();
            textRect.right = paint.measureText(text);
            return availableSpace.width() >= textRect.right && availableSpace.height() >= textRect.bottom;
        } else {
            StaticLayout layout = new StaticLayout(text, paint, (int) availableSpace.right, Layout.Alignment.ALIGN_NORMAL, spacingMult, spacingAdd, true);
            if (maxLines != -1 && layout.getLineCount() > maxLines)
                return false;
            return availableSpace.width() >= layout.getWidth() && availableSpace.height() >= layout.getHeight();
        }
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
        super.onTextChanged(text, start, before, after);
        adjustTextSize();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if (width != oldwidth || height != oldheight)
            adjustTextSize();
    }


    // -------------------------------
    // rendering mode
    // -------------------------------

    private RenderingMode renderingMode = RenderingMode.Auto;

    @Override
    public void setRenderingMode(RenderingMode mode) {
        this.renderingMode = mode;
        setElevation(elevation);
        setTranslationZ(translationZ);
        updateCorners();
    }

    @Override
    public RenderingMode getRenderingMode() {
        return renderingMode;
    }


    // -------------------------------
    // transformations
    // -------------------------------

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        invalidateParentIfNeeded();
    }

    @Override
    public void setRotationY(float rotationY) {
        super.setRotationY(rotationY);
        invalidateParentIfNeeded();
    }

    @Override
    public void setRotationX(float rotationX) {
        super.setRotationX(rotationX);
        invalidateParentIfNeeded();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        invalidateParentIfNeeded();
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        invalidateParentIfNeeded();
    }

    @Override
    public void setPivotX(float pivotX) {
        super.setPivotX(pivotX);
        invalidateParentIfNeeded();
    }

    @Override
    public void setPivotY(float pivotY) {
        super.setPivotY(pivotY);
        invalidateParentIfNeeded();
    }

    @Override
    public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        super.setAlpha(alpha);
        invalidateParentIfNeeded();
    }

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
        invalidateParentIfNeeded();
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        invalidateParentIfNeeded();
    }

    public void setWidth(int width) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            layoutParams.width = width;
            setLayoutParams(layoutParams);
        }
    }

    public void setHeight(int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height));
        } else {
            layoutParams.height = height;
            setLayoutParams(layoutParams);
        }
    }

    public void setSize(int width, int height) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            setLayoutParams(new ViewGroup.LayoutParams(width, height));
        } else {
            layoutParams.width = width;
            layoutParams.height = height;
            setLayoutParams(layoutParams);
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        setSize(width, height);
        setTranslationX(x);
        setTranslationY(y);
    }
}
