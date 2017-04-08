package carbon.beta;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import carbon.widget.FrameLayout;

public class BottomSheet extends FrameLayout {
    private ViewDragHelper mDragHelper;

    public BottomSheet(Context context) {
        super(context);
        initBottomSheet();
    }

    public BottomSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBottomSheet();
    }

    public BottomSheet(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBottomSheet();
    }

    public BottomSheet(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBottomSheet();
    }

    private void initBottomSheet() {
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    public class DragHelperCallback extends ViewDragHelper.Callback {

        public int getViewVerticalDragRange(View child) {
            return ((ViewGroup) getParent()).getMeasuredHeight() - child.getMeasuredHeight();
        }

        @Override
        public boolean tryCaptureView(View view, int i) {
            return true;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mDragHelper.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() { // needed for automatic settling.
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


}
