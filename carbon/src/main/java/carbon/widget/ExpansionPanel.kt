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
    private var _expanded = true

    var isExpanded
        get() = _expanded
        set(expanded) {
            expandedIndicator?.rotation = if (expanded) 180.0f else 0.0f
            this.content?.visibility = if (expanded) View.VISIBLE else View.GONE
            this._expanded = expanded
        }

    var onExpandedStateChangedListerner: OnExpandedStateChangedListerner? = null

    private var header: ExpansionPanel_Header? = null
    private var content: ExpansionPanel_Content? = null

    constructor(context: Context) : super(context) {
        initExpansionPanel(null, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initExpansionPanel(attrs, 0, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initExpansionPanel(attrs, defStyleAttr, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initExpansionPanel(attrs, defStyleAttr, defStyleRes)
    }

    private fun initExpansionPanel(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        orientation = android.widget.LinearLayout.VERTICAL

        val a = context.obtainStyledAttributes(attrs, R.styleable.ExpansionPanel, defStyleAttr, defStyleRes)
        isExpanded = a.getBoolean(R.styleable.ExpansionPanel_carbon_expanded, true)
        a.recycle()
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is ExpansionPanel_Header && this.header == null) {
            this.header = child
            expandedIndicator = child.findViewById(R.id.carbon_panelExpandedIndicator)
            child.setOnClickListener {
                toggle()
                onExpandedStateChangedListerner?.onExpandedStateChanged(_expanded)
            }
            isExpanded = _expanded
            super.addView(child, index, params)
        }
        if (child is ExpansionPanel_Content && this.content == null) {
            this.content = child
            isExpanded = _expanded
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
        _expanded = true
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
        _expanded = false
    }

    fun toggle() {
        if (isExpanded) {
            collapse()
        } else {
            expand()
        }
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
