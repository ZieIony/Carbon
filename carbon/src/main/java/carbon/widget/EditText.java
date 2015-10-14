package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.AnimatedView;
import carbon.animation.StateAnimator;
import carbon.drawable.ControlFocusedColorStateList;
import carbon.drawable.EmptyDrawable;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;
import carbon.drawable.VectorDrawable;
import carbon.internal.TypefaceUtils;

/**
 * Created by Marcin on 2015-02-14.
 */
public class EditText extends android.widget.EditText implements RippleView, TouchMarginView, AnimatedView, TintedView {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    private static final int ID_CUT = android.R.id.cut;
    private static final int ID_COPY = android.R.id.copy;
    private static final int ID_PASTE = android.R.id.paste;
    private static final int ID_COPY_URL = android.R.id.copyUrl;
    private static final int ID_SELECT_ALL = android.R.id.selectAll;

    int DIVIDER_PADDING;
    int disabledColor = 0x4d000000;
    int errorColor = 0xffff0000;

    private Pattern pattern;
    private String errorMessage;
    TextPaint errorPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    boolean drawError, afterFirstInteraction = false;

    private int matchingView;
    int minCharacters;
    int maxCharacters;
    TextPaint counterPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    TextPaint labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    int internalPaddingTop = 0, internalPaddingBottom = 0;

