package carbon.drawable.ripple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

class AnimatorsCompat {

    static List<WeakReference<ObjectAnimator>> sRunningAnimators = new ArrayList<>();

    public static void start(ObjectAnimator animator) {
        sRunningAnimators.add(new WeakReference<>(animator));

        animator.start();
    }

    public static void setAutoCancel(final ObjectAnimator animator) {
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                for (WeakReference<ObjectAnimator> wa : sRunningAnimators) {
                    ObjectAnimator a = wa.get();
                    if (a == null) {
                        continue;
                    }

                    if (hasSameTargetAndProperties(animator, a)) {
                        a.cancel();
                    }
                }
            }
        });
    }

    private static boolean hasSameTargetAndProperties(ObjectAnimator self, @Nullable Animator anim) {
        if (anim instanceof ObjectAnimator) {
            PropertyValuesHolder[] theirValues = ((ObjectAnimator) anim).getValues();
            PropertyValuesHolder[] selfValues = self.getValues();
            if (((ObjectAnimator) anim).getTarget() == self.getTarget() && selfValues.length == theirValues.length) {

                final int length = selfValues.length;
                for (int i = 0; i < length; ++i) {
                    PropertyValuesHolder pvhMine = selfValues[i];
                    PropertyValuesHolder pvhTheirs = theirValues[i];
                    if (pvhMine.getPropertyName() == null || !pvhMine.getPropertyName().equals(pvhTheirs.getPropertyName())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

}
