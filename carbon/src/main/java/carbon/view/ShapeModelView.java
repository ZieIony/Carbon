package carbon.view;

import com.google.android.material.shape.ShapeAppearanceModel;

public interface ShapeModelView extends RoundedCornersView {
    ShapeAppearanceModel getShapeModel();

    void setShapeModel(ShapeAppearanceModel model);

    void setCornerCut(float cornerCut);

    void setCornerRadius(float cornerRadius);
}
