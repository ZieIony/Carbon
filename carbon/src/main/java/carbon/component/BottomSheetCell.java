package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonBottomsheetCellBinding;
import carbon.recycler.RowFactory;
import carbon.widget.ImageView;

public class BottomSheetCell extends DataBindingComponent<MenuItem> {
    public static final RowFactory<MenuItem> FACTORY = BottomSheetCell::new;

    public BottomSheetCell(ViewGroup parent) {
        super(parent, R.layout.carbon_bottomsheet_cell);
    }

    @Override
    public void bind(MenuItem data) {
        super.bind(data);
        ImageView itemIcon = ((CarbonBottomsheetCellBinding) this.getBinding()).carbonItemIcon;
        itemIcon.setImageDrawable(data.getIcon(getView().getContext()));
        if (data.getIconTint() != null)
            itemIcon.setTint(data.getIconTint());
    }
}
