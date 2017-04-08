package carbon.component;

import android.view.View;

public interface Component<Type> {
    View getView();

    void bind(Type data);
}
