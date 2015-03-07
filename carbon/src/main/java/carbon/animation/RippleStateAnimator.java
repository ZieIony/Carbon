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
    protected void onPressedChanged() {
        if (view.getRippleDrawable() == null)
            return;
        if (pressed) {
            view.getRippleDrawable().onPress();
        } else {
            view.getRippleDrawable().onRelease();
        }
    }
}
