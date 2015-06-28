package carbon.widget;

import com.nineoldandroids.animation.Animator;

import carbon.animation.AnimUtils;

/**
 * Created by Marcin on 2014-11-30.
 */
public interface AnimatedView {
    Animator getAnimator();

    AnimUtils.Style getOutAnimation();

    void setOutAnimation(AnimUtils.Style outAnim);

    AnimUtils.Style getInAnimation();

    void setInAnimation(AnimUtils.Style inAnim);
}
