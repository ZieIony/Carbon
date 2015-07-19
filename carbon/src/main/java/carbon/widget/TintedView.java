package carbon.widget;

import android.content.res.ColorStateList;

/**
 * Created by Marcin on 2015-05-08.
 *
 * Interface of a view with support for tinting.
 */
public interface TintedView {
    /**
     * Sets the tint
     * @param list a tint color state list
     */
    void setTint(ColorStateList list);

    /**
     * Sets the tint
     * @param color a tint color
     */
    void setTint(int color);

    /**
     * Gets the tint
     * @return the tint
     */
    ColorStateList getTint();
}
