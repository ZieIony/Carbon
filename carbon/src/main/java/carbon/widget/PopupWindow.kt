package carbon.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

import carbon.R
import carbon.animation.AnimatedView

open class PopupWindow : android.widget.PopupWindow {

    private var content: View? = null
    protected var anchorView: View? = null

    constructor(context: Context) : super(View.inflate(context, R.layout.carbon_popupwindow, null)) {
        super.getContentView().layoutParams = ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        setBackgroundDrawable(ColorDrawable(context.resources.getColor(android.R.color.transparent)))
        isTouchable = true
        isFocusable = true
        isOutsideTouchable = true
        animationStyle = 0
    }

    constructor(content: View) : this(content.context) {
        contentView = content
    }

    override fun setContentView(contentView: View) {
        if (super.getContentView() == null) {
            super.setContentView(contentView)
        } else {
            content = contentView.also {
                it.visibility = View.INVISIBLE
            }
            val container = super.getContentView().findViewById<FrameLayout>(R.id.carbon_popupContainer)
            container.removeAllViews()
            container.addView(content)
        }
    }

    override fun getContentView(): View {
        return content ?: super.getContentView()
    }

    @JvmOverloads
    fun show(anchor: View, gravity: Int = Gravity.START or Gravity.TOP): Boolean {
        anchorView = anchor

        super.showAtLocation(anchor, gravity, 0, 0)

        update()

        if (content is AnimatedView)
            (content as AnimatedView).animateVisibility(View.VISIBLE)

        return true
    }

    @JvmOverloads
    fun showImmediate(anchor: View, gravity: Int = Gravity.START or Gravity.TOP): Boolean {
        anchorView = anchor

        super.showAtLocation(anchor, gravity, 0, 0)

        update()

        return true
    }

    override fun update() {
        if (anchorView == null)
            return

        super.getContentView().measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val left = -super.getContentView().paddingLeft
        val top = -super.getContentView().paddingTop
        update(anchorView, left, top, super.getContentView().measuredWidth, super.getContentView().measuredHeight)

        super.update()
    }

    override fun dismiss() {
        if (content is AnimatedView) {
            val animator = (content as AnimatedView).animateVisibility(View.INVISIBLE)
            if (animator != null) {
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super@PopupWindow.dismiss()
                    }
                })
            } else {
                super.dismiss()
            }
        } else {
            super.dismiss()
        }
    }

    fun dismissImmediate() = super.dismiss()
}
