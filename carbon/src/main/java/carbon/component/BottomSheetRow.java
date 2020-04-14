package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.beta.BottomSheetLayout;
import carbon.databinding.CarbonBottomsheetRowBinding;

public class BottomSheetRow extends LayoutComponent<BottomSheetLayout.Item> {

    private final CarbonBottomsheetRowBinding binding;

    public BottomSheetRow(ViewGroup parent) {
        super(parent, R.layout.carbon_bottomsheet_row);
        binding = CarbonBottomsheetRowBinding.bind(getView());
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
