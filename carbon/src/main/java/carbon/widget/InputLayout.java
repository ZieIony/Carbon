package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import carbon.Carbon;
import carbon.R;
import carbon.drawable.DefaultAccentColorStateList;
import carbon.drawable.DefaultTextSecondaryColorStateList;
import carbon.internal.TypefaceUtils;

public class InputLayout extends RelativeLayout {

    public enum LabelStyle {
        Floating, Persistent, Hint
    }

    private TextView errorTextView;

    boolean required = false;
    private int minCharacters;
    private int maxCharacters = Integer.MAX_VALUE;
    private TextView counterTextView;

    private LabelStyle labelStyle;
    private TextView labelTextView;

    private ImageView clearImageView;
    private ImageView showPasswordImageView;

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
        errorTextView = (TextView) findViewById(R.id.carbon_error);
        errorTextView.setTextColor(new DefaultAccentColorStateList(getContext()));
        errorTextView.setValid(false);
        counterTextView = (TextView) findViewById(R.id.carbon_counter);
        counterTextView.setTextColor(new DefaultTextSecondaryColorStateList(getContext()));
        labelTextView = (TextView) findViewById(R.id.carbon_label);
        labelTextView .setTextColor(new DefaultAccentColorStateList(getContext()));
        clearImageView = (ImageView) findViewById(R.id.carbon_clear);
        showPasswordImageView = (ImageView) findViewById(R.id.carbon_showPassword);

        if (isInEditMode())
            return;

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

        if (!isInEditMode())
            setError(a.getString(R.styleable.InputLayout_carbon_errorMessage));

        setMinCharacters(a.getInt(R.styleable.InputLayout_carbon_minCharacters, 0));
        setMaxCharacters(a.getInt(R.styleable.InputLayout_carbon_maxCharacters, Integer.MAX_VALUE));
        setLabelStyle(LabelStyle.values()[a.getInt(R.styleable.InputLayout_carbon_labelStyle, LabelStyle.Floating.ordinal())]);
        setLabel(a.getString(R.styleable.InputLayout_carbon_label));
        setRequired(a.getBoolean(R.styleable.InputLayout_carbon_required, false));
        setShowPasswordButtonEnabled(a.getBoolean(R.styleable.InputLayout_carbon_showPassword, false));
        setClearButtonEnabled(a.getBoolean(R.styleable.InputLayout_carbon_showClear, false));
        setGravity(a.getInt(R.styleable.InputLayout_android_gravity, Gravity.START | Gravity.LEFT));

