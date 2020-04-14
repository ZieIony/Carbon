package carbon.component

import android.view.ViewGroup
import carbon.R
import carbon.databinding.CarbonRowHeaderBinding
import carbon.databinding.CarbonRowPaddedheaderBinding
import java.io.Serializable

interface HeaderItem : Serializable {
    val text: String?
}

open class DefaultHeaderItem : HeaderItem {
    override var text: String? = null

    constructor()

    constructor(text: String?) {
        this.text = text
    }
}

open class HeaderRow<Type : DefaultHeaderItem>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_header) {
    private val binding = CarbonRowHeaderBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonText.text = data.text.toString()
    }
}

open class PaddedHeaderRow<Type : DefaultHeaderItem>(parent: ViewGroup) : LayoutComponent<Type>(parent, R.layout.carbon_row_paddedheader) {
    private val binding = CarbonRowPaddedheaderBinding.bind(view)

    override fun bind(data: Type) {
        super.bind(data)
        binding.carbonText.text = data.text.toString()
    }
}
