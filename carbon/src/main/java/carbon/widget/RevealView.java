package carbon.widget;

import android.animation.Animator;

/**
 * Created by Marcin on 2017-01-28.
 */

public interface RevealView {
    Animator startReveal(int x, int y, float startRadius, float finishRadius);
}
