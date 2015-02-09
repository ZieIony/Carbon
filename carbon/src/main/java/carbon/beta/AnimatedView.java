package carbon.beta;

import android.view.animation.Animation;

/**
 * Created by Marcin on 2014-11-30.
 */
public interface AnimatedView {
    void animateIn();
    void animateOut(Animation.AnimationListener animationListener);
}
