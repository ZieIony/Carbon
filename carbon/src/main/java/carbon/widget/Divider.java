package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import carbon.R;

/**
 * A very simple class of a divider. Just place it in your layout to get a material divider.
 */
public class Divider extends View {
    public static final int HORIZONTAL = 0, VERTICAL = 1;

    private int orientation = HORIZONTAL;

    public Divider(Context context) {
        super(context, null, R.attr.carbon_dividerStyle);
        initDivider(null, R.attr.carbon_dividerStyle, R.style.carbon_Divider);
    }

    public Divider(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_dividerStyle);
        initDivider(attrs, R.attr.carbon_dividerStyle, R.style.carbon_Divider);
    }

    public Divider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initDivider(attrs, defStyleAttr, R.style.carbon_Divider);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Divider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initDivider(attrs, defStyleAttr, defStyleRes);
    }

    public void initDivider(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Divider, defStyleAttr, defStyleRes);
        orientation = a.getInt(R.styleable.Divider_android_orientation, 0);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (orientation == HORIZONTAL) {
            setMeasuredDimension(getMeasuredWidth(), getContext().getResources().getDimensionPixelSize(R.dimen.carbon_dividerHeight));
        } else {
            setMeasuredDimension(getContext().getResources().getDimensionPixelSize(R.dimen.carbon_dividerHeight), getMeasuredHeight());
        }
    }
}
