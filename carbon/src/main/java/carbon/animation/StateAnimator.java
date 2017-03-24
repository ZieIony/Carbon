package carbon.animation;

import android.animation.Animator;
import android.util.StateSet;
import android.view.View;
import android.view.animation.Animation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Marcin on 2015-02-20.
 */
public class StateAnimator {
    private final ArrayList<Tuple> mTuples = new ArrayList<>();

    private Tuple lastMatch = null;
    private Animator runningAnimation = null;
    private WeakReference<AnimatedView> viewRef;

    public StateAnimator(AnimatedView target) {
        setTarget(target);
    }

    private Animator.AnimatorListener mAnimationListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (runningAnimation == animation) {
                runningAnimation = null;
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }

    };

    /**
     * Associates the given Animation with the provided drawable state specs so that it will be run
     * when the View's drawable state matches the specs.
     *
     * @param specs     drawable state specs to match against
     * @param animation The Animation to run when the specs match
     */
    public void addState(int[] specs, Animator animation, Animator.AnimatorListener listener) {
        Tuple tuple = new Tuple(specs, animation, listener);
        animation.addListener(mAnimationListener);
        mTuples.add(tuple);
    }

    /**
     * Returns the current {@link Animation} which is started because of a state
     * change.
     *
     * @return The currently running Animation or null if no Animation is running
     */
    Animator getRunningAnimation() {
        return runningAnimation;
    }


    AnimatedView getTarget() {
        return viewRef == null ? null : viewRef.get();
    }

    void setTarget(AnimatedView view) {
        final AnimatedView current = getTarget();
        if (current == view) {
            return;
        }
        if (current != null) {
            clearTarget();
        }
        if (view != null) {
            viewRef = new WeakReference<>(view);
        }
    }

    private void clearTarget() {
        final AnimatedView view = getTarget();
        final int size = mTuples.size();
        for (int i = 0; i < size; i++) {
            Animator anim = mTuples.get(i).animation;
            if (view.getAnimator() == anim) {
                anim.cancel();
            }
        }
        viewRef = null;
        lastMatch = null;
        runningAnimation = null;
    }

    /**
     * Called by View
     */
    public void setState(int[] state) {
        Tuple match = null;
        final int count = mTuples.size();
        for (int i = 0; i < count; i++) {
            final Tuple tuple = mTuples.get(i);
            if (StateSet.stateSetMatches(tuple.mSpecs, state)) {
                match = tuple;
                break;
            }
        }
        if (match == lastMatch) {
            return;
        }
        if (lastMatch != null) {
            cancel();
        }

        lastMatch = match;

        View view = (View) viewRef.get();
        if (match != null && view != null && view.getVisibility() == View.VISIBLE) {
            start(match);
        }
    }

    private void start(Tuple match) {
        match.getListener().onAnimationStart(match.animation);
        runningAnimation = match.animation;

        runningAnimation.start();
    }

    private void cancel() {
        if (runningAnimation != null) {
            final AnimatedView view = getTarget();
            if (view != null && view.getAnimator() == runningAnimation) {
                runningAnimation.cancel();
            }
            runningAnimation = null;
        }
    }

    /**
     * @hide
     */
    ArrayList<Tuple> getTuples() {
        return mTuples;
    }

    /**
     * If there is an animation running for a recent state change, ends it. <p> This causes the
     * animation to assign the end value(s) to the View.
     */
    public void jumpToCurrentState() {
        if (runningAnimation != null) {
            final AnimatedView view = getTarget();
            if (view != null && view.getAnimator() == runningAnimation) {
                runningAnimation.cancel();
            }
        }
    }

    static class Tuple {
        final int[] mSpecs;
        final Animator animation;
        private Animator.AnimatorListener listener;

        private Tuple(int[] specs, Animator Animation, Animator.AnimatorListener listener) {
            mSpecs = specs;
            animation = Animation;
            this.listener = listener;
        }

        int[] getSpecs() {
            return mSpecs;
        }

        Animator getAnimation() {
            return animation;
        }

        public Animator.AnimatorListener getListener() {
            return listener;
        }
    }

}
