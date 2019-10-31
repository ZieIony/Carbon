package carbon.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import carbon.R
import kotlin.math.max


open class Badge : Label {
    constructor(context: Context) : super(context, null, R.attr.carbon_badgeStyle)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, R.attr.carbon_badgeStyle)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(max(measuredWidth, measuredHeight), measuredHeight)
    }
}