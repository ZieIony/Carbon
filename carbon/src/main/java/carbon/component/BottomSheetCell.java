package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.beta.BottomSheetLayout;
import carbon.databinding.CarbonBottomsheetCellBinding;

public class BottomSheetCell extends LayoutComponent<BottomSheetLayout.Item> {

    private final CarbonBottomsheetCellBinding binding;

    public BottomSheetCell(ViewGroup parent) {
        super(parent, R.layout.carbon_bottomsheet_cell);
        binding = CarbonBottomsheetCellBinding.bind(getView());
    }

    @Override
    public void bind(BottomSheetLayout.Item data) {
        super.bind(data);
        binding.carbonItemIcon.setImageDrawable(data.getIcon());
        if (data.getIconTintList() != null)
            binding.carbonItemIcon.setTintList(data.getIconTintList());
        binding.carbonItemText.setText(data.getTitle());
    }
}
