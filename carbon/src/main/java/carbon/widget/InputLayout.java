package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.drawable.DefaultAccentColorStateList;
import carbon.drawable.DefaultTextSecondaryColorStateList;
import carbon.internal.TypefaceUtils;
import carbon.view.InputView;

public class InputLayout extends RelativeLayout {

    private int gravity;

    public enum LabelStyle {
        Floating, Persistent, Hint, IfNotEmpty
    }

    public enum ErrorMode {
        WhenInvalid, Always, Never
    }

    public enum ActionButton {
        None, Clear, ShowPassword, VoiceInput
    }

    private boolean inDrawableStateChanged = false;

    private TextView errorTextView;
    ErrorMode errorMode = ErrorMode.WhenInvalid;

    private String label;
    private TextView counterTextView;

    private LabelStyle labelStyle;
    private TextView labelTextView;

    private ActionButton actionButton = ActionButton.None;
    private ImageView clearImageView;
    private ImageView showPasswordImageView;
    private ImageView voiceInputImageView;

    private ViewGroup container;
    private View child;

    TransformationMethod transformationMethod;

    public InputLayout(Context context) {
        super(context);
        initInputLayout(null, R.attr.carbon_inputLayoutStyle);
    }

    public InputLayout(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.InputLayout, R.attr.carbon_inputLayoutStyle, R.styleable.InputLayout_carbon_theme), attrs);
        initInputLayout(attrs, R.attr.carbon_inputLayoutStyle);
    }

    public InputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.InputLayout, defStyleAttr, R.styleable.InputLayout_carbon_theme), attrs, defStyleAttr);
        initInputLayout(attrs, defStyleAttr);
    }

    public InputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.InputLayout, defStyleAttr, R.styleable.InputLayout_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initInputLayout(attrs, defStyleAttr);
    }

    private void initInputLayout(AttributeSet attrs, int defStyleAttr) {
        View.inflate(getContext(), R.layout.carbon_inputlayout, this);

        errorTextView = findViewById(R.id.carbon_error);
        errorTextView.setTextColor(new DefaultAccentColorStateList(getContext()));
        errorTextView.setValid(false);
        counterTextView = findViewById(R.id.carbon_counter);
        counterTextView.setTextColor(new DefaultTextSecondaryColorStateList(getContext()));
        labelTextView = findViewById(R.id.carbon_label);
        labelTextView.setTextColor(new DefaultAccentColorStateList(getContext()));
        labelTextView.setGravity(gravity);
        clearImageView = findViewById(R.id.carbon_clear);
        showPasswordImageView = findViewById(R.id.carbon_showPassword);
        voiceInputImageView = findViewById(R.id.carbon_voiceInput);
        container = findViewById(R.id.carbon_inputLayoutContainer);

        setAddStatesFromChildren(true);

        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.InputLayout, defStyleAttr, 0);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (!isInEditMode() && attr == R.styleable.InputLayout_carbon_errorFontPath) {
                String path = a.getString(attr);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                setErrorTypeface(typeface);
            } else if (attr == R.styleable.InputLayout_carbon_errorTextSize) {
                setErrorTextSize(a.getDimension(attr, 0));
            } else if (attr == R.styleable.InputLayout_carbon_errorFontFamily) {
                int textStyle = a.getInt(R.styleable.InputLayout_android_textStyle, 0);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), a.getString(attr), textStyle);
                setErrorTypeface(typeface);
            } else if (!isInEditMode() && attr == R.styleable.InputLayout_carbon_labelFontPath) {
                String path = a.getString(attr);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                setLabelTypeface(typeface);
            } else if (attr == R.styleable.InputLayout_carbon_counterTextSize) {
                setCounterTextSize(a.getDimension(attr, 0));
            } else if (attr == R.styleable.InputLayout_carbon_labelFontFamily) {
                int textStyle = a.getInt(R.styleable.InputLayout_android_textStyle, 0);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), a.getString(attr), textStyle);
                setLabelTypeface(typeface);
            } else if (!isInEditMode() && attr == R.styleable.InputLayout_carbon_counterFontPath) {
                String path = a.getString(attr);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                setCounterTypeface(typeface);
            } else if (attr == R.styleable.InputLayout_carbon_labelTextSize) {
                setLabelTextSize(a.getDimension(attr, 0));
            } else if (attr == R.styleable.InputLayout_carbon_counterFontFamily) {
                int textStyle = a.getInt(R.styleable.InputLayout_android_textStyle, 0);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), a.getString(attr), textStyle);
                setCounterTypeface(typeface);
            }
        }

        String error = a.getString(R.styleable.InputLayout_carbon_error);
        setError(error == null ? a.getString(R.styleable.InputLayout_carbon_errorMessage) : error);
        setErrorMode(ErrorMode.values()[a.getInt(R.styleable.InputLayout_carbon_errorMode, ErrorMode.WhenInvalid.ordinal())]);

        setLabelStyle(LabelStyle.values()[a.getInt(R.styleable.InputLayout_carbon_labelStyle, LabelStyle.Floating.ordinal())]);
        setLabel(a.getString(R.styleable.InputLayout_carbon_label));
        setActionButton(ActionButton.values()[a.getInt(R.styleable.InputLayout_carbon_actionButton, 0)]);
        setGravity(a.getInt(R.styleable.InputLayout_android_gravity, Gravity.START));

        a.recycle();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!"inputLayout".equals(child.getTag())) {
            params = setInputChild(child, (android.widget.RelativeLayout.LayoutParams) params);
            container.addView(child, 1, params);
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    private ViewGroup.LayoutParams setInputChild(View child, android.widget.RelativeLayout.LayoutParams params) {
        this.child = child;

        if (child.getId() == NO_ID)
            child.setId(R.id.carbon_input);
        params.addRule(BELOW, R.id.carbon_label);

        if (child instanceof EditText) {
            final EditText editText = (EditText) child;
            if (labelTextView.getText().length() == 0)
                setLabel(editText.getHint());
            editText.addOnValidateListener(valid -> {
                updateError(valid);
                updateCounter(editText);
            });
            showPasswordImageView.setOnTouchListener((view, motionEvent) -> {
                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    transformationMethod = editText.getTransformationMethod();
                    editText.setTransformationMethod(null);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    editText.setTransformationMethod(transformationMethod);
                }
                editText.setSelection(selectionStart, selectionEnd);
                return false;
            });
            clearImageView.setOnClickListener(view -> editText.setText(""));

            labelTextView.setInAnimator(null);
            labelTextView.setOutAnimator(null);
            setLabel(label);
            errorTextView.setInAnimator(null);
            errorTextView.setOutAnimator(null);
            updateError(editText.isValid());
            updateHint(editText);
            updateCounter(editText);
            labelTextView.setInAnimator(AnimUtils.getFlyInAnimator());
            labelTextView.setOutAnimator(AnimUtils.getFadeOutAnimator());
            errorTextView.setInAnimator(AnimUtils.getFadeInAnimator());
            errorTextView.setOutAnimator(AnimUtils.getFadeOutAnimator());
        } else if (child instanceof InputView) {
            InputView inputView = (InputView) child;
            inputView.addOnValidateListener(this::updateError);

            labelTextView.setInAnimator(null);
            labelTextView.setOutAnimator(null);
            errorTextView.setInAnimator(null);
            errorTextView.setOutAnimator(null);
            updateError(inputView.isValid());
            updateHint(child);
            labelTextView.setInAnimator(AnimUtils.getFlyInAnimator());
            labelTextView.setOutAnimator(AnimUtils.getFadeOutAnimator());
            errorTextView.setInAnimator(AnimUtils.getFadeInAnimator());
            errorTextView.setOutAnimator(AnimUtils.getFadeOutAnimator());
        }

        if (actionButton != ActionButton.None) {
            int buttonPadding = getResources().getDimensionPixelSize(R.dimen.carbon_padding) + getResources().getDimensionPixelSize(R.dimen.carbon_iconSize);
            child.setPadding(child.getPaddingLeft(), child.getPaddingTop(), child.getPaddingRight() + buttonPadding, child.getPaddingBottom());
        }

        return params;
    }

    @Override
    protected void drawableStateChanged() {
        if (inDrawableStateChanged)
            return;

        inDrawableStateChanged = true;
        super.drawableStateChanged();
        updateHint(child);
        inDrawableStateChanged = false;
    }

    private void updateError(boolean valid) {
        labelTextView.setValid(valid);

        errorTextView.animateVisibility(errorMode == ErrorMode.Always || errorMode == ErrorMode.WhenInvalid && !valid ? VISIBLE : errorMode == ErrorMode.Never ? GONE : INVISIBLE);
    }

    private void updateCounter(EditText editText) {
        int minCharacters = editText.getMinCharacters();
        int maxCharacters = editText.getMaxCharacters();

        counterTextView.setValid(!editText.isValid());

        if (minCharacters > 0 && maxCharacters < Integer.MAX_VALUE) {
            counterTextView.setVisibility(VISIBLE);
            counterTextView.setText(editText.length() + " / " + minCharacters + "-" + maxCharacters);
        } else if (minCharacters > 0) {
            counterTextView.setVisibility(VISIBLE);
            counterTextView.setText(editText.length() + " / " + minCharacters + "+");
        } else if (maxCharacters < Integer.MAX_VALUE) {
            counterTextView.setVisibility(VISIBLE);
            counterTextView.setText(editText.length() + " / " + maxCharacters);
        } else {
            counterTextView.setVisibility(GONE);
        }
    }

    private void updateHint(View child) {
        if (child == null) {
            labelTextView.setVisibility(GONE);
            return;
        }
        if (labelStyle == LabelStyle.Persistent || labelStyle == LabelStyle.Floating && child.isFocused() ||
                labelStyle == LabelStyle.IfNotEmpty && (child.isFocused() || child instanceof android.widget.TextView && ((android.widget.TextView) child).getText().length() > 0)) {
            labelTextView.animateVisibility(VISIBLE);
            if (child instanceof EditText)
                ((EditText) child).setHint(null);
        } else if (labelStyle != LabelStyle.Hint) {
            labelTextView.animateVisibility(INVISIBLE);
            if (child instanceof EditText)
                ((EditText) child).setHint(label + (((EditText) child).isRequired() ? " *" : ""));
        } else {
            labelTextView.setVisibility(GONE);
        }
    }

    public void setError(String text) {
        errorTextView.setText(text);
    }

    /**
     * Changes error message mode. Nothing will be shown until error text is set. Can be set from
     * xml using `carbon_errorMode`.
     *
     * @param errorMode error message mode
     */
    public void setErrorMode(@NonNull ErrorMode errorMode) {
        this.errorMode = errorMode;
        errorTextView.setVisibility(errorMode == ErrorMode.WhenInvalid ? INVISIBLE : errorMode == ErrorMode.Always ? VISIBLE : GONE);
    }

    /**
     * Deprecated use {@link carbon.widget.InputLayout.setErrorMode} instead.
     *
     * @param errorVisible true to make error messages show when the underlying InputView is
     *                     invalid
     */
    @Deprecated
    public void setErrorEnabled(boolean errorVisible) {
        setErrorMode(errorVisible ? ErrorMode.WhenInvalid : ErrorMode.Never);
    }

    public float getErrorTextSize() {
        return errorTextView.getTextSize();
    }

    public void setErrorTextSize(float errorTextSize) {
        errorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorTextSize);
    }

    public float getCounterTextSize() {
        return counterTextView.getTextSize();
    }

    public void setCounterTextSize(float counterTextSize) {
        counterTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, counterTextSize);
    }

    public float getLabelTextSize() {
        return labelTextView.getTextSize();
    }

    public void setLabelTextSize(float labelTextSize) {
        labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
    }

    public Typeface getErrorTypeface() {
        return errorTextView.getTypeface();
    }

    public void setErrorTypeface(Typeface errorTypeface) {
        errorTextView.setTypeface(errorTypeface);
    }

    public Typeface getCounterTypeface() {
        return counterTextView.getTypeface();
    }

    public void setCounterTypeface(Typeface counterTypeface) {
        counterTextView.setTypeface(counterTypeface);
    }

    public Typeface getLabelTypeface() {
        return labelTextView.getTypeface();
    }

    public void setLabelTypeface(Typeface labelTypeface) {
        labelTextView.setTypeface(labelTypeface);
    }

    public String getLabel() {
        return labelTextView.getText().toString();
    }

    public void setLabel(CharSequence label) {
        setLabel(label.toString());
    }

    public void setLabel(String label) {
        this.label = label;
        labelTextView.setText(label + (child instanceof EditText && ((EditText) child).isRequired() ? " *" : ""));
        if (child != null)
            updateHint(child);
    }

    public LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
        if (child != null)
            updateHint(child);
    }

    public ActionButton getActionButton() {
        return actionButton;
    }

    public void setActionButton(ActionButton actionButton) {
        int paddingRight = 0;
        if (child != null) {
            paddingRight = child.getPaddingRight();
            if (this.actionButton != ActionButton.None)
                paddingRight -= getResources().getDimensionPixelSize(R.dimen.carbon_padding) + getResources().getDimensionPixelSize(R.dimen.carbon_iconSize);
        }

        this.actionButton = actionButton;
        clearImageView.setVisibility(actionButton == ActionButton.Clear ? VISIBLE : GONE);
        showPasswordImageView.setVisibility(actionButton == ActionButton.ShowPassword ? VISIBLE : GONE);
        voiceInputImageView.setVisibility(actionButton == ActionButton.VoiceInput ? VISIBLE : GONE);

        if (actionButton != null)
            paddingRight += getResources().getDimensionPixelSize(R.dimen.carbon_padding) + getResources().getDimensionPixelSize(R.dimen.carbon_iconSize);
        if (child != null)
            child.setPadding(child.getPaddingLeft(), child.getPaddingTop(), paddingRight, child.getPaddingBottom());
    }

    @Override
    public int getBaseline() {
        if (child == null)
            return super.getBaseline();
        return (labelTextView.getVisibility() != GONE ? labelTextView.getMeasuredHeight() + 1 : 0) + child.getBaseline();
    }

    @Override
    public void setGravity(int gravity) {
        this.gravity = gravity;
        super.setGravity(gravity);
        if (labelTextView != null)
            labelTextView.setGravity(gravity);
    }
}
