package carbon.widget;

import android.graphics.Rect;

/**
 * Created by Marcin on 2015-02-22.
 */
public interface TouchMarginView {
    void setTouchMargin(int left, int top, int right, int bottom);

    void setTouchMarginLeft(int margin);

    void setTouchMarginTop(int margin);

    void setTouchMarginRight(int margin);

    void setTouchMarginBottom(int margin);

    Rect getTouchMargin();
}
