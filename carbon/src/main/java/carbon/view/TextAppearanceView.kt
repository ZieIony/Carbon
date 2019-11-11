package carbon.view

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.TextPaint

interface TextAppearanceView {

    var text: CharSequence

    val paint: TextPaint

    fun setTypeface(typeface: Typeface, style: Int)

    fun setAllCaps(allCaps: Boolean)

    fun setTextColor(textColor: ColorStateList)

    fun setTextSize(size: Float)
}
