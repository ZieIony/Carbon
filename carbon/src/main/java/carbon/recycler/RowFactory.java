package carbon.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;

import carbon.component.Component;

public interface RowFactory<Type> {
    @NonNull
    Component<Type> create(@NonNull ViewGroup parent);
}
