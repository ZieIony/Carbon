package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonFloatingactionmenuLeftBinding;
import carbon.widget.FloatingActionMenu;

public class FloatingActionMenuLeftRow extends LayoutComponent<FloatingActionMenu.Item> {

    private final CarbonFloatingactionmenuLeftBinding binding;

    public FloatingActionMenuLeftRow(ViewGroup parent) {
        super(parent, R.layout.carbon_floatingactionmenu_left);
        binding = CarbonFloatingactionmenuLeftBinding.bind(getView());
    }

    @Override
    public void bind(FloatingActionMenu.Item data) {
        binding.carbonFab.setImageDrawable(data.getIcon());
        binding.carbonFab.setEnabled(data.isEnabled());
        binding.carbonTooltip.setText(data.getTitle());
        binding.carbonTooltip.setEnabled(data.isEnabled());
        if (data.getIconTintList() != null)
            binding.carbonFab.setTintList(data.getIconTintList());
        if (data.getBackgroundDrawable() != null)
            binding.carbonFab.setBackgroundDrawable(data.getBackgroundDrawable());
    }
}
