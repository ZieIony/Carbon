package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.beta.BottomSheetLayout;
import carbon.databinding.CarbonBottomsheetCellBinding;
import carbon.widget.ImageView;

public class BottomSheetCell extends DataBindingComponent<BottomSheetLayout.Item> {

    public BottomSheetCell(ViewGroup parent) {
        super(parent, R.layout.carbon_bottomsheet_cell);
    }

    @Override
    public void bind(BottomSheetLayout.Item data) {
        super.bind(data);
        ImageView itemIcon = ((CarbonBottomsheetCellBinding) this.getBinding()).carbonItemIcon;
        if (data.getIconTintList() != null)
            itemIcon.setTintList(data.getIconTintList());
    }
}
