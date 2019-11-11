package carbon.view

import android.content.res.ColorStateList
import android.graphics.Canvas

interface ShadowView {
    /**
     * The elevation value. There are useful values of elevation defined in xml as
     * carbon_elevationFlat, carbon_elevationLow, carbon_elevationMedium, carbon_elevationHigh,
     * carbon_elevationMax.
     */
    var elevation: Float

    var translationZ: Float

    var elevationShadowColor: ColorStateList

    fun setElevationShadowColor(color: Int)

    var outlineAmbientShadowColor: Int

    fun setOutlineAmbientShadowColor(color: ColorStateList)

    var outlineSpotShadowColor: Int

    fun setOutlineSpotShadowColor(color: ColorStateList)

    fun hasShadow(): Boolean

    fun drawShadow(canvas: Canvas)

}