    private BitmapShader dashPathShader;
    private float labelFrac = 0;
    private boolean showFloatingLabel = true;
    private boolean drawDivider = true;
    private boolean counterError = false;

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
        }
    };

    public EditText(Context context) {
        this(context, null);
    }

    public EditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyleAttr) {
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
            }
        }

        setPattern(a.getString(R.styleable.EditText_carbon_pattern));
        DIVIDER_PADDING = (int) getResources().getDimension(R.dimen.carbon_paddingHalf);

        if (!isInEditMode())
            setError(a.getString(R.styleable.EditText_carbon_errorMessage));
        setMatchingView(a.getResourceId(R.styleable.EditText_carbon_matchingView, 0));
        setMinCharacters(a.getInt(R.styleable.EditText_carbon_minCharacters, 0));
        setMaxCharacters(a.getInt(R.styleable.EditText_carbon_maxCharacters, Integer.MAX_VALUE));
        setFloatingLabelEnabled(a.getBoolean(R.styleable.EditText_carbon_floatingLabel, false));

        a.recycle();

        Carbon.initRippleDrawable(this, attrs, defStyleAttr);
        Carbon.initAnimations(this, attrs, defStyleAttr);
        Carbon.initTouchMargin(this, attrs, defStyleAttr);
        Carbon.initTint(this, attrs, defStyleAttr);

        if (!isInEditMode()) {
            errorPaint.setTypeface(Roboto.getTypeface(getContext(), Roboto.Style.Regular));
            errorPaint.setTextSize(getResources().getDimension(R.dimen.carbon_errorTextSize));
            errorPaint.setColor(errorColor);

            labelPaint.setTypeface(Roboto.getTypeface(getContext(), Roboto.Style.Regular));
            labelPaint.setTextSize(getResources().getDimension(R.dimen.carbon_labelTextSize));

            counterPaint.setTypeface(Roboto.getTypeface(getContext(), Roboto.Style.Regular));
            counterPaint.setTextSize(getResources().getDimension(R.dimen.carbon_charCounterTextSize));
        }

        addTextChangedListener(textWatcher);

        float dip = getResources().getDimension(R.dimen.carbon_1dip);
        Bitmap dashPathBitmap = Bitmap.createBitmap((int) (dip * 4), (int) dip, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(dashPathBitmap);
        paint.setColor(0xffffffff);
        paint.setAlpha(255);
        paint.setColor(disabledColor);
        c.drawCircle(dashPathBitmap.getHeight() / 2.0f, dashPathBitmap.getHeight() / 2.0f, dashPathBitmap.getHeight() / 2.0f, paint);
        dashPathShader = new BitmapShader(dashPathBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        PADDING_ERROR = getResources().getDimension(R.dimen.carbon_paddingHalf);
        PADDING_LABEL = getResources().getDimension(R.dimen.carbon_paddingHalf);

        initActionModeCallback();

        validateInternalEvent();

        if (isFocused() && getText().length() > 0)
            labelFrac = 1;

        initSelectionHandle();
    }

    public void validate(){
        validateInternal();
        postInvalidate();
    }

    private void validateInternal(){
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
        if (errorMessage != null && pattern != null)
            drawPatternError = afterFirstInteraction && !pattern.matcher(s).matches();
        if (matchingView != 0) {
            View view = getRootView().findViewById(matchingView);
            if (view instanceof TextView) {
                TextView matchingTextView = (TextView) view;
                if (afterFirstInteraction && !matchingTextView.getText().toString().equals(getText().toString()))
                    drawMatchingViewError = true;
            }
        }
        drawError = drawPatternError | drawMatchingViewError;

        counterError = afterFirstInteraction && (minCharacters > 0 && s.length() < minCharacters || maxCharacters < Integer.MAX_VALUE && s.length() > maxCharacters);
        labelPaint.setColor(drawError | counterError ? errorColor : tint.getColorForState(new int[]{android.R.attr.state_focused}, disabledColor));
        counterPaint.setColor(drawError | counterError ? errorColor : getHintTextColors().getColorForState(new int[]{android.R.attr.state_focused}, disabledColor));
        if (showFloatingLabel)
            animateFloatingLabel(isFocused() && s.length() > 0);
    }

    private void validateInternalEvent() {
        validateInternal();
        fireOnValidateEvent();
        postInvalidate();
    }

    public void setOnValidateListener(OnValidateListener listener){
        this.validateListener = listener;
    }

    private void fireOnValidateEvent(){
        if(validateListener!=null)
            validateListener.onValidate(isValid());
    }

    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
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
                }
            }
            appearance.recycle();
        }
    }

    @Override
    public void setError(CharSequence text) {
        if (text == null) {
            drawError = false;
            errorMessage = null;
        } else {
            errorMessage = text.toString();
            drawError = true;
        }
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        this.setError(error);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        if (isInEditMode())
            return;

        int paddingTop = getPaddingTop() + internalPaddingTop;
        int paddingBottom = getPaddingBottom() + internalPaddingBottom;

        if (isFocused() && isEnabled()) {
            paint.setStrokeWidth(2 * getResources().getDimension(R.dimen.carbon_1dip));
        } else {
            paint.setStrokeWidth(getResources().getDimension(R.dimen.carbon_1dip));
        }
        if (drawDivider) {
            if (isEnabled()) {
                paint.setColor(drawError || counterError ? errorColor : tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
                paint.setShader(null);
                canvas.drawLine(getPaddingLeft(), getHeight() - paddingBottom + DIVIDER_PADDING, getWidth() - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING, paint);
            } else {
                Matrix matrix = new Matrix();
                matrix.postTranslate(0, getHeight() - paddingBottom + DIVIDER_PADDING - paint.getStrokeWidth() / 2.0f);
                dashPathShader.setLocalMatrix(matrix);
                paint.setShader(dashPathShader);
                canvas.drawRect(getPaddingLeft(), getHeight() - paddingBottom + DIVIDER_PADDING - paint.getStrokeWidth() / 2.0f,
                        getWidth() - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING + paint.getStrokeWidth() / 2.0f, paint);
            }
        }

        if (!isEnabled())
            return;

        if (drawError)
            canvas.drawText(errorMessage, getPaddingLeft(), getHeight() - paddingBottom + DIVIDER_PADDING + PADDING_ERROR + labelPaint.getTextSize(), errorPaint);

        if (getHint() != null && showFloatingLabel) {
            String label = getHint().toString();
            labelPaint.setAlpha((int) (255 * labelFrac));
            canvas.drawText(label, getPaddingLeft(), paddingTop + labelPaint.getTextSize() * (1 - labelFrac) - PADDING_LABEL, labelPaint);
        }

        int length = getText().length();
        if (minCharacters > 0 && maxCharacters < Integer.MAX_VALUE) {
            String text = length + " / " + minCharacters + "-" + maxCharacters;
            canvas.drawText(text, getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING + PADDING_ERROR + labelPaint.getTextSize(), counterPaint);
        } else if (minCharacters > 0) {
            String text = length + " / " + minCharacters + "+";
            canvas.drawText(text, getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING + PADDING_ERROR + labelPaint.getTextSize(), counterPaint);
        } else if (maxCharacters < Integer.MAX_VALUE) {
            String text = length + " / " + maxCharacters;
            canvas.drawText(text, getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - paddingBottom + DIVIDER_PADDING + PADDING_ERROR + labelPaint.getTextSize(), counterPaint);
        }

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Over)
            rippleDrawable.draw(canvas);
    }

    public boolean isFloatingLabelEnabled() {
        return showFloatingLabel;
    }

    public void setFloatingLabelEnabled(boolean showFloatingLabel) {
        this.showFloatingLabel = showFloatingLabel;
    }

    public boolean isValid() {
        return !((pattern != null || matchingView != 0 || drawError) && errorMessage != null || minCharacters > 0 || maxCharacters < Integer.MAX_VALUE);
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
        animateFloatingLabel(focused && getText().length() > 0);
        if (!focused) {
            afterFirstInteraction = true;
            validateInternalEvent();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed)
            return;

        if (getWidth() == 0 || getHeight() == 0)
            return;

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            if (showFloatingLabel)
                internalPaddingTop = (int) (PADDING_LABEL + labelPaint.getTextSize());
            if (!isValid()) {
                internalPaddingBottom = (int) (errorPaint.getTextSize());
                if (!drawDivider)
                    internalPaddingBottom += PADDING_ERROR;
            }
            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), paddingBottom);
        }
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

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top + internalPaddingTop, right, bottom + internalPaddingBottom);
    }


    // -------------------------------
    // popup
    // -------------------------------


    EditorMenu popupMenu;
    private boolean isShowingPopup = false;
    WindowManager brokenWindowManager = new WindowManager() {
        @Override
        public Display getDefaultDisplay() {
            return null;
        }

        @Override
        public void removeViewImmediate(View view) {

        }

        @Override
        public void addView(View view, ViewGroup.LayoutParams params) {
            final WindowManager.LayoutParams wparams
                    = (WindowManager.LayoutParams) params;
            view.setLayoutParams(wparams);
        }

        @Override
        public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
            final WindowManager.LayoutParams wparams
                    = (WindowManager.LayoutParams) params;
            view.setLayoutParams(wparams);
        }

        @Override
        public void removeView(View view) {

        }
    };

    @Override
    public int getSelectionStart() {
        try {
            if (brokenWindowManager != null) {
                Field ccmf = android.widget.TextView.class.getDeclaredField("mCursorControllerMenu");
                ccmf.setAccessible(true);
                Object ccm = ccmf.get(this);
                {
                    Field pwf = ccm.getClass().getDeclaredField("mPopupWindow");
                    pwf.setAccessible(true);
                    PopupWindow pw = (PopupWindow) pwf.get(ccm);
                    Field wmf = pw.getClass().getDeclaredField("mWindowManager");
                    wmf.setAccessible(true);
                    wmf.set(pw, brokenWindowManager);
                }
                {
                    Field pwf = ccm.getClass().getDeclaredField("mPopupWindowArrowDown");
                    pwf.setAccessible(true);
                    PopupWindow pw = (PopupWindow) pwf.get(ccm);
                    Field wmf = pw.getClass().getDeclaredField("mWindowManager");
                    wmf.setAccessible(true);
                    wmf.set(pw, brokenWindowManager);
                }
                {
                    Field pwf = ccm.getClass().getDeclaredField("mPopupWindowArrowUp");
                    pwf.setAccessible(true);
                    PopupWindow pw = (PopupWindow) pwf.get(ccm);
                    Field wmf = pw.getClass().getDeclaredField("mWindowManager");
                    wmf.setAccessible(true);
                    wmf.set(pw, brokenWindowManager);
                }
                brokenWindowManager = null;
            }
        } catch (Exception e) {

        }
        return super.getSelectionStart();
    }

    private void initSelectionHandle() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            try {
                final Field fEditor = android.widget.TextView.class.getDeclaredField("mEditor");
                fEditor.setAccessible(true);
                final Object editor = fEditor.get(this);

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
                leftHandle.setAlpha(127);
                fSelectHandleLeft.set(this, leftHandle);

                VectorDrawable rightHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_right);
                rightHandle.setTint(Carbon.getThemeColor(getContext(), R.attr.colorAccent));
                rightHandle.setAlpha(127);
                fSelectHandleRight.set(this, rightHandle);

                VectorDrawable middleHandle = new VectorDrawable(getResources(), R.raw.carbon_selecthandle_middle);
                middleHandle.setTint(Carbon.getThemeColor(getContext(), R.attr.colorAccent));
                middleHandle.setAlpha(127);
                fSelectHandleCenter.set(this, middleHandle);
            } catch (final Exception ignored) {
            }

        }
    }

    private void initActionModeCallback() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    TypedValue outValue = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.carbon_editMenuTheme, outValue, true);
                    int theme = outValue.resourceId;
                    Context themedContext = new ContextThemeWrapper(getContext(), theme);

                    popupMenu = new EditorMenu(themedContext);
                    popupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            isShowingPopup = false;
                        }
                    });
                    popupMenu.initCopy(menu.findItem(ID_COPY));
                    popupMenu.initCut(menu.findItem(ID_CUT));
                    popupMenu.initPaste(menu.findItem(ID_PASTE));
                    popupMenu.initSelectAll(menu.findItem(ID_SELECT_ALL));
                    if (popupMenu.hasVisibleItems()) {
                        popupMenu.show(EditText.this);
                        isShowingPopup = true;
                    }
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }
            });
        }
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.carbon_editMenuTheme, outValue, true);
        int theme = outValue.resourceId;
        Context themedContext = new ContextThemeWrapper(getContext(), theme);

        popupMenu = new EditorMenu(themedContext);
        popupMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isShowingPopup = false;
            }
        });

        super.onCreateContextMenu(menu);
        popupMenu.initCopy(menu.findItem(ID_COPY));
        popupMenu.initCut(menu.findItem(ID_CUT));
        popupMenu.initPaste(menu.findItem(ID_PASTE));
        popupMenu.initSelectAll(menu.findItem(ID_SELECT_ALL));
        if (popupMenu.hasVisibleItems()) {
            popupMenu.show(EditText.this);
            isShowingPopup = true;
        }
        menu.clear();
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);

        if (popupMenu != null)
            popupMenu.update();

        return result;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isShowingPopup)
            popupMenu.showImmediate(EditText.this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (isShowingPopup)
            popupMenu.dismissImmediate();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.stateToSave = this.isShowingPopup ? 1 : 0;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.isShowingPopup = ss.stateToSave > 0;
    }

    static class SavedState extends BaseSavedState {
        int stateToSave;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.stateToSave = in.readInt();
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }


    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;
    private EmptyDrawable emptyBackground = new EmptyDrawable();

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

    public void setRippleDrawable(RippleDrawable newRipple) {
        if (rippleDrawable != null) {
            rippleDrawable.setCallback(null);
            if (rippleDrawable.getStyle() == RippleDrawable.Style.Background)
                super.setBackgroundDrawable(rippleDrawable.getBackground() == null ? emptyBackground : rippleDrawable.getBackground());
        }

        if (newRipple != null) {
            newRipple.setCallback(this);
            if (newRipple.getStyle() == RippleDrawable.Style.Background) {
                super.setBackgroundDrawable((Drawable) newRipple);
            }
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
    }

    @Override
    public void invalidate(@NonNull Rect dirty) {
        super.invalidate(dirty);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate(dirty);
    }

    @Override
    public void invalidate(int l, int t, int r, int b) {
        super.invalidate(l, t, r, b);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate(l, t, r, b);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).invalidate();
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds) {
        super.postInvalidateDelayed(delayMilliseconds);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds);
    }

    @Override
    public void postInvalidateDelayed(long delayMilliseconds, int left, int top, int right, int bottom) {
        super.postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidateDelayed(delayMilliseconds, left, top, right, bottom);
    }

    @Override
    public void postInvalidate() {
        super.postInvalidate();
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void postInvalidate(int left, int top, int right, int bottom) {
        super.postInvalidate(left, top, right, bottom);
        if (getParent() == null || !(getParent() instanceof View))
            return;

        if (rippleDrawable != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
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
        super.setBackgroundDrawable(background == null ? emptyBackground : background);
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

    private List<StateAnimator> stateAnimators = new ArrayList<>();

    public void removeStateAnimator(StateAnimator animator) {
        stateAnimators.remove(animator);
    }

    public void addStateAnimator(StateAnimator animator) {
        this.stateAnimators.add(animator);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (rippleDrawable != null && rippleDrawable.getStyle() != RippleDrawable.Style.Background)
            rippleDrawable.setState(getDrawableState());
        if (stateAnimators != null)
            for (StateAnimator animator : stateAnimators)
                animator.stateChanged(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;
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

    @Override
    public void setTint(ColorStateList list) {
        if (list == null) {
            tint = null;
            drawDivider = false;
        } else {
            this.tint = list;
            drawDivider = true;
        }
    }

    @Override
    public void setTint(int color) {
        drawDivider = true;
        if (color == 0) {
            setTint(new ControlFocusedColorStateList(getContext()));
        } else {
            setTint(ColorStateList.valueOf(color));
        }
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }
}
