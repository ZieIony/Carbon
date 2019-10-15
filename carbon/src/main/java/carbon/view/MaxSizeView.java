package carbon.view;

/**
 * Interface of a view with support for maximum size.
 */
public interface MaxSizeView {
    /**
     * Sets maximum width. The new maximum size will be respected after next layout pass.
     *
     * @param maxWidth maximal width
     */
    void setMaxWidth(int maxWidth);

    /**
     * Gets maximum width
     *
     * @return maximum width
     */
    int getMaxWidth();

    /**
     * Sets maximum height. The new maximum size will be respected after next layout pass.
     *
     * @param maxHeight maximum height
     */
    void setMaxHeight(int maxHeight);

    /**
     * Gets maximum height
     *
     * @return maximum height
     */
    int getMaxHeight();

    @Deprecated
    default void setMaximumWidth(int w){
        setMaxWidth(w);
    }

    @Deprecated
    default int getMaximumWidth(){
        return getMaxWidth();
    }

    @Deprecated
    default void setMaximumHeight(int w){
        setMaxHeight(w);
    }

    @Deprecated
    default int getMaximumHeight(){
        return getMaxHeight();
    }
}
