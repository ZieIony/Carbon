package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.StateAnimator;

/**
 * Created by Marcin on 2015-02-14.
 */
public class EditText extends android.widget.EditText implements TouchMarginView, AnimatedView {
    int dividerPadding;
    ColorStateList dividerColor;
    int disabledColor = 0x4d000000;
    int errorColor = 0xffff0000;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path linePath;

    private Pattern pattern;
    private String errorMessage;
    TextPaint errorPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    boolean error;

    int minCharacters;
    int maxCharacters;

    int extraPaddingBottom = 0, extraPaddingTop = 0;

    TextPaint labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    boolean floatingHint = false;

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (errorMessage != null && pattern != null)
                error = !pattern.matcher(getText().toString()).matches();
        }
    };
    private Bitmap dashPathBitmap;
    private BitmapShader dashPathShader;

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

        int ap = a.getResourceId(R.styleable.RadioButton_android_textAppearance, -1);
        if (ap != -1) {
            TypedArray appearance = getContext().obtainStyledAttributes(ap, R.styleable.TextAppearance);
            if (appearance != null) {
                for (int i = 0; i < appearance.getIndexCount(); i++) {
                    int attr = appearance.getIndex(i);
                    if (attr == R.styleable.TextAppearance_carbon_textAllCaps) {
                        setAllCaps(appearance.getBoolean(R.styleable.TextAppearance_carbon_textAllCaps, true));
                    } else if (attr == R.styleable.TextAppearance_carbon_textStyle) {
                        setTextStyle(Roboto.Style.values()[appearance.getInt(R.styleable.TextAppearance_carbon_textStyle, Roboto.Style.Regular.ordinal())]);
                    }
                }
                appearance.recycle();
            }
        }

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.EditText_carbon_textAllCaps) {
                setAllCaps(a.getBoolean(R.styleable.EditText_carbon_textAllCaps, false));
            } else if (attr == R.styleable.EditText_carbon_textStyle) {
                setTextStyle(Roboto.Style.values()[a.getInt(R.styleable.EditText_carbon_textStyle, Roboto.Style.Regular.ordinal())]);
            }
        }

        Carbon.initAnimations(this, attrs, defStyleAttr);
        Carbon.initTouchMargin(this, attrs, defStyleAttr);

        setPattern(a.getString(R.styleable.EditText_carbon_pattern));
        setDividerPadding((int) a.getDimension(R.styleable.EditText_carbon_dividerPadding, 0));
        setDividerColor(a.getColorStateList(R.styleable.EditText_carbon_dividerColor));
        if (!isInEditMode())
            setErrorMessage(a.getString(R.styleable.EditText_carbon_errorMessage));

        a.recycle();

        if (!isInEditMode()) {
            errorPaint.setTypeface(Roboto.getTypeface(getContext(), Roboto.Style.Regular));
            errorPaint.setTextSize(getResources().getDimension(R.dimen.carbon_errorTextSize));
            errorPaint.setColor(errorColor);

            labelPaint.setTypeface(Roboto.getTypeface(getContext(), Roboto.Style.Regular));
            labelPaint.setTextSize(getResources().getDimension(R.dimen.carbon_labelTextSize));
            labelPaint.setColor(disabledColor);
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
        int dividerPadding = this.dividerPadding - extraPaddingBottom;
        if (errorMessage != null || minCharacters > 0 || maxCharacters > 0) {
            extraPaddingBottom = (int) (getResources().getDimension(R.dimen.carbon_errorTextSize) + getResources().getDimension(R.dimen.carbon_paddingHalf));
        } else {
            extraPaddingBottom = 0;
        }
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), paddingBottom + extraPaddingBottom);
        this.dividerPadding = dividerPadding + extraPaddingBottom;
    }

    public void setErrorMessage(String text) {
        this.errorMessage = text;
        if (errorMessage == null)
            error = false;
        updateLayout();
    }

    @Override
    public void setError(CharSequence text) {
        setErrorMessage(text.toString());
        error = errorMessage != null;
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        this.setError(error);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (isFocused() && isEnabled()) {
            paint.setStrokeWidth(2 * getResources().getDimension(R.dimen.carbon_1dip));
        } else {
            paint.setStrokeWidth(getResources().getDimension(R.dimen.carbon_1dip));
        }
        if (isEnabled()) {
            paint.setColor(error ? errorColor : dividerColor.getColorForState(getDrawableState(), dividerColor.getDefaultColor()));
            paint.setShader(null);
            canvas.drawLine(0, getHeight() - dividerPadding, getWidth(), getHeight() - dividerPadding, paint);
        } else {
            Matrix matrix = new Matrix();
            matrix.postTranslate(0, getHeight() - dividerPadding - paint.getStrokeWidth() / 2.0f);
            dashPathShader.setLocalMatrix(matrix);
            paint.setShader(dashPathShader);
            canvas.drawRect(0, getHeight() - dividerPadding - paint.getStrokeWidth() / 2.0f,
                    getWidth(), getHeight() - dividerPadding + paint.getStrokeWidth() / 2.0f, paint);
        }

        if (error && errorMessage != null)
            canvas.drawText(errorMessage, 0, getHeight() - dividerPadding + errorPaint.getTextSize() + getResources().getDimension(R.dimen.carbon_paddingHalf), errorPaint);
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

    public ColorStateList getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = ColorStateList.valueOf(dividerColor);
        postInvalidate();
    }

    public void setDividerColor(ColorStateList dividerColor) {
        this.dividerColor = dividerColor;
        postInvalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setDividerPadding(int dividerPadding) {
        this.dividerPadding = dividerPadding + extraPaddingBottom;
        postInvalidate();
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
    // roboto
    // -------------------------------

    Roboto.Style style;

    public void setTextStyle(Roboto.Style style) {
        this.style = style;
        if (!isInEditMode())
            super.setTypeface(Roboto.getTypeface(getContext(), style));
    }

    public Roboto.Style getTextStyle() {
        return style;
    }
}
