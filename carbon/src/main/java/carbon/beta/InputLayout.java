package carbon.beta;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import carbon.Carbon;
import carbon.R;
import carbon.internal.SimpleTextWatcher;
import carbon.internal.TypefaceUtils;
import carbon.widget.InputView;
import carbon.widget.RelativeLayout;

/**
 * Created by Marcin on 2016-07-25.
 */

public class InputLayout extends RelativeLayout {

    public enum LabelStyle {
        Floating, Persistent
    }

    private TextView errorTextView;

    private boolean afterFirstInteraction = false;

    boolean required = false;
    private int minCharacters;
    private int maxCharacters = Integer.MAX_VALUE;
    private TextView counterTextView;

    private LabelStyle labelStyle;
    private TextView labelTextView;

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
        counterTextView = (TextView) findViewById(R.id.carbon_counter);
        labelTextView = (TextView) findViewById(R.id.carbon_label);

        if (isInEditMode())
            return;

        if (attrs != null) {
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
            setRequired(a.getBoolean(R.styleable.EditText_carbon_required, false));

            a.recycle();
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof android.widget.TextView
                && child.getId() != R.id.carbon_label
                && child.getId() != R.id.carbon_error
                && child.getId() != R.id.carbon_counter) {
            params = setTextView(child, (android.widget.RelativeLayout.LayoutParams) params);
            super.addView(child, 1, params);
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    private ViewGroup.LayoutParams setTextView(View child, android.widget.RelativeLayout.LayoutParams params) {
        if (child.getId() == NO_ID)
            child.setId(R.id.carbon_input);
        params.addRule(BELOW, R.id.carbon_label);
        android.widget.RelativeLayout.LayoutParams errorTextViewLayoutParams = (android.widget.RelativeLayout.LayoutParams) errorTextView.getLayoutParams();
        errorTextViewLayoutParams.addRule(BELOW, child.getId());
        android.widget.RelativeLayout.LayoutParams counterTextViewLayoutParams = (android.widget.RelativeLayout.LayoutParams) counterTextView.getLayoutParams();
        counterTextViewLayoutParams.addRule(BELOW, child.getId());
        child.setOnFocusChangeListener((view, b) -> {
            if (labelStyle == LabelStyle.Persistent || b) {
                labelTextView.setVisibility(VISIBLE);
            } else {
                labelTextView.setVisibility(INVISIBLE);
            }
        });
        if (child instanceof InputView) {
            InputView inputView = (InputView) child;
            inputView.addOnValidateListener(this::setErrorVisible);
        }
        if (child instanceof TextView) {
            final TextView textView = (TextView) child;
            if (labelTextView.getText().length() == 0)
                labelTextView.setText(textView.getHint());
            textView.addTextChangedListener(new SimpleTextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    afterFirstInteraction = true;
                    //validateInternalEvent();
                    /*try {
                        int start = textView.getSelectionStart() - 1;
                        char c;
                        StringBuilder builder = new StringBuilder();
                        while (start >= 0 && Character.isLetterOrDigit(c = s.charAt(start--))) {
                            builder.insert(0, c);
                        }
                    } catch (Exception e) {
                    }*/

                    boolean counterError = afterFirstInteraction && (minCharacters > 0 && s.length() < minCharacters || maxCharacters < Integer.MAX_VALUE && s.length() > maxCharacters);
                    boolean requiredError = required && s.length() == 0;
                }
            });
        }
        return params;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void setError(String text) {
        errorTextView.setText(text);
    }

    public void setErrorVisible(boolean errorVisible) {
        errorTextView.setVisibility(errorVisible ? VISIBLE : GONE);
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
    }

    public LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(LabelStyle labelStyle) {
        this.labelStyle = labelStyle;
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

}
