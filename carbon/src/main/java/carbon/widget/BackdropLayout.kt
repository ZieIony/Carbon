package carbon.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.view.ViewCompat

open class BackdropLayout : FrameLayout {

    enum class Side {
        LEFT, RIGHT, BOTTOM, TOP, START, END
    }

    private var backLayout: BackdropLayout_Back? = null
    private var frontLayout: BackdropLayout_Front? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is BackdropLayout_Back && backLayout == null) {
            backLayout = child
            super.addView(child, index, params)
        }
        if (child is BackdropLayout_Front && frontLayout == null) {
            frontLayout = child
            super.addView(child, index, params)
        }
    }

    private var opened: Boolean = false
    private var side: Side = Side.TOP

    fun toggleLayout(side: Side = this.side) {
        if (opened) {
            closeLayout()
        } else {
            openLayout(side)
        }
    }

    fun openLayout(side: Side) {
        var s = side
        if (s == Side.START) {
            s = if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                Side.RIGHT
            } else {
                Side.LEFT
            }
        } else if (s == Side.END) {
            s = if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                Side.LEFT
            } else {
                Side.RIGHT
            }
        }

        val params = frontLayout!!.layoutParams as MarginLayoutParams
        when (s) {
            Side.LEFT -> {
                val animator = ValueAnimator.ofFloat(frontLayout!!.translationX, backLayout!!.width.toFloat() - params.leftMargin)
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.duration = 200
                animator.addUpdateListener {
                    frontLayout!!.translationX = it.animatedValue as Float
                }
                animator.start()
            }
            Side.TOP -> {
                val animator = ValueAnimator.ofFloat(frontLayout!!.translationY, backLayout!!.height.toFloat() - params.topMargin)
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.duration = 200
                animator.addUpdateListener {
                    frontLayout!!.translationY = it.animatedValue as Float
                }
                animator.start()
            }
            Side.RIGHT -> {
                val animator = ValueAnimator.ofFloat(frontLayout!!.translationX, -backLayout!!.width.toFloat() - params.rightMargin)
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.duration = 200
                animator.addUpdateListener {
                    frontLayout!!.translationX = it.animatedValue as Float
                }
                animator.start()
            }
            else -> {
                val animator = ValueAnimator.ofFloat(frontLayout!!.translationY, -backLayout!!.height.toFloat() - params.bottomMargin)
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.duration = 200
                animator.addUpdateListener {
                    frontLayout!!.translationY = it.animatedValue as Float
                }
                animator.start()
            }
        }
        opened = true
        this.side = s
    }

    fun closeLayout() {
        if (side == Side.LEFT || side == Side.RIGHT) {
            val animator = ValueAnimator.ofFloat(frontLayout!!.translationX, 0.0f)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = 200
            animator.addUpdateListener {
                frontLayout!!.translationX = it.animatedValue as Float
            }
            animator.start()
        }else {
            val animator = ValueAnimator.ofFloat(frontLayout!!.translationY, 0.0f)
            animator.interpolator = AccelerateDecelerateInterpolator()
            animator.duration = 200
            animator.addUpdateListener {
                frontLayout!!.translationY = it.animatedValue as Float
            }
            animator.start()
        }
        opened = false
    }
}

open class BackdropLayout_Back : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
}

open class BackdropLayout_Front : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
}
