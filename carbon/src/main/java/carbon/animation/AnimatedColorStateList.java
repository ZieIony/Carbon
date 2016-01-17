package carbon.animation;

import android.content.res.ColorStateList;
import android.util.StateSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.ValueAnimator;

import java.lang.reflect.Field;
import java.util.Arrays;

import carbon.internal.ArgbEvaluator;

/**
 * Created by Marcin on 2016-01-16.
 */
public class AnimatedColorStateList extends ColorStateList {
    private final int[][] states;
    private int[] currentState = null;
    private ValueAnimator runningAnimation = null;
    private AnimatedView view;
    int animatedColor;

    public static AnimatedColorStateList fromList(ColorStateList list, final AnimatedView target, final ValueAnimator.AnimatorUpdateListener listener) {
        int[][] mStateSpecs = new int[0][]; // must be parallel to mColors
        int[] mColors = new int[0];      // must be parallel to mStateSpecs
        int mDefaultColor = 0xffff0000;

        try {
            Field mStateSpecsField = ColorStateList.class.getDeclaredField("mStateSpecs");
            mStateSpecsField.setAccessible(true);
            mStateSpecs = (int[][]) mStateSpecsField.get(list);
            Field mColorsField = ColorStateList.class.getDeclaredField("mColors");
            mColorsField.setAccessible(true);
            mColors = (int[]) mColorsField.get(list);
            Field mDefaultColorField = ColorStateList.class.getDeclaredField("mDefaultColor");
            mDefaultColorField.setAccessible(true);
            mDefaultColor = (int) mDefaultColorField.get(list);
            AnimatedColorStateList animatedColorStateList = new AnimatedColorStateList(mStateSpecs, mColors, target, listener);
            mDefaultColorField.set(animatedColorStateList, mDefaultColor);
            return animatedColorStateList;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public AnimatedColorStateList(int[][] states, int[] colors, final AnimatedView target, final ValueAnimator.AnimatorUpdateListener listener) {
        super(states, colors);
        view = target;
        this.states = states;
        runningAnimation = ValueAnimator.ofInt(0, 0);
        runningAnimation.setEvaluator(new ArgbEvaluator());
        runningAnimation.setDuration(300);
        runningAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        runningAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                synchronized (AnimatedColorStateList.this) {
                    animatedColor = (int) animation.getAnimatedValue();
                    listener.onAnimationUpdate(animation);
                }
            }
        });
    }

    @Override
    public int getColorForState(int[] stateSet, int defaultColor) {
        synchronized (AnimatedColorStateList.this) {
            if (currentState != null && StateSet.stateSetMatches(stateSet, currentState))
                return animatedColor;
        }
        return super.getColorForState(stateSet, defaultColor);
    }

    public void setState(int[] newState) {
        if (Arrays.equals(newState, currentState))
            return;
        if (currentState != null)
            cancel();

        for (int i = 0; i < states.length; i++) {
            final int[] state = states[i];
            if (StateSet.stateSetMatches(state, newState)) {

                if (view != null && ((View) view).getVisibility() == View.VISIBLE) {
                    int firstColor = super.getColorForState(currentState, getDefaultColor());
                    int secondColor = super.getColorForState(newState, getDefaultColor());
                    runningAnimation.setIntValues(firstColor, secondColor);
                    runningAnimation.start();
                }
                currentState = newState;
                return;
            }
        }

        currentState = newState;
    }

    private void cancel() {
        runningAnimation.cancel();
    }

    public void jumpToCurrentState() {
        runningAnimation.cancel();
    }
}
