package carbon.component

import android.view.ViewGroup
import carbon.R
import carbon.beta.BottomSheetLayout
import carbon.databinding.CarbonBottomsheetRowBinding

class BottomSheetRow(parent: ViewGroup) : LayoutComponent<BottomSheetLayout.Item>(parent, R.layout.carbon_bottomsheet_row) {
    private val binding = CarbonBottomsheetRowBinding.bind(view)

    override fun bind(data: BottomSheetLayout.Item) {
        binding.carbonItemIcon.setImageDrawable(data.icon)
        if (data.iconTintList != null)
            binding.carbonItemIcon.setTintList(data.iconTintList)
        binding.carbonItemText.text = data.title
    }
}