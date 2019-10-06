package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R
import carbon.widget.DropDown

import java.io.Serializable

interface IconDropDownItem : Serializable {
    val icon: Drawable?
    val hint: String?
    val items: Array<Serializable>
    val selectedItem: Serializable?
}

open class DefaultIconDropDownItem<Type : Serializable> : IconDropDownItem {
    override var icon: Drawable? = null
    override var hint: String? = null
    override lateinit var items: Array<Serializable>
    override var selectedItem: Serializable? = null

    constructor()

    constructor(icon: Drawable?, hint: String?, items: Array<Type>, selectedItem: Type?) {
        this.icon = icon
        this.hint = hint
        this.items = arrayOf(*items)
        this.selectedItem = selectedItem
    }
}

open class IconDropDownRow<Type : IconDropDownItem, ItemType : Serializable> : DataBindingComponent<Type> {

    val dropDown: DropDown

    var selectedItem: ItemType
        get() = dropDown.getSelectedItem()
        set(value) {
            dropDown.setSelectedItem(value)
        }

    constructor(parent: ViewGroup) : super(parent, R.layout.carbon_row_icondropdown) {
        dropDown = view.findViewById(R.id.carbon_dropDown)
    }

    override fun bind(data: Type) {
        super.bind(data)
        dropDown.setItems(data.items)
    }
}
