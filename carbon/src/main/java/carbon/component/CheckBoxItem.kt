package carbon.component

import android.view.ViewGroup
import carbon.R
import carbon.databinding.CarbonRowIconcheckboxBinding
import java.io.Serializable

interface CheckBoxItem : Serializable {
    val isChecked: Boolean
    val text: String?
}

open class DefaultCheckBoxItem : CheckBoxItem {
    override var isChecked: Boolean = false
    override var text: String? = null

    constructor()

    constructor(isChecked: Boolean, text: String?) {
        this.isChecked = isChecked
        this.text = text
    }
}

open class CheckBoxRow<Type : CheckBoxItem>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_iconcheckbox) {
    private val binding = CarbonRowIconcheckboxBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonCheckBox.isChecked = data.isChecked
    }
}