        a.recycle();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!"inputLayout".equals(child.getTag())) {
            params = setTextView(child, (android.widget.RelativeLayout.LayoutParams) params);
            super.addView(child, 1, params);
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    private ViewGroup.LayoutParams setTextView(View child, android.widget.RelativeLayout.LayoutParams params) {
        this.child = child;

        if (child.getId() == NO_ID)
            child.setId(R.id.carbon_input);
        params.addRule(BELOW, R.id.carbon_label);

        android.widget.RelativeLayout.LayoutParams errorTextViewLayoutParams = (android.widget.RelativeLayout.LayoutParams) errorTextView.getLayoutParams();
        errorTextViewLayoutParams.addRule(Build.VERSION.SDK_INT >= 17 ? ALIGN_START : ALIGN_LEFT, child.getId());
        errorTextViewLayoutParams.addRule(BELOW, child.getId());

        android.widget.RelativeLayout.LayoutParams counterTextViewLayoutParams = (android.widget.RelativeLayout.LayoutParams) counterTextView.getLayoutParams();
        counterTextViewLayoutParams.addRule(Build.VERSION.SDK_INT >= 17 ? ALIGN_END : ALIGN_RIGHT, child.getId());
        counterTextViewLayoutParams.addRule(BELOW, child.getId());

        android.widget.RelativeLayout.LayoutParams clearImageViewLayoutParams = (android.widget.RelativeLayout.LayoutParams) clearImageView.getLayoutParams();
        clearImageViewLayoutParams.addRule(Build.VERSION.SDK_INT >= 17 ? ALIGN_END : ALIGN_RIGHT, child.getId());
        clearImageViewLayoutParams.addRule(ALIGN_BASELINE, child.getId());

        android.widget.RelativeLayout.LayoutParams showPasswordImageViewLayoutParams = (android.widget.RelativeLayout.LayoutParams) showPasswordImageView.getLayoutParams();
        showPasswordImageViewLayoutParams.addRule(Build.VERSION.SDK_INT >= 17 ? ALIGN_END : ALIGN_RIGHT, child.getId());
        showPasswordImageViewLayoutParams.addRule(ALIGN_BASELINE, child.getId());

        child.setOnFocusChangeListener((view, b) -> {
            updateHint(child);
        });
        if (child instanceof EditText) {
            final EditText editText = (EditText) child;
            if (labelTextView.getText().length() == 0)
                labelTextView.setText(editText.getHint());
            editText.addOnValidateListener(valid -> {
                boolean counterError = (minCharacters > 0 && editText.length() < minCharacters || maxCharacters < Integer.MAX_VALUE && editText.length() > maxCharacters);
                boolean requiredError = required && editText.length() == 0;

                counterTextView.setValid(!counterError);
                updateHint(editText);
                updateCounter(editText);
                if (errorTextView.getVisibility() != GONE)
                    errorTextView.setVisibility(valid ? INVISIBLE : VISIBLE);
            });
            showPasswordImageView.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    transformationMethod = editText.getTransformationMethod();
                    editText.setTransformationMethod(null);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    editText.setTransformationMethod(transformationMethod);
                }
                return true;
            });
            clearImageView.setOnClickListener(view -> editText.setText(""));

            updateHint(editText);
            updateCounter(editText);
        } else if (child instanceof InputView) {
            InputView inputView = (InputView) child;
            inputView.addOnValidateListener(this::setErrorEnabled);
        }

        return params;
    }

    private void updateCounter(EditText editText) {
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
        if (labelStyle == LabelStyle.Persistent || labelStyle == LabelStyle.Floating && child.isFocused()) {
            labelTextView.setVisibility(VISIBLE);
            if (child instanceof EditText)
                ((EditText) child).setHint(null);
        } else if (labelStyle != LabelStyle.Hint) {
            labelTextView.setVisibility(INVISIBLE);
            if (child instanceof EditText)
                ((EditText) child).setHint(labelTextView.getText());
        } else {
            labelTextView.setVisibility(GONE);
        }
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setError(String text) {
        errorTextView.setText(text);
        setErrorEnabled(text != null);
    }

    public void setErrorEnabled(boolean errorVisible) {
        errorTextView.setVisibility(errorVisible ? INVISIBLE : GONE);
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

    public void setLabel(String label) {
        labelTextView.setText(label);
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

    public boolean isShowPasswordButtonEnabled() {
        return showPasswordImageView.getVisibility() == VISIBLE;
    }

    public void setShowPasswordButtonEnabled(boolean b) {
        showPasswordImageView.setVisibilityImmediate(b ? VISIBLE : GONE);
        if (b)
            setClearButtonEnabled(false);
    }

    public boolean isClearButtonEnabled() {
        return clearImageView.getVisibility() == VISIBLE;
    }

    public void setClearButtonEnabled(boolean b) {
        clearImageView.setVisibilityImmediate(b ? VISIBLE : GONE);
        if (b)
            setShowPasswordButtonEnabled(false);
    }

    @Override
    public int getBaseline() {
        return child == null ? super.getBaseline() : child.getTop() + child.getBaseline();
    }

    @Override
    public void setGravity(int gravity) {
        super.setGravity(gravity);
        labelTextView.setGravity(gravity);
    }
}
