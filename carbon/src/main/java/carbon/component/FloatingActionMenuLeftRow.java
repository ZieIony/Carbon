package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonFloatingactionmenuLeftBinding;
import carbon.widget.FloatingActionButton;

public class FloatingActionMenuLeftRow extends DataBindingComponent<MenuItem> {

    public FloatingActionMenuLeftRow(ViewGroup parent) {
        super(parent, R.layout.carbon_floatingactionmenu_left);
    }

    @Override
    public void bind(MenuItem data) {
        super.bind(data);
        FloatingActionButton fab = ((CarbonFloatingactionmenuLeftBinding) this.getBinding()).carbonFab;
        fab.setImageDrawable(data.getIcon(getView().getContext()));
        if (data.getIconTint() != null)
            fab.setTint(data.getIconTint());
        if (data.getBackgroundDrawable() != null)
            fab.setBackgroundDrawable(data.getBackgroundDrawable());
    }
}
