package carbon.component

import android.view.ViewGroup
import carbon.component.databinding.CarbonRowTextBinding


open class TextRow<String>(parent: ViewGroup) : LayoutComponent<String>(parent, R.layout.carbon_row_text) {
    private val binding = CarbonRowTextBinding.bind(view)

    override fun bind(data: String) {
        super.bind(data)
        binding.carbonText.text = data.toString()
    }
}
