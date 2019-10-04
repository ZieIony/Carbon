package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonFloatingactionmenuLeftBinding;
import carbon.widget.FloatingActionButton;
import carbon.widget.FloatingActionMenu;

public class FloatingActionMenuLeftRow extends DataBindingComponent<FloatingActionMenu.Item> {

    public FloatingActionMenuLeftRow(ViewGroup parent) {
        super(parent, R.layout.carbon_floatingactionmenu_left);
    }

    @Override
    public void bind(FloatingActionMenu.Item data) {
        super.bind(data);
        FloatingActionButton fab = ((CarbonFloatingactionmenuLeftBinding) this.getBinding()).carbonFab;
        if (data.getIconTintList() != null)
            fab.setTintList(data.getIconTintList());
        if (data.getBackgroundDrawable() != null)
            fab.setBackgroundDrawable(data.getBackgroundDrawable());
    }
}
