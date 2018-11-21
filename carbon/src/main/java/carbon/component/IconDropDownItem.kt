package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R
import carbon.widget.DropDown

import java.io.Serializable

interface IconDropDownItem<Type : Serializable> : Serializable {
    val icon: Drawable?
    val hint: String?
    val items: Array<Type>
    val selectedItem: Type?
}

open class DefaultIconDropDownItem<Type : Serializable> : IconDropDownItem<Type> {
    override var icon: Drawable? = null
    override var hint: String? = null
    override lateinit var items: Array<Type>
    override var selectedItem: Type? = null

    constructor()

    constructor(icon: Drawable?, hint: String?, items: Array<Type>, selectedItem: Type?) {
        this.icon = icon
        this.hint = hint
        this.items = items
        this.selectedItem = selectedItem
    }
}

open class IconDropDownRow<Type : IconDropDownItem<ItemType>, ItemType : Serializable> : DataBindingComponent<Type> {

    val dropDown: DropDown<ItemType>

    var selectedItem: ItemType
        get() = dropDown.selectedItem
        set(value) {
            dropDown.selectedItem = value
        }

    constructor(parent: ViewGroup) : super(parent, R.layout.carbon_row_icondropdown) {
        dropDown = view.findViewById(R.id.carbon_dropDown)
    }

    override fun bind(data: Type) {
        super.bind(data)
        dropDown.setItems(data.items)
    }
}
