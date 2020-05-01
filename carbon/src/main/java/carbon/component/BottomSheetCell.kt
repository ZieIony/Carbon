package carbon.component

import android.view.ViewGroup
import carbon.R
import carbon.beta.BottomSheetLayout
import carbon.databinding.CarbonBottomsheetCellBinding

class BottomSheetCell(parent: ViewGroup) : LayoutComponent<BottomSheetLayout.Item>(parent, R.layout.carbon_bottomsheet_cell) {
    private val binding = CarbonBottomsheetCellBinding.bind(view)

    override fun bind(data: BottomSheetLayout.Item) {
        binding.carbonItemIcon.setImageDrawable(data.icon)
        if (data.iconTintList != null)
            binding.carbonItemIcon.setTintList(data.iconTintList)
        binding.carbonItemText.text = data.title
    }
}