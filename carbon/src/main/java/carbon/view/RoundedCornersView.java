package carbon.view;

/**
 * Interface of a view with rounded corners. In case of a layout rounded corners also clip the
 * content.
 */
@Deprecated
public interface RoundedCornersView extends RenderingModeView {
    /**
     * Gets the corner radius
     *
     * @return the corner radius
     */
    float getCornerRadius();

    /**
     * Sets the corner radius. The corner radius cannot be larger than 25px
     * due to limitations of a blurring script. In all Carbon widgets it will be clamped to
     * [0:25px].
     *
     * @param cornerRadius the corner radius
     */
    void setCornerRadius(float cornerRadius);

    /*void setCornerRadius(float topStart, float topEnd, float bottomStart, float bottomEnd);
*/

}
