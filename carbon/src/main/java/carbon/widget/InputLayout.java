package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.view.InputView;

public class InputLayout extends RelativeLayout {

    private int gravity;

    public enum LabelMode {
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

    private LabelMode labelMode;
    private TextView labelTextView;

    private ActionButton actionButton = ActionButton.None;
    private ImageView clearImageView;
    private ImageView showPasswordImageView;
    private ImageView voiceInputImageView;

    private ViewGroup container;
    private View child;

    TransformationMethod transformationMethod;

    public InputLayout(Context context) {
        super(context, null, R.attr.carbon_inputLayoutStyle);
        initInputLayout(null, R.attr.carbon_inputLayoutStyle, R.style.carbon_InputLayout);
    }

    public InputLayout(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_inputLayoutStyle);
        initInputLayout(attrs, R.attr.carbon_inputLayoutStyle, R.style.carbon_InputLayout);
    }

    public InputLayout(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInputLayout(attrs, defStyleAttr, R.style.carbon_InputLayout);
    }

    public InputLayout(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInputLayout(attrs, defStyleAttr, defStyleRes);
    }

    private void initInputLayout(AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        View.inflate(getContext(), R.layout.carbon_inputlayout, this);

        errorTextView = findViewById(R.id.carbon_error);
        errorTextView.setValid(false);
        counterTextView = findViewById(R.id.carbon_counter);
        labelTextView = findViewById(R.id.carbon_label);
        labelTextView.setGravity(gravity);
        clearImageView = findViewById(R.id.carbon_clear);
        showPasswordImageView = findViewById(R.id.carbon_showPassword);
        voiceInputImageView = findViewById(R.id.carbon_voiceInput);
        container = findViewById(R.id.carbon_inputLayoutContainer);

        setAddStatesFromChildren(true);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.InputLayout, defStyleAttr, defStyleRes);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (!isInEditMode() && attr == R.styleable.InputLayout_carbon_errorTextAppearance) {
                Carbon.setTextAppearance(errorTextView, attr, false, true);
            } else if (attr == R.styleable.InputLayout_carbon_counterTextAppearance) {
                Carbon.setTextAppearance(counterTextView, attr, false, true);
            } else if (attr == R.styleable.InputLayout_carbon_labelTextAppearance) {
                Carbon.setTextAppearance(labelTextView, attr, false, true);
            }
        }

        setError(a.getString(R.styleable.InputLayout_carbon_error));
        setErrorMode(ErrorMode.values()[a.getInt(R.styleable.InputLayout_carbon_errorMode, ErrorMode.WhenInvalid.ordinal())]);

        setLabelMode(LabelMode.values()[a.getInt(R.styleable.InputLayout_carbon_labelMode, LabelMode.Floating.ordinal())]);
        setLabel(a.getString(R.styleable.InputLayout_carbon_label));
        setActionButton(ActionButton.values()[a.getInt(R.styleable.InputLayout_carbon_actionButton, 0)]);
        setGravity(a.getInt(R.styleable.InputLayout_android_gravity, Gravity.START));

        a.recycle();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (!"inputLayout".equals(child.getTag())) {
            setInputChild(child);
            container.addView(child, index == -1 ? -1 : index + 1, new LinearLayout.LayoutParams(params));
        } else {
            // Carry on adding the View...
            super.addView(child, index, params);
        }
    }

    private void setInputChild(View child) {
        this.child = child;

        if (child.getId() == NO_ID)
            child.setId(R.id.carbon_input);

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

        counterTextView.setValid(editText.isValid());

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
        if (labelTextView == null)
            return;
        if (child == null) {
            labelTextView.setVisibility(GONE);
            return;
        }
        if (labelMode == LabelMode.Persistent || labelMode == LabelMode.Floating && child.isFocused() ||
                labelMode == LabelMode.IfNotEmpty && (child.isFocused() || child instanceof android.widget.TextView && ((android.widget.TextView) child).getText().length() > 0)) {
            labelTextView.animateVisibility(VISIBLE);
            if (child instanceof EditText)
                ((EditText) child).setHint(null);
        } else if (labelMode != LabelMode.Hint) {
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

    public void setErrorTextAppearance(int resId) {
        Carbon.setTextAppearance(errorTextView, resId, false, true);
    }

    public void setCounterTextAppearance(int resId) {
        Carbon.setTextAppearance(counterTextView, resId, false, true);
    }

    public void setLabelTextAppearance(int resId) {
        Carbon.setTextAppearance(labelTextView, resId, false, true);
    }

    public String getLabel() {
        return labelTextView.getText().toString();
    }

    public void setLabel(CharSequence label) {
        setLabel(String.valueOf(label));
    }

    public void setLabel(String label) {
        this.label = label;
        labelTextView.setText((label != null ? label : "") + (child instanceof EditText && ((EditText) child).isRequired() ? " *" : ""));
        if (child != null)
            updateHint(child);
    }

    public LabelMode getLabelMode() {
        return labelMode;
    }

    public void setLabelMode(LabelMode labelMode) {
        this.labelMode = labelMode;
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
