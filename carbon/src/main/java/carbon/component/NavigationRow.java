package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonNavigationRowBinding;
import carbon.recycler.RowFactory;
import carbon.widget.ImageView;

public class NavigationRow extends DataBindingComponent<MenuItem> {
    public static final RowFactory<MenuItem> FACTORY = NavigationRow::new;

    public NavigationRow(ViewGroup parent) {
        super(parent, R.layout.carbon_navigation_row);
    }

    @Override
    public void bind(MenuItem data) {
        super.bind(data);
        ImageView itemIcon = ((CarbonNavigationRowBinding) this.getBinding()).carbonItemIcon;
        itemIcon.setImageDrawable(data.getIcon(getView().getContext()));
        if (data.getIconTint() != null)
            itemIcon.setTint(data.getIconTint());
    }
}
