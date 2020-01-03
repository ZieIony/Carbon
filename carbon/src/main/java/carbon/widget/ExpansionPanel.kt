package carbon.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import carbon.R

open class ExpansionPanel : LinearLayout {

    interface OnExpandedStateChangedListerner {
        fun onExpandedStateChanged(isExpanded: Boolean)
    }

    private var expandedIndicator: ImageView? = null
    private var expanded = true

    var onExpandedStateChangedListerner:OnExpandedStateChangedListerner? = null

    private var header: ExpansionPanel_Header? = null
    private var content: ExpansionPanel_Content? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        orientation = android.widget.LinearLayout.VERTICAL
        setExpanded(true)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is ExpansionPanel_Header && this.header == null) {
            this.header = child
            expandedIndicator = child.findViewById(R.id.carbon_panelExpandedIndicator)
            child.setOnClickListener {
                toggle()
                onExpandedStateChangedListerner?.onExpandedStateChanged(expanded)
            }
            setExpanded(expanded)
            super.addView(child, index, params)
        }
        if (child is ExpansionPanel_Content && this.content == null) {
            this.content = child
            setExpanded(expanded)
            super.addView(child, index, params)
        }
    }

    fun expand() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 200
        animator.addUpdateListener { animation: ValueAnimator ->
            expandedIndicator?.let {
                it.rotation = 180 * animation.animatedValue as Float
                it.postInvalidate()
            }
        }
        animator.start()
        this.content!!.visibility = View.VISIBLE
        expanded = true
    }

    fun collapse() {
        val animator = ValueAnimator.ofFloat(1f, 0f)
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 200
        animator.addUpdateListener { animation: ValueAnimator ->
            expandedIndicator?.let {
                it.rotation = 180 * animation.animatedValue as Float
                it.postInvalidate()
            }
        }
        animator.start()
        this.content!!.visibility = View.GONE
        expanded = false
    }

    fun toggle() {
        if (isExpanded()) {
            collapse()
        } else {
            expand()
        }
    }

    fun setExpanded(expanded: Boolean) {
        expandedIndicator?.rotation = if (expanded) 180.0f else 0.0f
        this.content?.visibility = if (expanded) View.VISIBLE else View.GONE
        this.expanded = expanded
    }

    fun isExpanded(): Boolean {
        return expanded
    }
}

open class ExpansionPanel_Header : FrameLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        View.inflate(context, R.layout.carbon_expansionpanel_header, this)
    }
}

open class ExpansionPanel_Content : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
}
