package carbon.widget;

import android.animation.Animator;

public interface RevealView {
    Animator startReveal(int x, int y, float startRadius, float finishRadius);
}
