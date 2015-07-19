package carbon.widget;

/**
 * Created by Marcin on 2015-04-23.
 * <p/>
 * Interface of a view with rounded corners. In case of a layout rounded corners also clip the content.
 */
public interface CornerView {
    /**
     * Gets the corner radius
     *
     * @return the corner radius
     */
    int getCornerRadius();

    /**
     * Sets the corner radius. The corner radius cannot be larger than 25px
     * due to limitations of a blurring script. In all Carbon widgets it will be clamped to
     * [0:25px]. There are three cases: for 0dp (no clipping, faster rendering), <2.5dp
     * (fast shadows, pretty fast rendering) and >=2.5dp (the most GPU-intensive option).
     *
     * @param cornerRadius the corner radius
     */
    void setCornerRadius(int cornerRadius);
}
