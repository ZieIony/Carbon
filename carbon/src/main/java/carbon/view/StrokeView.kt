package carbon.view

import android.content.res.ColorStateList

interface StrokeView {

    var stroke: ColorStateList?

    fun setStroke(color: Int)

    var strokeWidth: Float

}
