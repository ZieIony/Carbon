package carbon.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
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
     * Sets the tint of foreground parts like checkbox or icon
     *
     * @param list a tint color state list
     */
    void setTint(ColorStateList list);

    /**
     * Sets the tint of foreground parts like checkbox or icon
     *
     * @param color a tint color
     */
    void setTint(int color);

    /**
     * Gets the tint of foreground parts like checkbox or icon
     *
     * @return the tint
     */
    ColorStateList getTint();

    /**
     * Sets the tint mode of foreground parts like checkbox or icon
     *
     * @param mode
     */
    void setTintMode(@NonNull PorterDuff.Mode mode);

    /**
     * Gets the tint mode of foreground parts like checkbox or icon
     *
     * @return
     */
    PorterDuff.Mode getTintMode();

    /**
     * Sets the tint of background Drawable
     *
     * @param list a tint color state list
     */
    void setBackgroundTint(ColorStateList list);

    /**
     * Sets the tint of background Drawable
     *
     * @param color a tint color
     */
    void setBackgroundTint(int color);

    /**
     * Gets the tint of background Drawable
     *
     * @return the tint
     */
    ColorStateList getBackgroundTint();

    /**
     * Sets the tint mode of background Drawable
     *
     * @param mode
     */
    void setBackgroundTintMode(@Nullable PorterDuff.Mode mode);

    /**
     * Gets the tint mode of background Drawable
     *
     * @return
     */
    PorterDuff.Mode getBackgroundTintMode();

    boolean isAnimateColorChangesEnabled();

    void setAnimateColorChangesEnabled(boolean animateColorChanges);
}
