package carbon.component

import android.graphics.drawable.Drawable
import android.text.method.PasswordTransformationMethod
import android.view.ViewGroup
import carbon.component.databinding.CarbonRowIconpasswordBinding
import java.io.Serializable

interface IconPasswordItem : Serializable {
    val icon: Drawable?
    val hint: String?
    val text: String?
}

open class DefaultIconPasswordItem : IconPasswordItem {
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

open class IconPasswordRow<Type : IconPasswordItem>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_iconpassword) {
    private val binding = CarbonRowIconpasswordBinding.bind(view)

    var text: String
        get() = binding.carbonText.text.toString()
        set(text) = binding.carbonText.setText(text)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonIcon.setImageDrawable(data.icon)
        binding.carbonInput.label = data.hint
        binding.carbonText.transformationMethod = PasswordTransformationMethod()
        binding.carbonText.text = data.text ?: ""
    }

}
