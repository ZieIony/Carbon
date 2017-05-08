package carbon.animation;

import android.animation.Animator;

/**
 * Interface of a view with animations. These animations are used for changing visibility by using
 * setVisible(boolean) method.
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
     * @return the current out animation or null if nothing is set.
     */
    Animator getOutAnimator();

    /**
     * Sets the animation used when view's visibility is changed from VISIBLE to GONE/INVISIBLE
     *
     * @param outAnim new out animation. Use null for no animation.
     */
    void setOutAnimator(Animator outAnim);

    /**
     * Gets the animation used when view's visibility is changed from GONE/INVISIBLE to VISIBLE
     *
     * @return the current in animation or null if nothing is set.
     */
    Animator getInAnimator();

    /**
     * Sets the animation used when view's visibility is changed from GONE/INVISIBLE to VISIBLE
     *
     * @param inAnim new in animation. Use null for no animation.
     */
    void setInAnimator(Animator inAnim);

    /**
     * Sets visibility using set animation style.
     *
     * @param visibility one of View.VISIBLE/INVISIBLE/GONE flags
     * @return visibility animation animator
     */
    Animator animateVisibility(int visibility);
}
