package carbon.animation;

/**
 * Created by Marcin on 2015-02-20.
 */
public abstract class StateAnimator {
    protected boolean pressed = false, enabled = false;
    private boolean checked = false;

    public void stateChanged(int[] states) {
        boolean newPressed = false, newEnabled = false, newChecked = false;
        for (int state : states) {
            if (state == android.R.attr.state_enabled) {
                newEnabled = true;
            } else if (state == android.R.attr.state_pressed) {
                newPressed = true;
            } else if (state == android.R.attr.state_checked) {
                newChecked = true;
            }
        }
        if (pressed != newPressed) {
            pressed = newPressed;
            onPressedChanged();
        }
        if (enabled != newEnabled) {
            enabled = newEnabled;
            onEnabledChanged();
        }
        if (checked != newChecked) {
            checked = newChecked;
            onCheckedChanged();
        }
    }

    protected void onPressedChanged() {

    }

    protected void onEnabledChanged() {

    }

    protected void onCheckedChanged() {

    }
}
