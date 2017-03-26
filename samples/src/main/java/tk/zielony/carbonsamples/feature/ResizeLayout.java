package tk.zielony.carbonsamples.feature;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Marcin on 2017-03-20.
 */

public class ResizeLayout extends FrameLayout {
    public ResizeLayout(@NonNull Context context) {
        super(context);
    }

    public ResizeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewGroup.LayoutParams layoutParams = getChildAt(0).getLayoutParams();
        layoutParams.height = (int) ev.getY();
        getChildAt(0).setLayoutParams(layoutParams);
        getChildAt(0).requestLayout();
        return true;
    }
}
