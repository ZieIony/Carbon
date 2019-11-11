package carbon.widget

import android.widget.Checkable

/**
 * Interface definition for a callback to be invoked when the checked state of a checkable view
 * changed.
 */
interface OnCheckedChangeListener {
    /**
     * Called when the checked state of a checkable view has changed.
     *
     * @param view      The checkable view whose state has changed.
     * @param isChecked The new checked state of buttonView.
     */
    fun onCheckedChanged(view: Checkable, isChecked: Boolean)
}
