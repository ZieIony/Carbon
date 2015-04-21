package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import carbon.R;
import carbon.animation.ElevationStateAnimator;

/**
 * Created by Marcin on 2014-12-04.
 */
public class ImageActionButton extends ImageView {

    public ImageActionButton(Context context) {
        this(context, null);
    }

    public ImageActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_fabStyle);
    }

    public ImageActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        setRect(false);
        addStateAnimator(new ElevationStateAnimator(this));

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImageActionButton, defStyleAttr, 0);
        if (a != null) {
            setCornerRadius((int) a.getDimension(R.styleable.ImageActionButton_carbon_cornerRadius, -1));
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
