package carbon.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;

/**
 * Created by Marcin on 2015-05-08.
 * <p/>
 * Interface of a view with support for tinting.
 */
public interface TintedView {
    PorterDuff.Mode[] modes = {
            PorterDuff.Mode.SRC_OVER,
            PorterDuff.Mode.SRC_IN,
            PorterDuff.Mode.SRC_ATOP,
            PorterDuff.Mode.MULTIPLY,
            PorterDuff.Mode.SCREEN
    };

    /**
     * Sets the tint
     *
     * @param list a tint color state list
     */
    void setTint(ColorStateList list);

    /**
     * Sets the tint
     *
     * @param color a tint color
     */
    void setTint(int color);

    /**
     * Gets the tint
     *
     * @return the tint
     */
    ColorStateList getTint();

    /**
     * @param mode
     */
    void setTintMode(@NonNull PorterDuff.Mode mode);

    /**
     * @return
     */
    PorterDuff.Mode getTintMode();
}
