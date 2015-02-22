package carbon.animation;

import carbon.drawable.RippleView;

/**
 * Created by Marcin on 2015-02-20.
 */
public class RippleStateAnimator extends StateAnimator {
    private final RippleView view;

    public RippleStateAnimator(RippleView view) {
        this.view = view;
    }

    @Override
    public void stateChanged(int[] states) {
        super.stateChanged(states);
        if (view.getRippleDrawable() == null)
            return;
        if (pressed) {
            view.getRippleDrawable().onPress();
        } else {
            view.getRippleDrawable().onRelease();
        }
    }
}
