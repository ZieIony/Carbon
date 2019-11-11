package carbon.view

import android.graphics.Rect

/**
 * Interface of a View with support for touch margins. Touch margin is a feature which allows a view
 * to handle events which happened outside of that view. These event have to happen inside the
 * view's parent.
 */
interface TouchMarginView {

    val touchMargin: Rect

    fun setTouchMargin(left: Int, top: Int, right: Int, bottom: Int)

    fun setTouchMarginLeft(margin: Int)

    fun setTouchMarginTop(margin: Int)

    fun setTouchMarginRight(margin: Int)

    fun setTouchMarginBottom(margin: Int)
}
