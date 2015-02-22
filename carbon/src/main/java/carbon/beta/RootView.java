package carbon.beta;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by Marcin on 2014-11-30.
 */
public class RootView extends android.widget.FrameLayout {
    public RootView(Context context) {
        super(context);
    }

    public RootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(final View view) {
        super.addView(view);
        if (view instanceof AnimatedView)
            ((AnimatedView) view).animateIn();
    }

    @Override
    public void removeView(final View view) {
        if (view instanceof AnimatedView) {
            ((AnimatedView) view).animateOut(new DefaultAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    _removeView(view);
                }
            });
        } else {
            super.removeView(view);
        }
    }

    private void _removeView(View view) {
        super.removeView(view);
    }

    public boolean onBack() {
        if (getChildCount() > 1) {
            removeView(getChildAt(getChildCount() - 1));
            return true;
        }
        return false;
    }
}
