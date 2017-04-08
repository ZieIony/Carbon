package carbon.recycler;

import android.view.ViewGroup;

import carbon.component.Component;

public interface RowFactory<Type> {
    Component<Type> create(ViewGroup parent);
}
