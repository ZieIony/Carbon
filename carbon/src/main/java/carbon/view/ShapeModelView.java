package carbon.view;

import carbon.shadow2.ShapeAppearanceModel;

public interface ShapeModelView extends RoundedCornersView {
    ShapeAppearanceModel getShapeModel();

    void setShapeModel(ShapeAppearanceModel model);

    void setCornerCut(float cornerCut);

    void setCornerRadius(float cornerRadius);
}
