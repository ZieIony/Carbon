package tk.zielony.carbonsamples.feature.scroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import carbon.widget.RelativeLayout;

/**
 * Created by Marcin on 2017-02-09.
 */

public class DependencyLayout extends RelativeLayout implements GestureDetector.OnGestureListener {
    GestureDetector detector = new GestureDetector(this);

    public DependencyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateChildren() {
        Stream.of(getViews()).forEach(view -> {
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            View dependency = findViewById(params.getDependsOn());
            if (view instanceof BehaviorView)
                ((BehaviorView) view).dependedViewChanged(dependency);
        });
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (detector.onTouchEvent(ev))
            return true;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateChildren();
    }


    // -------------------------------
    // layout params
    // -------------------------------

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int dy = (int) distanceY;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof ScrollChild) {
                ScrollChild child = (ScrollChild) view;
                dy -= child.onNestedScrollByY(dy);
            }
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {

        private int dependsOn;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, carbon.R.styleable.DependencyLayout_Layout);
            dependsOn = a.getResourceId(carbon.R.styleable.DependencyLayout_Layout_carbon_dependsOn, -1);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.widget.RelativeLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
        }

        public LayoutParams(RelativeLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
        }

        public LayoutParams(DependencyLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
        }

        public int getDependsOn() {
            return dependsOn;
        }
    }

}
