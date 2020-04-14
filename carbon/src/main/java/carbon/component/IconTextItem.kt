package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R
import carbon.databinding.CarbonRowIcontextBinding

import java.io.Serializable

interface IconTextItem : Serializable {
    val icon: Drawable?
    val text: String?
}

open class DefaultIconTextItem : IconTextItem {
    override var icon: Drawable? = null
    override var text: String? = ""

    constructor()

    constructor(icon: Drawable, text: String) {
        this.icon = icon
        this.text = text
    }
}

open class IconTextRow<Type : IconTextItem>(parent: ViewGroup)
    : LayoutComponent<Type>(parent, R.layout.carbon_row_icontext) {
    private val binding = CarbonRowIcontextBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)

        binding.carbonIcon.setImageDrawable(data.icon)
        binding.carbonText.text = data.text ?: ""
    }
}
