package carbon.view

import carbon.widget.AutoSizeTextMode

/**
 * Interface of a text view capable of automatic text size adjusting.
 */
interface AutoSizeTextView {

    /**
     * @return current auto text size mode
     */
    /**
     * @param autoSizeText new text size mode
     */
    var autoSizeText: AutoSizeTextMode

    /**
     * Gets minimum text size the view allows
     *
     * @return minimum text size
     */
    /**
     * Sets minimum text size the view allows
     *
     * @param minTextSize new minimum text size
     */
    var minTextSize: Float

    /**
     * Gets maximum text size the view allows
     *
     * @return maximum text size
     */
    /**
     * Sets maximum text size the view allows
     *
     * @param maxTextSize maximum text size
     */
    var maxTextSize: Float

    /**
     * Gets automatic text size granularity.
     *
     * @return granularity
     */
    /**
     * Sets automatic text size granularity. Text can only take values which are equal to minSize *
     * [0 to N] * granularity or maxSize. This parameter helps to keep automatic text sizes count
     * low and easy to predict. Setting granularity to a larger value speeds up the adjustment
     * process.
     *
     * @param autoSizeStepGranularity granularity
     */
    var autoSizeStepGranularity: Int

    /**
     * This method is not compatible with the official API, but allows setting even more granular
     * values.
     *
     * @param autoSizeStepGranularity granularity
     */
    fun setAutoSizeStepGranularity(autoSizeStepGranularity: Float)
}
