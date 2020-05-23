package carbon.component

import android.content.Context
import android.view.View

abstract class Component<Type> {
    lateinit var view: View
        protected set

    val context: Context
        get() = view.context

    private var _data: Type? = null
    var data: Type
        get() = _data!!
        set(value) {
            _data = value
            bind(value)
        }

    protected open fun bind(data: Type) {
    }
}