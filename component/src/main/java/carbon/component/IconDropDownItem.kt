package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.component.databinding.CarbonRowIcondropdownBinding
import carbon.widget.DropDown
import carbon.widget.ImageView
import carbon.widget.InputLayout
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

open class IconDropDownRow<Type : IconDropDownItem, ItemType : Serializable>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_icondropdown) {

    private val binding = CarbonRowIcondropdownBinding.bind(view)

    var selectedItem: ItemType
        get() = binding.carbonDropDown.getSelectedItem()
        set(value) {
            binding.carbonDropDown.setSelectedItem(value)
        }

    override fun bind(data: Type) {
        binding.carbonIcon.setImageDrawable(data.icon)
        binding.carbonInput.label = data.hint
        binding.carbonDropDown.setItems(data.items)
        binding.carbonDropDown.text = data.selectedItem.toString()
    }
}
