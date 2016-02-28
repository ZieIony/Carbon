package carbon.drawable.ripple;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public abstract class LollipopDrawable extends Drawable {

    private ColorFilter mColorFilter;

    /**
     * Inflate this Drawable from an XML resource optionally styled by a theme.
     *
     * @param r      Resources used to resolve attribute values
     * @param parser XML parser from which to inflate this Drawable
     * @param attrs  Base set of attribute values
     * @param theme  Theme to apply, may be null
     * @throws XmlPullParserException
     * @throws IOException
     */
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme)
            throws XmlPullParserException, IOException {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mColorFilter = cf;
    }

    @Override
    public void setAlpha(int alpha) {

    }

    /**
     * Returns the current color filter, or {@code null} if none set.
     *
     * @return the current color filter, or {@code null} if none set
     */
    public ColorFilter getColorFilter() {
        return mColorFilter;
    }

    /**
     * Gets the current alpha value for the drawable. 0 means fully transparent,
     * 255 means fully opaque. This method is implemented by
     * Drawable subclasses and the value returned is specific to how that class treats alpha.
     * The default return value is 255 if the class does not override this method to return a value
     * specific to its use of alpha.
     */
    public int getAlpha() {
        return 0xFF;
    }

    public boolean canApplyTheme() {
        return false;
    }

    /**
     * Applies the specified theme to this Drawable and its children.
     */
    public void applyTheme(Resources.Theme t) {
    }

    /**
     * Specifies a tint for this drawable.
     * <p>
     * Setting a color filter via {@link #setColorFilter(ColorFilter)} overrides
     * tint.
     *
     * @param tint Color to use for tinting this drawable
     * @see #setTintMode(PorterDuff.Mode)
     */
    public void setTint(int tint) {
        setTintList(ColorStateList.valueOf(tint));
    }

    /**
     * Specifies a tint for this drawable as a color state list.
     * <p>
     * Setting a color filter via {@link #setColorFilter(ColorFilter)} overrides
     * tint.
     *
     * @param tint Color state list to use for tinting this drawable, or null to
     *             clear the tint
     * @see #setTintMode(PorterDuff.Mode)
     */
    public void setTintList(ColorStateList tint) {
    }

    /**
     * Specifies a tint blending mode for this drawable.
     * <p>
     * Setting a color filter via {@link #setColorFilter(ColorFilter)} overrides
     * tint.
     *
     * @param tintMode Color state list to use for tinting this drawable, or null to
     *                 clear the tint
     * @param tintMode A Porter-Duff blending mode
     */
    public void setTintMode(PorterDuff.Mode tintMode) {
    }

    /**
     * Specifies the hotspot's location within the drawable.
     *
     * @param x The X coordinate of the center of the hotspot
     * @param y The Y coordinate of the center of the hotspot
     */
    public void setHotspot(float x, float y) {
    }

    /**
     * Sets the bounds to which the hotspot is constrained, if they should be
     * different from the drawable bounds.
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setHotspotBounds(int left, int top, int right, int bottom) {
    }

    /**
     * @hide For internal use only. Individual results may vary.
     */
    public void getHotspotBounds(Rect outRect) {
        outRect.set(getBounds());
    }
}
