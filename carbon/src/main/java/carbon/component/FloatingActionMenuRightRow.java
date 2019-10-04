package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonFloatingactionmenuRightBinding;
import carbon.widget.FloatingActionButton;
import carbon.widget.FloatingActionMenu;

public class FloatingActionMenuRightRow extends DataBindingComponent<FloatingActionMenu.Item> {

    public FloatingActionMenuRightRow(ViewGroup parent) {
        super(parent, R.layout.carbon_floatingactionmenu_right);
    }

    @Override
    public void bind(FloatingActionMenu.Item data) {
        super.bind(data);
        FloatingActionButton fab = ((CarbonFloatingactionmenuRightBinding) this.getBinding()).carbonFab;
        if (data.getIconTintList() != null)
            fab.setTintList(data.getIconTintList());
        if (data.getBackgroundDrawable() != null)
            fab.setBackgroundDrawable(data.getBackgroundDrawable());
    }
}
