package carbon.view;

import carbon.widget.OnInsetsChangedListener;

/**
 * Interface of a view with insets. Used by layouts to handle standard system insets added by status
 * bar and navigation bar.
 */
public interface InsetView {
    /**
     * Inset-not-set constant
     */
    int INSET_NULL = -1;

    /**
     * Sets insets
     *
     * @param left   has to be greater than or equal to 0 or {@link InsetView.INSET_NULL}
     * @param top    has to be greater than or equal to 0 or {@link InsetView.INSET_NULL}
     * @param right  has to be greater than or equal to 0 or {@link InsetView.INSET_NULL}
     * @param bottom has to be greater than or equal to 0 or {@link InsetView.INSET_NULL}
     */
    void setInset(int left, int top, int right, int bottom);

    int getInsetLeft();

    void setInsetLeft(int insetLeft);

    int getInsetTop();

    void setInsetTop(int insetTop);

    int getInsetRight();

    void setInsetRight(int insetRight);

    int getInsetBottom();

    void setInsetBottom(int insetBottom);

    /**
     * Sets inset color. All insets will be colored
     *
     * @param color new inset color
     */
    void setInsetColor(int color);

    int getInsetColor();

    void setOnInsetsChangedListener(OnInsetsChangedListener onInsetsChangedListener);
}
