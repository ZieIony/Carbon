package carbon.drawable.ripple;

public interface RippleView {
    /**
     * Gets the ripple drawable.
     *
     * @return the ripple drawable. Can be null.
     */
    RippleDrawable getRippleDrawable();

    /**
     * Sets the ripple drawable. This method doesn't break the background.
     *
     * @param rippleDrawable the ripple drawable. Can be null
     */
    void setRippleDrawable(RippleDrawable rippleDrawable);
}
