package carbon.beta;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import carbon.widget.AnimatedView;

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

     public boolean onBack() {
        if (getChildCount() > 1) {
            removeView(getChildAt(getChildCount() - 1));
            return true;
        }
        return false;
    }
}
