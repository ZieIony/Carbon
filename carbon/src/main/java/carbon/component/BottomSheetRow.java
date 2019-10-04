package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.beta.BottomSheetLayout;
import carbon.databinding.CarbonBottomsheetRowBinding;
import carbon.widget.ImageView;

public class BottomSheetRow extends DataBindingComponent<BottomSheetLayout.Item> {

    public BottomSheetRow(ViewGroup parent) {
        super(parent, R.layout.carbon_bottomsheet_row);
    }

    @Override
    public void bind(BottomSheetLayout.Item data) {
        super.bind(data);
        ImageView itemIcon = ((CarbonBottomsheetRowBinding) this.getBinding()).carbonItemIcon;
        if (data.getIconTintList() != null)
            itemIcon.setTintList(data.getIconTintList());
    }

}
