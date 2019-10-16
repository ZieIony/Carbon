package carbon.view;

import android.view.View;

public interface VisibleView {
    default boolean isVisible() {
        return ((View) this).getVisibility() == View.VISIBLE;
    }
}
