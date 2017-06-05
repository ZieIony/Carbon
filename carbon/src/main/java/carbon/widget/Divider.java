package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import carbon.R;

/**
 * A very simple class of a divider. Just place it in your layout to get a material divider.
 */
public class Divider extends View {
    public Divider(Context context) {
        super(context, null, R.attr.carbon_dividerStyle);
    }

    public Divider(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_dividerStyle);
    }

    public Divider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Divider(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getContext().getResources().getDimensionPixelSize(R.dimen.carbon_dividerHeight));
    }
}
