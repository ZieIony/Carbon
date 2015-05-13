package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.StateAnimator;
import carbon.drawable.ControlFocusedColorStateList;
import carbon.internal.TypefaceUtils;

/**
 * Created by Marcin on 2015-02-14.
 */
public class EditText extends android.widget.EditText implements TouchMarginView, AnimatedView, TintedView {
    int dividerPadding;
    int disabledColor = 0x4d000000;
    int errorColor = 0xffff0000;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Pattern pattern;
    private String errorMessage;
    TextPaint errorPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    boolean drawError;

    int minCharacters;
    int maxCharacters;
    TextPaint counterPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    int extraPaddingBottom = 0, extraPaddingTop = 0;

    TextPaint labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            validate();
        }
    };

    private void validate() {
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
        if (errorMessage != null && pattern != null)
            drawError = !pattern.matcher(s).matches();
        counterError = minCharacters > 0 && s.length() < minCharacters || maxCharacters < Integer.MAX_VALUE && s.length() > maxCharacters;
        labelPaint.setColor(drawError | counterError ? errorColor : tint.getColorForState(new int[]{android.R.attr.state_focused}, disabledColor));
        counterPaint.setColor(drawError | counterError ? errorColor : disabledColor);
        if (showFloatingLabel)
            animateFloatingLabel(isFocused() && s.length() > 0);
    }

    private Bitmap dashPathBitmap;
    private BitmapShader dashPathShader;
    private float labelFrac = 0;
    private boolean showFloatingLabel = true;
    private boolean drawDivider = true;
    private boolean counterError = false;

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
        if (isInEditMode())
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EditText, defStyleAttr, 0);

        int ap = a.getResourceId(R.styleable.RadioButton_android_textAppearance, -1);
        if (ap != -1) {
            TypedArray appearance = getContext().obtainStyledAttributes(ap, R.styleable.TextAppearance);
            if (appearance != null) {
                for (int i = 0; i < appearance.getIndexCount(); i++) {
                    int attr = appearance.getIndex(i);
                    if (attr == R.styleable.TextAppearance_carbon_textAllCaps) {
                        setAllCaps(appearance.getBoolean(attr, true));
                    } else if (attr == R.styleable.TextAppearance_carbon_fontPath) {
                        String path = appearance.getString(attr);
                        Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                        setTypeface(typeface);
                    }
                }
                appearance.recycle();
            }
        }

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EditText_carbon_textAllCaps) {
                setAllCaps(a.getBoolean(attr, false));
            } else if (attr == R.styleable.EditText_carbon_fontPath) {
                String path = a.getString(attr);
                Typeface typeface = TypefaceUtils.getTypeface(getContext(), path);
                setTypeface(typeface);
            }
        }

        setPattern(a.getString(R.styleable.EditText_carbon_pattern));
        dividerPadding = (int) getResources().getDimension(R.dimen.carbon_paddingHalf);
        if (!isInEditMode())
            setError(a.getString(R.styleable.EditText_carbon_errorMessage));
        setMinCharacters(a.getInt(R.styleable.EditText_carbon_minCharacters, 0));
        setMaxCharacters(a.getInt(R.styleable.EditText_carbon_maxCharacters, Integer.MAX_VALUE));
        setFloatingLabelEnabled(a.getBoolean(R.styleable.EditText_carbon_floatingLabel, false));

        a.recycle();

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
        dashPathBitmap = Bitmap.createBitmap((int) (dip * 4), (int) dip, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(dashPathBitmap);
        paint.setColor(0xffffffff);
        paint.setAlpha(255);
        paint.setColor(disabledColor);
        c.drawCircle(dashPathBitmap.getHeight() / 2.0f, dashPathBitmap.getHeight() / 2.0f, dashPathBitmap.getHeight() / 2.0f, paint);
        dashPathShader = new BitmapShader(dashPathBitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);

        /*if (android.os.Build.VERSION.SDK_INT < 11) {
            setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

                @Override
                public void onCreateContextMenu(ContextMenu menu, View v,
                                                ContextMenu.ContextMenuInfo menuInfo) {
                    // TODO Auto-generated method stub
                    menu.clear();
                }
            });
        } else {
            setCustomSelectionActionModeCallback(new ActionMode.Callback() {

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {
                    // TODO Auto-generated method stub

                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    // TODO Auto-generated method stub
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode,
                                                   MenuItem item) {
                    // TODO Auto-generated method stub
                    return false;
                }
            });
        }*/

        validate();

        if (isFocused() && getText().length() > 0)
            labelFrac = 1;
    }

    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    private void updateLayout() {
        int paddingBottom = getPaddingBottom() - extraPaddingBottom;
        extraPaddingBottom = 0;
        if (errorMessage != null || minCharacters > 0 || maxCharacters < Integer.MAX_VALUE)
            extraPaddingBottom += getResources().getDimension(R.dimen.carbon_errorTextSize);
        if (drawDivider)
            extraPaddingBottom += getResources().getDimension(R.dimen.carbon_padding);

        int paddingTop = getPaddingTop() - extraPaddingTop;
        extraPaddingTop = 0;
        if (showFloatingLabel && getHint() != null)
            extraPaddingTop += getResources().getDimension(R.dimen.carbon_labelTextSize) + getResources().getDimension(R.dimen.carbon_paddingHalf);
        setPadding(getPaddingLeft(), paddingTop + extraPaddingTop, getPaddingRight(), paddingBottom + extraPaddingBottom);
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
        updateLayout();
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        this.setError(error);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isInEditMode())
            return;

        if (isFocused() && isEnabled()) {
            paint.setStrokeWidth(2 * getResources().getDimension(R.dimen.carbon_1dip));
        } else {
            paint.setStrokeWidth(getResources().getDimension(R.dimen.carbon_1dip));
        }
        if (drawDivider) {
            if (isEnabled()) {
                paint.setColor(drawError || counterError ? errorColor : tint.getColorForState(getDrawableState(), tint.getDefaultColor()));
                paint.setShader(null);
                canvas.drawLine(getPaddingLeft(), getHeight() + dividerPadding - getPaddingBottom(), getWidth() - getPaddingRight(), getHeight() + dividerPadding - getPaddingBottom(), paint);
            } else {
                Matrix matrix = new Matrix();
                matrix.postTranslate(0, getHeight() + dividerPadding - getPaddingBottom() - paint.getStrokeWidth() / 2.0f);
                dashPathShader.setLocalMatrix(matrix);
                paint.setShader(dashPathShader);
                canvas.drawRect(getPaddingLeft(), getHeight() + dividerPadding - getPaddingBottom() - paint.getStrokeWidth() / 2.0f,
                        getWidth() - getPaddingRight(), getHeight() + dividerPadding - getPaddingBottom() + paint.getStrokeWidth() / 2.0f, paint);
            }
        }

        if (!isEnabled())
            return;

        if (drawError)
            canvas.drawText(errorMessage, getPaddingLeft(), getHeight() - getPaddingBottom() + extraPaddingBottom - 1, errorPaint);

        if (getHint() != null && showFloatingLabel) {
            String label = getHint().toString();
            labelPaint.setAlpha((int) (255 * labelFrac));
            canvas.drawText(label, getPaddingLeft(), getResources().getDimension(R.dimen.carbon_labelTextSize) * (2 - labelFrac) + getPaddingTop() - extraPaddingTop, labelPaint);
        }

        int length = getText().length();
        if (minCharacters > 0 && maxCharacters < Integer.MAX_VALUE) {
            String text = length + " / " + minCharacters + "-" + maxCharacters;
            canvas.drawText(text, getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - getPaddingBottom() + extraPaddingBottom - 1, counterPaint);
        } else if (minCharacters > 0) {
            String text = length + " / " + minCharacters + "+";
            canvas.drawText(text, getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - getPaddingBottom() + extraPaddingBottom - 1, counterPaint);
        } else if (maxCharacters < Integer.MAX_VALUE) {
            String text = length + " / " + maxCharacters;
            canvas.drawText(text, getWidth() - counterPaint.measureText(text) - getPaddingRight(), getHeight() - getPaddingBottom() + extraPaddingBottom - 1, counterPaint);
        }
    }

    public boolean isFloatingLabelEnabled() {
        return showFloatingLabel;
    }

    public void setFloatingLabelEnabled(boolean showFloatingLabel) {
        this.showFloatingLabel = showFloatingLabel;
    }

    public String getPattern() {
        return pattern.pattern();
    }

    public void setPattern(final String pattern) {
        if (pattern == null) {
            this.pattern = null;
            return;
        }
        this.pattern = Pattern.compile(pattern);
    }

    public int getMinCharacters() {
        return minCharacters;
    }

    public void setMinCharacters(int minCharacters) {
        this.minCharacters = minCharacters;
        updateLayout();
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
        updateLayout();
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

    public void getHitRect(Rect outRect) {
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
        if (stateAnimators != null)
            for (StateAnimator animator : stateAnimators)
                animator.stateChanged(getDrawableState());
    }


    // -------------------------------
    // animations
    // -------------------------------

    private AnimUtils.Style inAnim, outAnim;

    public void setVisibility(final int visibility) {
        if (getVisibility() != View.VISIBLE && visibility == View.VISIBLE && inAnim != null) {
            AnimUtils.animateIn(this, inAnim, null);
            super.setVisibility(visibility);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    EditText.super.setVisibility(visibility);
                }
            });
        }
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
        this.tint = list != null ? list : new ControlFocusedColorStateList(getContext());
        drawDivider = Color.alpha(tint.getDefaultColor()) != 0;
        updateLayout();
    }

    @Override
    public void setTint(int color) {
        setTint(ColorStateList.valueOf(color));
    }

    @Override
    public ColorStateList getTint() {
        return tint;
    }
}
