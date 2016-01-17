package carbon.widget;

import com.nineoldandroids.animation.Animator;

import carbon.animation.AnimUtils;

/**
 * Created by Marcin on 2014-11-30.
 * <p/>
 * Interface of a view with animations. These animations are used for changing visibility by using setVisible(boolean) method.
 */
public interface AnimatedView {
    /**
     * Gets the current Animator object. Works like View.getAnimation() but with animators.
     *
     * @return the current Animator object or null
     */
    Animator getAnimator();

    /**
     * Gets the animation used when view's visibility is changed from VISIBLE to GONE/INVISIBLE
     *
     * @return the current out animation or AnimUtils.Style.None if nothing is set.
     */
    AnimUtils.Style getOutAnimation();

    /**
     * Sets the animation used when view's visibility is changed from VISIBLE to GONE/INVISIBLE
     *
     * @param outAnim new out animation. Use AnimUtils.Style.None for no animation.
     */
    void setOutAnimation(AnimUtils.Style outAnim);

    /**
     * Gets the animation used when view's visibility is changed from GONE/INVISIBLE to VISIBLE
     *
     * @return the current in animation or AnimUtils.Style.None if nothing is set.
     */
    AnimUtils.Style getInAnimation();

    /**
     * Sets the animation used when view's visibility is changed from GONE/INVISIBLE to VISIBLE
     *
     * @param inAnim new in animation. Use AnimUtils.Style.None for no animation.
     */
    void setInAnimation(AnimUtils.Style inAnim);
}
