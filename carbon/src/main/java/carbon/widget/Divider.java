package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

import carbon.R;
import carbon.view.Orientation;
import carbon.view.View;

/**
 * A very simple class of a divider. Just place it in your layout to get a material divider.
 */
public class Divider extends View {

    private Orientation orientation = Orientation.HORIZONTAL;

    public Divider(Context context) {
        super(context, null, R.attr.carbon_dividerStyle);
        initDivider(null, R.attr.carbon_dividerStyle, R.style.carbon_Divider);
    }

    public Divider(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_dividerStyle);
        initDivider(attrs, R.attr.carbon_dividerStyle, R.style.carbon_Divider);
    }

    public Divider(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initDivider(attrs, defStyleAttr, R.style.carbon_Divider);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Divider(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initDivider(attrs, defStyleAttr, defStyleRes);
    }

    public void initDivider(AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Divider, defStyleAttr, defStyleRes);
        orientation = Orientation.values()[a.getInt(R.styleable.Divider_android_orientation, Orientation.HORIZONTAL.ordinal())];
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (orientation == Orientation.HORIZONTAL) {
            setMeasuredDimension(getMeasuredWidth(), getContext().getResources().getDimensionPixelSize(R.dimen.carbon_dividerHeight));
        } else {
            setMeasuredDimension(getContext().getResources().getDimensionPixelSize(R.dimen.carbon_dividerHeight), getMeasuredHeight());
        }
    }
}
