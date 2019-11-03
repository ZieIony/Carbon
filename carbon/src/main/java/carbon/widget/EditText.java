package carbon.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import carbon.Carbon;
import carbon.CarbonContextWrapper;
import carbon.R;
import carbon.animation.AnimatedColorStateList;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.UnderlineDrawable;
import carbon.drawable.ripple.RippleDrawable;
import carbon.drawable.ripple.RippleView;
import carbon.internal.AllCapsTransformationMethod;
import carbon.internal.RevealAnimator;
import carbon.internal.SimpleTextWatcher;
import carbon.view.AutoSizeTextView;
import carbon.view.InputView;
import carbon.view.MarginView;
import carbon.view.MaxSizeView;
import carbon.view.RevealView;
import carbon.view.ShadowView;
import carbon.view.ShapeModelView;
import carbon.view.StateAnimatorView;
import carbon.view.StrokeView;
import carbon.view.TextAppearanceView;
import carbon.view.TintedView;
import carbon.view.TouchMarginView;
import carbon.view.ValidStateView;
import carbon.view.VisibleView;

@SuppressLint("AppCompatCustomView")
public class EditText extends android.widget.EditText
        implements
        ShadowView,
        RippleView,
        TouchMarginView,
        StateAnimatorView,
        AnimatedView,
        ShapeModelView,
        TintedView,
        InputView,
        StrokeView,
        MaxSizeView,
        ValidStateView,
        AutoSizeTextView,
        RevealView,
        VisibleView,
        MarginView,
        TextAppearanceView {

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
    private boolean skipValidate = false;

    private CharSequence prefix, suffix;
    private StaticLayout prefixLayout, suffixLayout;
    private int prefixPadding, prefixTextPadding, suffixPadding, suffixTextPadding;

    private List<OnValidateListener> validateListeners = new ArrayList<>();
    private boolean clearFocusOnTouchOutside = false;

    public EditText(Context context) {
        super(CarbonContextWrapper.wrap(context));
        initEditText(null, android.R.attr.editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(CarbonContextWrapper.wrap(context), attrs);
        initEditText(attrs, android.R.attr.editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(CarbonContextWrapper.wrap(context), attrs, defStyleAttr);
        initEditText(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(CarbonContextWrapper.wrap(context), attrs, defStyleAttr, defStyleRes);
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
    private static int[] strokeIds = new int[]{
            R.styleable.EditText_carbon_stroke,
            R.styleable.EditText_carbon_strokeWidth
    };
    private static int[] cornerCutRadiusIds = new int[]{
            R.styleable.EditText_carbon_cornerRadiusTopStart,
            R.styleable.EditText_carbon_cornerRadiusTopEnd,
            R.styleable.EditText_carbon_cornerRadiusBottomStart,
            R.styleable.EditText_carbon_cornerRadiusBottomEnd,
            R.styleable.EditText_carbon_cornerRadius,
            R.styleable.EditText_carbon_cornerCutTopStart,
            R.styleable.EditText_carbon_cornerCutTopEnd,
            R.styleable.EditText_carbon_cornerCutBottomStart,
            R.styleable.EditText_carbon_cornerCutBottomEnd,
            R.styleable.EditText_carbon_cornerCut
    };
    private static int[] maxSizeIds = new int[]{
            R.styleable.EditText_carbon_maxWidth,
            R.styleable.EditText_carbon_maxHeight,
    };
    private static int[] elevationIds = new int[]{
            R.styleable.EditText_carbon_elevation,
            R.styleable.EditText_carbon_elevationShadowColor,
            R.styleable.EditText_carbon_elevationAmbientShadowColor,
            R.styleable.EditText_carbon_elevationSpotShadowColor
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
            Carbon.setTextAppearance(this, ap, a.hasValue(R.styleable.EditText_android_textColor), false);

        int textStyle = a.getInt(R.styleable.EditText_android_textStyle, 0);
        int fontWeight = a.getInt(R.styleable.EditText_carbon_fontWeight, 400);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EditText_carbon_font) {
                Carbon.handleFontAttribute(this, a, textStyle, fontWeight, attr);
            } else if (attr == R.styleable.EditText_android_textAllCaps) {
                setAllCaps(a.getBoolean(attr, true));
            } else if (attr == R.styleable.EditText_android_singleLine) {
                setSingleLine(a.getBoolean(attr, false));
            } else if (attr == R.styleable.EditText_android_maxLines) {
                setMaxLines(a.getInt(attr, Integer.MAX_VALUE));
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
        Carbon.initElevation(this, a, elevationIds);
        Carbon.initTint(this, a, tintIds);
        Carbon.initAnimations(this, a, animationIds);
        Carbon.initTouchMargin(this, a, touchMarginIds);
        Carbon.initMaxSize(this, a, maxSizeIds);
        Carbon.initHtmlText(this, a, R.styleable.EditText_carbon_htmlText);
        Carbon.initStroke(this, a, strokeIds);
        Carbon.initCornerCutRadius(this, a, cornerCutRadiusIds);
        Carbon.initAutoSizeText(this, a, autoSizeTextIds);

        if (a.getResourceId(R.styleable.EditText_android_background, 0) == R.color.carbon_colorUnderline) {
            float underlineWidth = getResources().getDimensionPixelSize(R.dimen.carbon_1dip);
            UnderlineDrawable underlineDrawable = new UnderlineDrawable();
            underlineDrawable.setThickness(underlineWidth);
            underlineDrawable.setPaddingBottom(getPaddingBottom() - getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf) + underlineWidth / 2);
            setBackgroundDrawable(underlineDrawable);
        }

        a.recycle();

        if (!isInEditMode())
            initSelectionHandle();

        addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (!skipValidate)
                    validateInternalEvent();
            }
        });

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

            Drawable leftHandle = getResources().getDrawable(R.drawable.carbon_selecthandle_left);
            leftHandle.setColorFilter(Carbon.getThemeColor(getContext(), R.attr.colorSecondary), PorterDuff.Mode.SRC_IN);
            fSelectHandleLeft.set(editor, leftHandle);

            Drawable rightHandle = getResources().getDrawable(R.drawable.carbon_selecthandle_right);
            rightHandle.setColorFilter(Carbon.getThemeColor(getContext(), R.attr.colorSecondary), PorterDuff.Mode.SRC_IN);
            fSelectHandleRight.set(editor, rightHandle);

            Drawable middleHandle = getResources().getDrawable(R.drawable.carbon_selecthandle_middle);
            middleHandle.setColorFilter(Carbon.getThemeColor(getContext(), R.attr.colorSecondary), PorterDuff.Mode.SRC_IN);
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
            drawable.setColorFilter(new PorterDuffColorFilter(cursorColor, PorterDuff.Mode.SRC_IN));
            mCursorDrawable[0] = drawable;

            Drawable drawable2 = getResources().getDrawable(R.drawable.carbon_textcursor);
            drawable2.setColorFilter(new PorterDuffColorFilter(cursorColor, PorterDuff.Mode.SRC_IN));
            mCursorDrawable[1] = drawable2;
        } catch (Exception e) {
            Carbon.logReflectionError(e);
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
    public void setTextColor(@NonNull ColorStateList colors) {
        super.setTextColor(animateColorChanges && !(colors instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(colors, textColorAnimatorListener) : colors);
    }

    @Override
    public void setTextAppearance(@NonNull Context context, int resid) {
        super.setTextAppearance(context, resid);
        Carbon.setTextAppearance(this, resid, false, false);
    }

    public void setTextAppearance(int resid) {
        super.setTextAppearance(getContext(), resid);
        Carbon.setTextAppearance(this, resid, false, false);
    }

    @Override
    public void setValid(boolean valid) {
        if (this.valid == valid)
            return;
        this.valid = valid;
        refreshDrawableState();
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public boolean isEmpty() {
        return getText().length() == 0;
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
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
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

    private RectF boundsRect = new RectF();
    private Path cornersMask = new Path();

    public ShapeAppearanceModel getShapeModel() {
        return shapeModel;
    }


    /**
     * Sets the corner radius. If corner radius is equal to 0, rounded corners are turned off.
     *
     * @param cornerRadius
     */
    @Override
    public void setCornerRadius(float cornerRadius) {
        shapeModel = ShapeAppearanceModel.builder().setAllCorners(new RoundedCornerTreatment(cornerRadius)).build();
        setShapeModel(shapeModel);
    }

    @Override
    public void setCornerCut(float cornerCut) {
        shapeModel = ShapeAppearanceModel.builder().setAllCorners(new CutCornerTreatment(cornerCut)).build();
        setShapeModel(shapeModel);
    }

    @Override
    public void setShapeModel(ShapeAppearanceModel model) {
        this.shapeModel = model;
        shadowDrawable = new MaterialShapeDrawable(shapeModel);
        if (getWidth() > 0 && getHeight() > 0)
            updateCorners();
        if (!Carbon.IS_LOLLIPOP_OR_HIGHER)
            postInvalidate();
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
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            setClipToOutline(true);
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (Carbon.isShapeRect(shapeModel, boundsRect)) {
                        outline.setRect(0, 0, getWidth(), getHeight());
                    } else {
                        shadowDrawable.setBounds(0, 0, getWidth(), getHeight());
                        shadowDrawable.getOutline(outline);
                    }
                }
            });
        }

        boundsRect.set(shadowDrawable.getBounds());
        shadowDrawable.getPathForSize(getWidth(), getHeight(), cornersMask);
    }

    public void drawInternal(@NonNull Canvas canvas) {
        if (animateColorChanges)
            drawableStateChanged();
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

        if (stroke != null)
            drawStroke(canvas);
        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(@NonNull Canvas canvas) {
        boolean r = revealAnimator != null;
        boolean c = !Carbon.isShapeRect(shapeModel, boundsRect);

        if (Carbon.IS_PIE_OR_HIGHER) {
            if (spotShadowColor != null)
                super.setOutlineSpotShadowColor(spotShadowColor.getColorForState(getDrawableState(), spotShadowColor.getDefaultColor()));
            if (ambientShadowColor != null)
                super.setOutlineAmbientShadowColor(ambientShadowColor.getColorForState(getDrawableState(), ambientShadowColor.getDefaultColor()));
        }

        if (isInEditMode()) {
            if ((r || c) && getWidth() > 0 && getHeight() > 0) {
                Bitmap layer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas layerCanvas = new Canvas(layer);
                drawInternal(layerCanvas);

                Bitmap mask = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
                Canvas maskCanvas = new Canvas(mask);
                Paint maskPaint = new Paint(0xffffffff);
                maskCanvas.drawPath(cornersMask, maskPaint);

                for (int x = 0; x < getWidth(); x++) {
                    for (int y = 0; y < getHeight(); y++) {
                        int maskPixel = mask.getPixel(x, y);
                        layer.setPixel(x, y, Color.alpha(maskPixel) > 0 ? layer.getPixel(x, y) : 0);
                    }
                }
                canvas.drawBitmap(layer, 0, 0, paint);
            } else {
                drawInternal(canvas);
            }
        } else if (getWidth() > 0 && getHeight() > 0 && (((r || c) && !Carbon.IS_LOLLIPOP_OR_HIGHER) || !shapeModel.isRoundRect(boundsRect))) {
            int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);

            if (r) {
                int saveCount2 = canvas.save();
                canvas.clipRect(revealAnimator.x - revealAnimator.radius, revealAnimator.y - revealAnimator.radius, revealAnimator.x + revealAnimator.radius, revealAnimator.y + revealAnimator.radius);
                drawInternal(canvas);
                canvas.restoreToCount(saveCount2);
            } else {
                drawInternal(canvas);
            }

            paint.setXfermode(Carbon.CLEAR_MODE);
            if (c) {
                cornersMask.setFillType(Path.FillType.INVERSE_WINDING);
                canvas.drawPath(cornersMask, paint);
            }
            if (r)
                canvas.drawPath(revealAnimator.mask, paint);
            paint.setXfermode(null);    // TODO check if this is needed

            canvas.restoreToCount(saveCount);
            paint.setXfermode(null);
        } else {
            drawInternal(canvas);
        }
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        if (shadowDrawable.isPointInTransparentRegion((int) event.getX(), (int) event.getY()))
            return false;

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
            newRipple.setState(getDrawableState());
            ((Drawable) newRipple).setVisible(getVisibility() == VISIBLE, false);
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

        if (elevation > 0 || !Carbon.isShapeRect(shapeModel, boundsRect))
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

        if (elevation > 0 || !Carbon.isShapeRect(shapeModel, boundsRect))
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
        super.setCompoundDrawables(
                left != null ? DrawableCompat.wrap(left) : null,
                top != null ? DrawableCompat.wrap(top) : null,
                right != null ? DrawableCompat.wrap(right) : null,
                bottom != null ? DrawableCompat.wrap(bottom) : null);
        updateTint();
    }


    // -------------------------------
    // elevation
    // -------------------------------

    private float elevation = 0;
    private float translationZ = 0;
    private ShapeAppearanceModel shapeModel = new ShapeAppearanceModel();
    private MaterialShapeDrawable shadowDrawable = new MaterialShapeDrawable(shapeModel);
    private ColorStateList ambientShadowColor, spotShadowColor;

    @Override
    public float getElevation() {
        return elevation;
    }

    @Override
    public void setElevation(float elevation) {
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setElevation(elevation);
            super.setTranslationZ(translationZ);
        } else if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            if (ambientShadowColor == null || spotShadowColor == null) {
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
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setTranslationZ(translationZ);
        } else if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            if (ambientShadowColor == null || spotShadowColor == null) {
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
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public boolean hasShadow() {
        return getElevation() + getTranslationZ() >= 0.01f && getWidth() > 0 && getHeight() > 0;
    }

    @Override
    public void drawShadow(Canvas canvas) {
        float alpha = getAlpha() * Carbon.getBackgroundTintAlpha(this) / 255.0f;
        if (alpha == 0 || !hasShadow())
            return;

        float z = getElevation() + getTranslationZ();

        int saveCount;
        boolean maskShadow = getBackground() != null && alpha != 1;
        boolean r = revealAnimator != null && revealAnimator.isRunning();

        if (alpha != 255) {
            paint.setAlpha((int) (127 * alpha));
            saveCount = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), paint, Canvas.ALL_SAVE_FLAG);
        } else {
            saveCount = canvas.save();
        }
        Matrix matrix = getMatrix();
        canvas.setMatrix(matrix);

        if (r) {
            canvas.clipRect(
                    getLeft() + revealAnimator.x - revealAnimator.radius, getTop() + revealAnimator.y - revealAnimator.radius,
                    getLeft() + revealAnimator.x + revealAnimator.radius, getTop() + revealAnimator.y + revealAnimator.radius);
        }

        shadowDrawable.setFillColor(spotShadowColor);
        shadowDrawable.setShadowColor(spotShadowColor != null ? spotShadowColor.getColorForState(getDrawableState(), spotShadowColor.getDefaultColor()) : 0xff000000);
        shadowDrawable.setAlpha(0x44);
        shadowDrawable.setElevation(z);
        shadowDrawable.setShadowVerticalOffset(0);
        shadowDrawable.setBounds(getLeft(), (int) (getTop() + z / 4), getRight(), (int) (getBottom() + z / 4));
        shadowDrawable.draw(canvas);

        canvas.translate(this.getLeft(), this.getTop());
        canvas.concat(matrix);
        paint.setXfermode(Carbon.CLEAR_MODE);
        if (maskShadow) {
            cornersMask.setFillType(Path.FillType.WINDING);
            canvas.drawPath(cornersMask, paint);
        }
        if (r)
            canvas.drawPath(revealAnimator.mask, paint);

        canvas.restoreToCount(saveCount);
        paint.setXfermode(null);
        paint.setAlpha(255);
    }

    @Override
    public void setElevationShadowColor(ColorStateList shadowColor) {
        ambientShadowColor = spotShadowColor = shadowColor;
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public void setElevationShadowColor(int color) {
        ambientShadowColor = spotShadowColor = ColorStateList.valueOf(color);
        setElevation(elevation);
        setTranslationZ(translationZ);
    }

    @Override
    public ColorStateList getElevationShadowColor() {
        return ambientShadowColor;
    }

    @Override
    public void setOutlineAmbientShadowColor(int color) {
        setOutlineAmbientShadowColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setOutlineAmbientShadowColor(ColorStateList color) {
        ambientShadowColor = color;
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setOutlineAmbientShadowColor(color.getColorForState(getDrawableState(), color.getDefaultColor()));
        } else {
            setElevation(elevation);
            setTranslationZ(translationZ);
        }
    }

    @Override
    public int getOutlineAmbientShadowColor() {
        return ambientShadowColor.getDefaultColor();
    }

    @Override
    public void setOutlineSpotShadowColor(int color) {
        setOutlineSpotShadowColor(ColorStateList.valueOf(color));
    }

    @Override
    public void setOutlineSpotShadowColor(ColorStateList color) {
        spotShadowColor = color;
        if (Carbon.IS_PIE_OR_HIGHER) {
            super.setOutlineSpotShadowColor(color.getColorForState(getDrawableState(), color.getDefaultColor()));
        } else {
            setElevation(elevation);
            setTranslationZ(translationZ);
        }
    }

    @Override
    public int getOutlineSpotShadowColor() {
        return spotShadowColor.getDefaultColor();
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin = new Rect();

    @Override
    public void setTouchMargin(int left, int top, int right, int bottom) {
        touchMargin.set(left, top, right, bottom);
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

    private static final int[] INVALID_STATE_SET = {
            R.attr.carbon_state_invalid
    };

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (!isValid()) {
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, INVALID_STATE_SET);
            return drawableState;
        }
        return super.onCreateDrawableState(extraSpace);
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
    public void setTintList(ColorStateList list) {
        this.tint = list == null ? null : animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, tintAnimatorListener) : list;
        updateTint();
    }

    @Override
    public void setTint(int color) {
        setTintList(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }

    private void updateTint() {
        Drawable[] drawables = getCompoundDrawables();
        if (tint != null && tintMode != null) {
            for (Drawable drawable : drawables) {
                if (drawable != null) {
                    Carbon.setTintListMode(drawable, tint, tintMode);

                    if (drawable.isStateful())
                        drawable.setState(getDrawableState());
                }
            }
        } else {
            for (Drawable drawable : drawables) {
                if (drawable != null) {
                    Carbon.setTintList(drawable, null);

                    if (drawable.isStateful())
                        drawable.setState(getDrawableState());
                }
            }
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
    public void setBackgroundTintList(ColorStateList list) {
        this.backgroundTint = animateColorChanges && !(list instanceof AnimatedColorStateList) ? AnimatedColorStateList.fromList(list, backgroundTintAnimatorListener) : list;
        updateBackgroundTint();
    }

    @Override
    public void setBackgroundTint(int color) {
        setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getBackgroundTint() {
        return backgroundTint;
    }

    private void updateBackgroundTint() {
        Drawable background = getBackground();
        if (background instanceof RippleDrawable)
            background = ((RippleDrawable) background).getBackground();
        if (background == null)
            return;

        if (backgroundTint != null && backgroundTintMode != null) {
            Carbon.setTintListMode(background, backgroundTint, backgroundTintMode);
        } else {
            Carbon.setTintList(background, null);
        }

        if (background.isStateful())
            background.setState(getDrawableState());
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
            setTintList(AnimatedColorStateList.fromList(tint, tintAnimatorListener));
        if (backgroundTint != null && !(backgroundTint instanceof AnimatedColorStateList))
            setBackgroundTintList(AnimatedColorStateList.fromList(backgroundTint, backgroundTintAnimatorListener));
        if (!(getTextColors() instanceof AnimatedColorStateList))
            setTextColor(AnimatedColorStateList.fromList(getTextColors(), textColorAnimatorListener));
    }


    // -------------------------------
    // stroke
    // -------------------------------

    private ColorStateList stroke;
    private float strokeWidth;
    private Paint strokePaint;

    private void drawStroke(Canvas canvas) {
        strokePaint.setStrokeWidth(strokeWidth * 2);
        strokePaint.setColor(stroke.getColorForState(getDrawableState(), stroke.getDefaultColor()));
        cornersMask.setFillType(Path.FillType.WINDING);
        canvas.drawPath(cornersMask, strokePaint);
    }

    @Override
    public void setStroke(ColorStateList colorStateList) {
        stroke = colorStateList;

        if (stroke == null)
            return;

        if (strokePaint == null) {
            strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            strokePaint.setStyle(Paint.Style.STROKE);
        }
    }

    @Override
    public void setStroke(int color) {
        setStroke(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getStroke() {
        return stroke;
    }

    @Override
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    public float getStrokeWidth() {
        return strokeWidth;
    }


    // -------------------------------
    // maximum width & height
    // -------------------------------

    int maxWidth = Integer.MAX_VALUE, maxHeight = Integer.MAX_VALUE;

    @Override
    public int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        requestLayout();
    }

    @Override
    public int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        requestLayout();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredWidth() > maxWidth || getMeasuredHeight() > maxHeight) {
            if (getMeasuredWidth() > maxWidth)
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
            if (getMeasuredHeight() > maxHeight)
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
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
        skipValidate = true;
        super.setText(text, type);
        skipValidate = false;
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
    // transformations
    // -------------------------------

    List<OnTransformationChangedListener> transformationChangedListeners = new ArrayList<>();

    public void addOnTransformationChangedListener(OnTransformationChangedListener listener) {
        transformationChangedListeners.add(listener);
    }

    public void removeOnTransformationChangedListener(OnTransformationChangedListener listener) {
        transformationChangedListeners.remove(listener);
    }

    public void clearOnTransformationChangedListeners() {
        transformationChangedListeners.clear();
    }

    private void fireOnTransformationChangedListener() {
        if (transformationChangedListeners == null)
            return;
        for (OnTransformationChangedListener listener : transformationChangedListeners)
            listener.onTransformationChanged();
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setRotationY(float rotationY) {
        super.setRotationY(rotationY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setRotationX(float rotationX) {
        super.setRotationX(rotationX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setPivotX(float pivotX) {
        super.setPivotX(pivotX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setPivotY(float pivotY) {
        super.setPivotY(pivotY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        super.setAlpha(alpha);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setTranslationX(float translationX) {
        super.setTranslationX(translationX);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
    }

    @Override
    public void setTranslationY(float translationY) {
        super.setTranslationY(translationY);
        invalidateParentIfNeeded();
        fireOnTransformationChangedListener();
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
