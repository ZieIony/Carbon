package carbon.component

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import carbon.R
import carbon.databinding.CarbonRowIconedittextBinding
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

open class IconEditTextRow<Type : IconEditTextItem>(parent: ViewGroup)
    : LayoutComponent<Type>(parent, R.layout.carbon_row_iconedittext) {

    private var binding = CarbonRowIconedittextBinding.bind(view)

    var text: String
        get() = binding.carbonText.text.toString()
        set(text) = binding.carbonText.setText(text)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonIcon.setImageDrawable(data.icon)
        binding.carbonInput.label = data.hint
        binding.carbonText.text = data.text ?: ""
    }
}
