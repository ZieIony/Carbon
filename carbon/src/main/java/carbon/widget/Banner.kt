package carbon.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import carbon.R

open class Banner : LinearLayout {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initBanner(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initBanner(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initBanner(attrs)
    }

    private val buttonContainer: LinearLayout
    private val contentContainer: View
    private var finishedInflating = false
    private val iconImageView: ImageView
    private val textTextView: TextView

    init {
        View.inflate(context, R.layout.carbon_banner, this)
        finishedInflating = true
        buttonContainer = findViewById(R.id.carbon_banner_buttons)
        contentContainer = findViewById(R.id.carbon_banner_content)
        iconImageView = findViewById(R.id.carbon_bannerIcon)
        textTextView = findViewById(R.id.carbon_bannerText)
    }

    private fun initBanner(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.Banner)

        setIcon(a.getDrawable(R.styleable.Banner_carbon_icon))
        setText(a.getString(R.styleable.Banner_android_text))

        a.recycle()
    }

    private fun setText(string: String?) {
        textTextView.text = string ?: ""
    }

    private fun setIcon(icon: Drawable?) {
        iconImageView.visibility = if (icon == null) View.GONE else View.VISIBLE
        iconImageView.setImageDrawable(icon)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (!finishedInflating) {
            super.addView(child, index, params)
        } else if (child is Button) {
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            if (buttonContainer.childCount > 0) {
                layoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)
                if (Build.VERSION.SDK_INT >= 17)
                    layoutParams.marginStart = resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)
                layoutParams.leftMargin = resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)
            }
            buttonContainer.addView(child, index, layoutParams)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (buttonContainer.width > width - contentContainer.width) {
            buttonContainer.layoutParams.width = LayoutParams.MATCH_PARENT
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        if (buttonContainer.width >= width) {
            buttonContainer.orientation = android.widget.LinearLayout.VERTICAL
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    fun dismiss() {
        animate().translationY((-height).toFloat()).setDuration(200).setInterpolator(AccelerateInterpolator()).start()
    }
}
