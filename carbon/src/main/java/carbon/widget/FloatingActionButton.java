package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import carbon.R;
import carbon.animation.ElevationStateAnimator;

/**
 * Created by Marcin on 2014-12-04.
 *
 * FAB implementation using an ImageView and rounded corners. Supports SVGs, animated shadows, ripples
 * and other material features.
 */
public class FloatingActionButton extends ImageView {

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_fabStyle);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        addStateAnimator(new ElevationStateAnimator(this));

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyleAttr, 0);
        if (a != null) {
            setCornerRadius((int) a.getDimension(R.styleable.FloatingActionButton_carbon_cornerRadius, -1));
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getCornerRadius() < 0)
            setCornerRadius(Math.min(getWidth(), getHeight()) / 2);
    }
}
