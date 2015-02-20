package carbon.animation;

/**
 * Created by Marcin on 2015-02-20.
 */
public class StateAnimator {
    protected boolean pressed = false, enabled = false;

    public void stateChanged(int[] states) {
        pressed = false;
        enabled = false;
        for (int state : states) {
            if (state == android.R.attr.state_enabled) {
                enabled = true;
            } else if (state == android.R.attr.state_pressed) {
                pressed = true;
            }
        }
    }
}
