package carbon.view;

import carbon.shadow.ShapeAppearanceModel;

public interface ShapeModelView {
    ShapeAppearanceModel getShapeModel();

    void setShapeModel(ShapeAppearanceModel model);

    void setCornerCut(float cornerCut);

    void setCornerRadius(float cornerRadius);
}
