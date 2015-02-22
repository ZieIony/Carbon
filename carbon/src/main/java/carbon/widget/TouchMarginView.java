package carbon.widget;

import android.graphics.Rect;

/**
 * Created by Marcin on 2015-02-22.
 */
public interface TouchMarginView {
    void setTouchMargin(Rect rect);

    void setTouchMargin(int left, int top, int right, int bottom);

    Rect getTouchMargin();
}
