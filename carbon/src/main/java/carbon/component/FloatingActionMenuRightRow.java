package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonFloatingactionmenuRightBinding;
import carbon.recycler.RowFactory;
import carbon.widget.FloatingActionButton;

public class FloatingActionMenuRightRow extends DataBindingComponent<MenuItem> {
    public static final RowFactory FACTORY = FloatingActionMenuRightRow::new;

    public FloatingActionMenuRightRow(ViewGroup parent) {
        super(parent, R.layout.carbon_floatingactionmenu_right);
    }

    @Override
    public void bind(MenuItem data) {
        super.bind(data);
        FloatingActionButton fab = ((CarbonFloatingactionmenuRightBinding) this.getBinding()).carbonFab;
        fab.setImageDrawable(data.getIcon(getView().getContext()));
        if (data.getIconTint() != null)
            fab.setTint(data.getIconTint());
        if (data.getBackgroundDrawable() != null)
            fab.setBackgroundDrawable(data.getBackgroundDrawable());
    }
}
