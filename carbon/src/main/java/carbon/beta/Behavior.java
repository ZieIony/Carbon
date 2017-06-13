package carbon.beta;

import android.graphics.PointF;
import android.view.View;

import carbon.view.TransformationView;
import carbon.widget.OnTransformationChangedListener;

public abstract class Behavior<Type extends View> {
    private Type target;
    private View dependency;
    private OnTransformationChangedListener transformationListener = () -> onDependencyChanged(getDependency());
    private android.view.View.OnLayoutChangeListener layoutListener = (view, i, i1, i2, i3, i4, i5, i6, i7) -> onDependencyChanged(getDependency());

    public Behavior(Type target) {
        this.target = target;
    }

    public Type getTarget() {
        return target;
    }

    public View getDependency() {
        return dependency;
    }

    public void setDependency(View dependency) {
        removeListeners();
        this.dependency = dependency;
        addListeners();
    }

    public PointF onNestedScroll(float scrollX, float scrollY) {
        return new PointF(scrollX, scrollY);
    }

    public void onDependencyChanged(View view) {
    }

    public void onAttachedToWindow() {
        addListeners();
    }

    private void addListeners() {
        if (dependency == null)
            return;

        if (dependency instanceof TransformationView)
            ((TransformationView) dependency).addOnTransformationChangedListener(transformationListener);
        dependency.addOnLayoutChangeListener(layoutListener);
    }

    public void onDetachedFromWindow() {
        removeListeners();
    }

    private void removeListeners() {
        if (dependency == null)
            return;

        if (dependency instanceof TransformationView)
            ((TransformationView) dependency).removeOnTransformationChangedListener(transformationListener);
        dependency.removeOnLayoutChangeListener(layoutListener);
    }
}
