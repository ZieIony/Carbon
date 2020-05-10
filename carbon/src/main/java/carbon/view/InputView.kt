package carbon.view

import carbon.widget.OnValidChangedListener
import carbon.widget.OnValidateListener

/**
 * Interface of a view with input, which can be validated. Used by [ ]
 */
interface InputView : ValidStateView {
    /**
     * Performs validation
     */
    fun validate()

    /**
     * Adds a listener
     *
     * @param listener cannot be null
     */
    fun addOnValidateListener(listener: OnValidateListener)

    /**
     * Removes a listener
     *
     * @param listener cannot be null
     */
    fun removeOnValidateListener(listener: OnValidateListener)

    /**
     * Removes all listeners
     */
    fun clearOnValidateListeners()

    /**
     * Adds a listener
     *
     * @param listener cannot be null
     */
    fun addOnValidChangedListener(listener: OnValidChangedListener)

    /**
     * Removes a listener
     *
     * @param listener cannot be null
     */
    fun removeOnValidChangedListener(listener: OnValidChangedListener)

    /**
     * Removes all listeners
     */
    fun clearOnValidChangedListeners()
}
