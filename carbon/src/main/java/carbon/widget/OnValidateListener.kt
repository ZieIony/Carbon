package carbon.widget

interface OnValidateListener {
    fun onValidate(): Boolean
}

interface OnValidChangedListener {
    fun onValidChanged(valid: Boolean)
}