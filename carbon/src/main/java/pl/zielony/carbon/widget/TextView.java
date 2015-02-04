package pl.zielony.carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.animation.Animator;

import pl.zielony.carbon.R;
import pl.zielony.carbon.animation.AnimUtils;
import pl.zielony.carbon.animation.DefaultAnimatorListener;

/**
 * Created by Marcin on 2014-11-07.
 */
public class TextView extends android.widget.TextView {
    Roboto.Style style;
    private AnimUtils.Style inAnim,outAnim;

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
        inAnim = AnimUtils.Style.values()[a.getInt(R.styleable.TextView_carbon_inAnimation,0)];
        outAnim = AnimUtils.Style.values()[a.getInt(R.styleable.TextView_carbon_outAnimation,0)];
        a.recycle();
    }

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

    private void setTextStyle(Roboto.Style style) {
        this.style = style;
        if (!isInEditMode())
            super.setTypeface(Roboto.getTypeface(getContext(), style));
    }
}
