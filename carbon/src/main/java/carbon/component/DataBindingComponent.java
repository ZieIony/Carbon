package carbon.component;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import carbon.BR;
import carbon.recycler.Component;

/**
 * Created by Marcin on 2017-02-02.
 */
public class DataBindingComponent<DataType> implements Component<DataType> {
    private final ViewDataBinding binding;
    private final View view;
    private DataType data;

    public DataBindingComponent(ViewGroup parent, int resId) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), resId, parent, false);
        view = binding.getRoot();
    }

    @Override
    public View getView() {
        return view;
    }

    public void bind(DataType data) {
        this.data = data;
        binding.setVariable(BR.data, data);
        binding.executePendingBindings();
    }

    public DataType getData() {
        return data;
    }

    protected ViewDataBinding getBinding() {
        return binding;
    }
}
