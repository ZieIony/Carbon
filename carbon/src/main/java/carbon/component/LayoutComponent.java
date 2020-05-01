package carbon.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class LayoutComponent<DataType> implements Component<DataType> {
    private final View view;

    public LayoutComponent(@NonNull ViewGroup parent, int resId) {
        view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }

    @Override
    @NonNull
    public View getView() {
        return view;
    }

    @Deprecated
    public DataType getData() {
        return null;
    }
}
