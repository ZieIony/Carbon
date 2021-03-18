package carbon.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.res.ColorStateList
import android.os.Parcel
import android.os.Parcelable
import android.util.StateSet
import android.view.animation.AccelerateDecelerateInterpolator
import carbon.internal.ArgbEvaluator
import java.lang.reflect.Field
import java.util.*

class AnimatedColorStateList(private val states: Array<IntArray>, colors: IntArray?, listener: AnimatorUpdateListener?) : ColorStateList(states, colors) {
    private var currentState = IntArray(0)
    private val colorAnimation: ValueAnimator
    private var animatedColor = 0

    companion object {
        private var mStateSpecsField: Field? = null
        private var mColorsField: Field? = null
        private var mDefaultColorField: Field? = null
        @JvmStatic
        fun fromList(list: ColorStateList?, listener: AnimatorUpdateListener?): AnimatedColorStateList? {
            val mStateSpecs: Array<IntArray> // must be parallel to mColors
            val mColors: IntArray // must be parallel to mStateSpecs
            val mDefaultColor: Int
            try {
                mStateSpecs = mStateSpecsField!![list] as Array<IntArray>
                mColors = mColorsField!![list] as IntArray
                mDefaultColor = mDefaultColorField!![list] as Int
                val animatedColorStateList = AnimatedColorStateList(mStateSpecs, mColors, listener)
                mDefaultColorField!![animatedColorStateList] = mDefaultColor
                return animatedColorStateList
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            return null
        }

        val CREATOR: Parcelable.Creator<AnimatedColorStateList?> = object : Parcelable.Creator<AnimatedColorStateList?> {
            override fun newArray(size: Int): Array<AnimatedColorStateList?> {
                return arrayOfNulls(size)
            }

            override fun createFromParcel(source: Parcel): AnimatedColorStateList? {
                val N = source.readInt()
                val stateSpecs = arrayOfNulls<IntArray>(N)
                for (i in 0 until N) {
                    stateSpecs[i] = source.createIntArray()
                }
                val colors = source.createIntArray()
                return fromList(ColorStateList(stateSpecs, colors), null)
            }
        }

        init {
            try {
                mStateSpecsField = ColorStateList::class.java.getDeclaredField("mStateSpecs")
                mStateSpecsField!!.isAccessible = true
                mColorsField = ColorStateList::class.java.getDeclaredField("mColors")
                mColorsField!!.isAccessible = true
                mDefaultColorField = ColorStateList::class.java.getDeclaredField("mDefaultColor")
                mDefaultColorField!!.isAccessible = true
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
        }
    }

    override fun getColorForState(stateSet: IntArray?, defaultColor: Int): Int {
        synchronized(this@AnimatedColorStateList) {
            if (Arrays.equals(stateSet, currentState) && colorAnimation.isRunning) {
                return animatedColor
            }
        }
        return super.getColorForState(stateSet, defaultColor)
    }

    fun setState(newState: IntArray) {
        synchronized(this@AnimatedColorStateList) {
            if (Arrays.equals(newState, currentState)) return
            colorAnimation.end()
            if (currentState.size != 0) {
                for (state in states) {
                    if (StateSet.stateSetMatches(state, newState)) {
                        val firstColor = getColorForState(currentState, defaultColor)
                        val secondColor = super.getColorForState(newState, defaultColor)
                        colorAnimation.setIntValues(firstColor, secondColor)
                        currentState = newState
                        animatedColor = firstColor
                        colorAnimation.start()
                        return
                    }
                }
            }
            currentState = newState
        }
    }

    fun jumpToCurrentState() {
        colorAnimation.end()
    }

    init {
        colorAnimation = ValueAnimator.ofInt(0, 0)
        colorAnimation.setEvaluator(ArgbEvaluator())
        colorAnimation.duration = 200
        colorAnimation.interpolator = AccelerateDecelerateInterpolator()
        colorAnimation.addUpdateListener { animation: ValueAnimator ->
            synchronized(this@AnimatedColorStateList) {
                animatedColor = animation.animatedValue as Int
                listener!!.onAnimationUpdate(animation)
            }
        }
        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatedColor = colorAnimation.animatedValue as Int
                listener!!.onAnimationUpdate(colorAnimation)
            }
        })
    }
}