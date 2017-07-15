package carbon.drawable;

import android.view.Gravity;

public enum ButtonGravity {
    LEFT(Gravity.LEFT), START(Gravity.START), RIGHT(Gravity.RIGHT), END(Gravity.END);

    private int gravity;

    ButtonGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getGravity() {
        return gravity;
    }
}
