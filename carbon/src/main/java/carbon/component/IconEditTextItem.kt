package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R
import carbon.widget.EditText

import java.io.Serializable

interface IconEditTextItem : Serializable {
    val icon: Drawable?
    val hint: String?
    val text: String?
}

open class DefaultIconEditTextItem : IconEditTextItem {
    override var icon: Drawable? = null
    override var hint: String? = null
    override var text: String? = null

    constructor()

    constructor(icon: Drawable?, hint: String?, text: String?) {
        this.icon = icon
        this.hint = hint
        this.text = text
    }
}

open class IconEditTextRow<Type : IconEditTextItem> : DataBindingComponent<Type> {

    val editText: EditText

    var text: String
        get() = editText.text.toString()
        set(text) = editText.setText(text)

    constructor(parent: ViewGroup) : super(parent, R.layout.carbon_row_iconedittext) {
        this.editText = view.findViewById(R.id.carbon_text)
    }

}
