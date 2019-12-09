package carbon.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LayoutComponent<DataType> implements Component<DataType> {
    private final View view;
    private DataType data;

    public LayoutComponent(ViewGroup parent, int resId) {
        view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }

    @Override
    public View getView() {
        return view;
    }

    public void bind(DataType data) {
        this.data = data;
    }

    public DataType getData() {
        return data;
    }
}
