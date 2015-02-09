package carbon.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Marcin on 2015-01-11.
 */
public class FastLayout extends carbon.widget.FrameLayout {
    public FastLayout(Context context) {
        super(context);
    }

    public FastLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FastLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void requestLayout() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
        if (layoutParams != null && layoutParams.width > 0 && layoutParams.height > 0) {
            View parent = ((View) getParent());
            measure(MeasureSpec.makeMeasureSpec(layoutParams.width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(layoutParams.height, MeasureSpec.EXACTLY));
            layout(layoutParams.leftMargin + parent.getPaddingLeft(),
                    parent.getPaddingTop() + layoutParams.topMargin,
                    parent.getPaddingLeft() + layoutParams.leftMargin + layoutParams.width,
                    parent.getPaddingTop() + layoutParams.topMargin + layoutParams.height);
        } else {
            super.requestLayout();
        }
    }
}
