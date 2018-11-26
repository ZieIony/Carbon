package carbon.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator

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
            Side.LEFT -> frontLayout!!.animate().translationX(backLayout!!.width.toFloat() - params.leftMargin).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(200).start()
            Side.TOP -> frontLayout!!.animate().translationY(backLayout!!.height.toFloat() - params.topMargin).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(200).start()
            Side.RIGHT -> frontLayout!!.animate().translationX(-backLayout!!.width.toFloat() - params.rightMargin).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(200).start()
            else -> frontLayout!!.animate().translationY(-backLayout!!.height.toFloat() - params.bottomMargin).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(200).start()
        }
        opened = true
        this.side = side
    }

    fun closeLayout() {
        frontLayout!!.animate().translationY(0.0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(200).start()
        frontLayout!!.animate().translationX(0.0f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(200).start()
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
