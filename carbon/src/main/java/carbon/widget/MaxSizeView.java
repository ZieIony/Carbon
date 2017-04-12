package carbon.widget;

/**
 * Interface of a view with support for maximum size.
 */
public interface MaxSizeView {
    /**
     * Sets maximum width. The new maximum size will be respected after next layout pass.
     *
     * @param maxWidth maximal width
     */
    void setMaximumWidth(int maxWidth);

    /**
     * Gets maximum width
     *
     * @return maximum width
     */
    int getMaximumWidth();

    /**
     * Sets maximum height. The new maximum size will be respected after next layout pass.
     *
     * @param maxHeight maximum height
     */
    void setMaximumHeight(int maxHeight);

    /**
     * Gets maximum height
     *
     * @return maximum height
     */
    int getMaximumHeight();
}
