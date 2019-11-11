package carbon.view

import com.google.android.material.shape.ShapeAppearanceModel

interface ShapeModelView {
    var shapeModel: ShapeAppearanceModel

    fun setCornerCut(cornerCut: Float)

    fun setCornerRadius(cornerRadius: Float)
}
