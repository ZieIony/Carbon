package carbon.component;

import android.annotation.SuppressLint;
import android.view.View;

public interface Component<Type> {
    View getView();

    @SuppressLint("NewApi")
    default void bind(Type data) {
    }
}
