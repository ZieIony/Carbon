package carbon.view;

public interface CornersView extends RoundedCornersView{
    Corners getCorners();

    void setCorners(Corners corners);

    void setCornerCut(float cornerCut);

}
