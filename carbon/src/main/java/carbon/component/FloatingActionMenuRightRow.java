package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.databinding.CarbonFloatingactionmenuRightBinding;
import carbon.widget.FloatingActionMenu;

public class FloatingActionMenuRightRow extends  LayoutComponent<FloatingActionMenu.Item> {

    private final CarbonFloatingactionmenuRightBinding binding;

    public FloatingActionMenuRightRow(ViewGroup parent) {
        super(parent, R.layout.carbon_floatingactionmenu_right);
        binding = CarbonFloatingactionmenuRightBinding.bind(getView());
    }

    @Override
    public void bind(FloatingActionMenu.Item data) {
        super.bind(data);
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
