package carbon.view;

import android.animation.Animator;

public interface RevealView extends RenderingModeView {
    float MAX_RADIUS = -1;

    Animator createCircularReveal(android.view.View hotspot, float startRadius, float finishRadius);

    Animator createCircularReveal(int x, int y, float startRadius, float finishRadius);
}
