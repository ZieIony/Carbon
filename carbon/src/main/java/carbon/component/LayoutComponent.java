package carbon.component;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public abstract class LayoutComponent<DataType> extends Component<DataType> {

    public LayoutComponent(@NonNull ViewGroup parent, int resId) {
        view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
    }
}
