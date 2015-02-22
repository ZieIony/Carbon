package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.DefaultAnimatorListener;
import carbon.animation.StateAnimator;

/**
 * Created by Marcin on 2014-11-07.
 */
public class TextView extends android.widget.TextView implements TouchMarginView {

    public TextView(Context context) {
        super(context);
        init(null, android.R.attr.textViewStyle);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, android.R.attr.textViewStyle);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextView, defStyle, 0);
        setTextStyle(Roboto.Style.values()[a.getInt(R.styleable.TextView_carbon_textStyle, Roboto.Style.Regular.ordinal())]);
        setInAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.TextView_carbon_inAnimation, 0)]);
        setOutAnimation(AnimUtils.Style.values()[a.getInt(R.styleable.TextView_carbon_outAnimation, 0)]);
        Carbon.initTouchMargin(this, attrs, defStyle);
        a.recycle();
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
            AnimUtils.animateOut(this, outAnim, new DefaultAnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    TextView.super.setVisibility(visibility);
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
