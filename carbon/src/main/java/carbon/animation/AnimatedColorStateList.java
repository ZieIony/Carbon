package carbon.animation;

import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.StateSet;
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
    private ValueAnimator colorAnimation = null;
    private int animatedColor;

    public static AnimatedColorStateList fromList(ColorStateList list, ValueAnimator.AnimatorUpdateListener listener) {
        int[][] mStateSpecs; // must be parallel to mColors
        int[] mColors;      // must be parallel to mStateSpecs
        int mDefaultColor;

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
            AnimatedColorStateList animatedColorStateList = new AnimatedColorStateList(mStateSpecs, mColors, listener);
            mDefaultColorField.set(animatedColorStateList, mDefaultColor);
            return animatedColorStateList;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public AnimatedColorStateList(int[][] states, int[] colors, final ValueAnimator.AnimatorUpdateListener listener) {
        super(states, colors);
        this.states = states;
        colorAnimation = ValueAnimator.ofInt(0, 0);
        colorAnimation.setEvaluator(new ArgbEvaluator());
        colorAnimation.setDuration(200);
        colorAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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
            if (Arrays.equals(stateSet, currentState)) {
                return animatedColor;
            }
        }
        return super.getColorForState(stateSet, defaultColor);
    }

    public void setState(int[] newState) {
        if (Arrays.equals(newState, currentState))
            return;
        if (currentState != null)
            cancel();

        for (final int[] state : states) {
            if (StateSet.stateSetMatches(state, newState)) {
                int firstColor = super.getColorForState(currentState, getDefaultColor());
                int secondColor = super.getColorForState(newState, getDefaultColor());
                colorAnimation.setIntValues(firstColor, secondColor);
                currentState = newState;
                animatedColor = firstColor;
                colorAnimation.start();
                return;
            }
        }

        currentState = newState;
    }

    private void cancel() {
        colorAnimation.cancel();
    }

    public void jumpToCurrentState() {
        colorAnimation.end();
    }

    public static final Parcelable.Creator<AnimatedColorStateList> CREATOR =
            new Parcelable.Creator<AnimatedColorStateList>() {
                @Override
                public AnimatedColorStateList[] newArray(int size) {
                    return new AnimatedColorStateList[size];
                }

                @Override
                public AnimatedColorStateList createFromParcel(Parcel source) {
                    final int N = source.readInt();
                    final int[][] stateSpecs = new int[N][];
                    for (int i = 0; i < N; i++) {
                        stateSpecs[i] = source.createIntArray();
                    }
                    final int[] colors = source.createIntArray();
                    return AnimatedColorStateList.fromList(new ColorStateList(stateSpecs, colors), null);
                }
            };
}
