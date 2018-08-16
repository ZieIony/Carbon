package carbon.shadow;

import android.content.res.ColorStateList;
import android.graphics.Canvas;

import carbon.view.RenderingModeView;

public interface ShadowView extends RenderingModeView {
    /**
     * Gets the elevation.
     *
     * @return the elevation value.
     */
    float getElevation();

    /**
     * Sets the elevation value. There are useful values of elevation defined in xml as
     * carbon_elevationFlat, carbon_elevationLow, carbon_elevationMedium, carbon_elevationHigh,
     * carbon_elevationMax
     *
     * @param elevation can be from range [0 - 25]
     */
    void setElevation(float elevation);

    float getTranslationZ();

    void setTranslationZ(float translationZ);

    ShadowShape getShadowShape();

    boolean hasShadow();

    void drawShadow(Canvas canvas);

    void setElevationShadowColor(ColorStateList shadowColor);

    void setElevationShadowColor(int color);

    ColorStateList getElevationShadowColor();

    void setOutlineAmbientShadowColor(int color);

    void setOutlineAmbientShadowColor(ColorStateList color);

    int getOutlineAmbientShadowColor();

    void setOutlineSpotShadowColor(int color);

    void setOutlineSpotShadowColor(ColorStateList color);

    int getOutlineSpotShadowColor();

}
