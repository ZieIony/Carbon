package carbon.widget;

import android.graphics.Rect;

/**
 * Interface of a View with support for touch margins. Touch margin is a feature which allows a view
 * to handle events which happened outside of that view. These event have to happen inside the
 * view's parent.
 */
public interface TouchMarginView {
    void setTouchMargin(int left, int top, int right, int bottom);

    void setTouchMarginLeft(int margin);

    void setTouchMarginTop(int margin);

    void setTouchMarginRight(int margin);

    void setTouchMarginBottom(int margin);

    Rect getTouchMargin();
}
