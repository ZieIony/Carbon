package carbon.widget;

import android.view.View;

class Dependency {
    View view;
    OnTransformationChangedListener transformationListener;
    View.OnLayoutChangeListener layoutListener;

    public Dependency(View view, OnTransformationChangedListener transformationListener, View.OnLayoutChangeListener layoutListener) {
        this.view = view;
        this.transformationListener = transformationListener;
        this.layoutListener = layoutListener;
    }
}
