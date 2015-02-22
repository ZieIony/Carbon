package carbon.widget;

import carbon.animation.StateAnimator;

/**
 * Created by Marcin on 2015-02-22.
 */
public interface StateAnimatorView {

    void removeStateAnimator(StateAnimator animator);

    void addStateAnimator(StateAnimator animator);
}
