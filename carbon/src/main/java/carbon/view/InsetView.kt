package carbon.view

import carbon.widget.OnInsetsChangedListener

/**
 * Interface of a view with insets. Used by layouts to handle standard system insets added by status
 * bar and navigation bar.
 */
interface InsetView {

    var insetLeft: Int

    var insetTop: Int

    var insetRight: Int

    var insetBottom: Int

    /**
     * Sets inset color. All insets will be colored
     *
     * @param color new inset color
     */
    var insetColor: Int

    /**
     * Sets insets
     *
     * @param left   has to be greater than or equal to 0 or [InsetView.INSET_NULL]
     * @param top    has to be greater than or equal to 0 or [InsetView.INSET_NULL]
     * @param right  has to be greater than or equal to 0 or [InsetView.INSET_NULL]
     * @param bottom has to be greater than or equal to 0 or [InsetView.INSET_NULL]
     */
    fun setInset(left: Int, top: Int, right: Int, bottom: Int)

    fun setOnInsetsChangedListener(onInsetsChangedListener: OnInsetsChangedListener)

    companion object {
        /**
         * Inset-not-set constant
         */
        const val INSET_NULL = -1
    }
}
