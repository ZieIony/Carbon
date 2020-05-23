package tk.zielony.carbonsamples;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import carbon.component.Component;

public class DataBindingComponent<DataType> extends Component<DataType> {
    private final ViewDataBinding binding;

    public DataBindingComponent(ViewGroup parent, int resId) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), resId, parent, false);
        view = binding.getRoot();
    }

    protected void bind(DataType data) {
        binding.setVariable(BR.data, data);
        binding.executePendingBindings();
    }

    protected <BindingType extends ViewDataBinding> BindingType getBinding() {
        return (BindingType)binding;
    }
}
