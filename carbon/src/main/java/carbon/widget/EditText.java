package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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
public class EditText extends android.widget.EditText implements TouchMarginView {
    int maxCharacters;
    int minCharacters;
    private Pattern pattern;
    float dividerPadding;
    int dividerColor;
    private Paint paint = new Paint();

    public EditText(Context context) {
        super(context);
        init(null, R.attr.carbon_editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_editTextStyle);
    }

    public EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.EditText, defStyle, 0);
        setTextStyle(Roboto.Style.values()[a.getInt(R.styleable.EditText_carbon_textStyle, Roboto.Style.Regular.ordinal())]);
        setInAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.EditText_carbon_inAnimation, 0)]);
        setOutAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.EditText_carbon_outAnimation, 0)]);
        Carbon.initTouchMargin(this, attrs, defStyle);

        setPattern(a.getString(R.styleable.EditText_carbon_pattern));
        setDividerPadding(a.getDimension(R.styleable.EditText_carbon_dividerPadding, 0));
        setDividerColor(a.getColor(R.styleable.EditText_carbon_dividerColor, 0));

        a.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        paint.setColor(dividerColor);
        paint.setStrokeWidth(2);
        canvas.drawLine(0, getHeight() - dividerPadding, getWidth(), getHeight() - dividerPadding, paint);
    }

    public String getPattern() {
        return pattern.pattern();
    }

    public void setPattern(String pattern) {
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
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {
        this.maxCharacters = maxCharacters;
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public float getDividerPadding() {
        return dividerPadding;
    }

    public void setDividerPadding(float dividerPadding) {
        this.dividerPadding = dividerPadding;
    }


    // -------------------------------
    // touch margin
    // -------------------------------

    private Rect touchMargin;

    @Override
    public void setTouchMargin(Rect rect) {
        touchMargin = rect;
    }

    @Override
    public void setTouchMargin(int left, int top, int right, int bottom) {
        touchMargin = new Rect(left, top, right, bottom);
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
            super.setVisibility(visibility);
            AnimUtils.animateIn(this, inAnim, null);
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
