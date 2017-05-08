package carbon.widget;

import android.animation.Animator;

public interface RevealView extends RenderingModeView {
    Animator createCircularReveal(int x, int y, float startRadius, float finishRadius);
}
