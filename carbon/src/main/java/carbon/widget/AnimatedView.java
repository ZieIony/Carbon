package carbon.widget;

import android.view.animation.Animation;

import carbon.animation.AnimUtils;

/**
 * Created by Marcin on 2014-11-30.
 */
public interface AnimatedView {
    public AnimUtils.Style getOutAnimation();

    public void setOutAnimation(AnimUtils.Style outAnim);

    public AnimUtils.Style getInAnimation();

    public void setInAnimation(AnimUtils.Style inAnim);
}
