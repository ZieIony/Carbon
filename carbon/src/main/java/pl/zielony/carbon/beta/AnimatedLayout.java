package pl.zielony.carbon.beta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Marcin on 2014-11-15.
 */
public class AnimatedLayout extends FrameLayout implements AnimatedView {
    private Rect clipRect = new Rect();
    private float dim;
    private Rect from, to;

    public AnimatedLayout(Context context) {
        super(context);
    }

    public AnimatedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void fadeIn(List<View> views) {
        Collections.sort(views, new Comparator<View>() {
            @Override
            public int compare(View view, View view2) {
                return view.getLeft() - view2.getLeft();
            }
        });
        Collections.sort(views, new Comparator<View>() {
            @Override
            public int compare(View view, View view2) {
                return view.getTop() - view2.getTop();
            }
        });

        for (final View v : views) {
            final AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(100);
            animation.setFillBefore(true);
            animation.setFillEnabled(true);
            animation.setFillAfter(true);
            animation.setStartOffset(views.indexOf(v) * 20);
            v.startAnimation(animation);
        }
    }

    private List<View> getViews() {
        List<View> views = new ArrayList<View>();
        List<ViewGroup> viewGroups = new ArrayList<ViewGroup>();

        viewGroups.add(this);
        while (!viewGroups.isEmpty()) {
            ViewGroup viewGroup = viewGroups.remove(0);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof ViewGroup) {
                    viewGroups.add((ViewGroup) view);
                } else {
                    views.add(view);
                }
            }
        }
        return views;
    }

    public void animateIn() {
//        heroFrom.setDrawingCacheEnabled(true);

        final List<View> views = getViews();
        //      views.remove(heroTo);
        for (View v : views) {
      //      v.setVisibility(View.INVISIBLE);
        }

       // fadeIn(views);

        LayoutAnimation animation = new LayoutAnimation(from, to, this);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(300);
        animation.setFillEnabled(true);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        startAnimation(animation);
    }

    public void animateOut(Animation.AnimationListener listener) {
        Rect rectTo = new Rect(getLeft(), getTop(), getRight(), getBottom());

        final List<View> views = getViews();
        for (View v : views) {
            v.setVisibility(View.INVISIBLE);
        }

        if (listener != null)
            listener.onAnimationEnd(null);
    }

    private void fadeOut(List<View> views, Animation.AnimationListener listener) {
        Collections.sort(views, new Comparator<View>() {
            @Override
            public int compare(View view, View view2) {
                return view.getTop() - view2.getTop();
            }
        });

        AlphaAnimation animation = null;
        for (View v : views) {
            animation = new AlphaAnimation(1, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(100);
            animation.setFillBefore(true);
            animation.setFillEnabled(true);
            animation.setFillAfter(true);
            animation.setStartOffset(views.indexOf(v) * 20);
            v.startAnimation(animation);
        }

        if (animation != null)
            animation.setAnimationListener(listener);
    }

    public void dimIn(float percent, int duration, Interpolator interpolator) {
        ValueAnimation animation = new ValueAnimation(0, percent, this, "dim");
        animation.setDuration(duration);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setInterpolator(interpolator);
        animation.start();
    }

    public void dimOut(int duration, Interpolator interpolator) {
        ValueAnimation animation = new ValueAnimation(dim, 0, this, "dim");
        animation.setDuration(duration);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        animation.setInterpolator(interpolator);
        animation.start();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawColor(Color.argb((int) (0xff * dim), 0, 0, 0));
        canvas.save();
        canvas.clipRect(clipRect);
        super.dispatchDraw(canvas);
        canvas.restore();
    }

    public void setClipRect(int left, int top, int right, int bottom) {
        clipRect.left = left;
        clipRect.top = top;
        clipRect.right = right;
        clipRect.bottom = bottom;
    }

    public void setDim(float dim) {
        this.dim = dim;
    }

    public void setFrom(Rect from) {
        this.from = from;
    }

    public void setTo(Rect to) {
        this.to = to;
    }
}
