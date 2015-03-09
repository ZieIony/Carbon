package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.animation.AnimUtils;
import carbon.animation.StateAnimator;
import carbon.drawable.CheckableDrawable;
import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleView;

/**
 * Created by Marcin on 2015-03-06.
 */
public class RadioButton extends android.widget.RadioButton implements RippleView, TouchMarginView, StateAnimatorView, AnimatedView {
    private CheckableDrawable drawable;

    public RadioButton(Context context) {
        this(context, null);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_radioButtonStyle);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public void init(AttributeSet attrs, int defStyleAttr) {
        if(!isInEditMode()) {
            drawable = new CheckableDrawable(getContext(), R.raw.carbon_radiobutton_checked, R.raw.carbon_radiobutton_unchecked, R.raw.carbon_radiobutton_filled, new PointF(0, 0));
            setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            setButtonDrawable(null);
        }

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RadioButton, defStyleAttr, 0);

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

        Carbon.initRippleDrawable(this, attrs, defStyleAttr);

        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.RadioButton_carbon_textAllCaps) {
                setAllCaps(a.getBoolean(R.styleable.RadioButton_carbon_textAllCaps, false));
            } else if (attr == R.styleable.RadioButton_carbon_textStyle) {
                setTextStyle(Roboto.Style.values()[a.getInt(R.styleable.RadioButton_carbon_textStyle, Roboto.Style.Regular.ordinal())]);
            }else if(attr==R.styleable.RadioButton_carbon_radioCheckedColor){
                drawable.setCheckedColor(a.getColor(attr,0));
            }else if(attr==R.styleable.RadioButton_carbon_radioUncheckedColor){
                drawable.setUncheckedColor(a.getColor(attr,0));
            }
        }

        Carbon.initAnimations(this, attrs, defStyleAttr);
        Carbon.initTouchMargin(this, attrs, defStyleAttr);
        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!changed || getWidth() == 0 || getHeight() == 0)
            return;

        if (rippleDrawable != null)
            rippleDrawable.setBounds(0, 0, getWidth(), getHeight());
    }

    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    // -------------------------------
    // ripple
    // -------------------------------

    private RippleDrawable rippleDrawable;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (rippleDrawable != null && event.getAction() == MotionEvent.ACTION_DOWN)
            ((RippleDrawable) rippleDrawable).setHotspot(event.getX(), event.getY());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public RippleDrawable getRippleDrawable() {
        return rippleDrawable;
    }

    public void setRippleDrawable(RippleDrawable rippleDrawable) {
        this.rippleDrawable = rippleDrawable;
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || rippleDrawable == who;
    }

    @Override
    public void invalidateDrawable(Drawable drawable) {
        super.invalidateDrawable(drawable);
        if (rippleDrawable != null && getParent() != null && rippleDrawable.getStyle() == RippleDrawable.Style.Borderless)
            ((View) getParent()).postInvalidate();
    }

    @Override
    public void setBackground(Drawable background) {
        setBackgroundDrawable(background);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (rippleDrawable == null || rippleDrawable.getBackground() == null) {
            super.setBackgroundDrawable(background);
            return;
        }
        rippleDrawable.setBackground(background);
        super.setBackgroundDrawable(rippleDrawable);
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
            AnimUtils.animateIn(this, inAnim, null);
            super.setVisibility(visibility);
        } else if (getVisibility() == View.VISIBLE && visibility != View.VISIBLE) {
            AnimUtils.animateOut(this, outAnim, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animator) {
                    RadioButton.super.setVisibility(visibility);
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
