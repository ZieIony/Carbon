package tk.zielony.carbonsamples.feature;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
