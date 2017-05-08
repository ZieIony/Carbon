package carbon.animation;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public class ViewAnimator extends ValueAnimator {

    private View target;

    private OnSetupValuesListener listener;

    public void setOnSetupValuesListener(OnSetupValuesListener listener) {
        this.listener = listener;
    }

    @Override
    public void start() {
        if (listener != null)
            listener.onSetupValues();
        super.start();
    }

    @Override
    public void setTarget(@Nullable Object target) {
        super.setTarget(target);
        this.target = (View) target;
    }

    @NonNull
    public View getTarget() {
        return target;
    }
}
