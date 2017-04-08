package carbon.widget;

import android.annotation.SuppressLint;
import android.view.View;

public interface VisibleView {
    @SuppressLint("NewApi")
    default boolean isVisible() {
        return ((View) this).getVisibility() == View.VISIBLE;
    }
}
