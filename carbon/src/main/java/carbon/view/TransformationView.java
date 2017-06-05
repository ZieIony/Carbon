package carbon.view;

import carbon.widget.OnTransformationChangedListener;

public interface TransformationView {
    void addOnTransformationChangedListener(OnTransformationChangedListener listener);

    void removeOnTransformationChangedListener(OnTransformationChangedListener listener);

    void clearOnTransformationChangedListeners();

}
