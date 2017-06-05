package carbon.view;

import android.view.View;

import carbon.widget.OnDependencyChangedListener;

public interface DependencyView {
    void addDependency(View view, OnDependencyChangedListener listener);

    void removeDependency(View view);
}
