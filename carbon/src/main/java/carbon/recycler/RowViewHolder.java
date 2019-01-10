package carbon.recycler;

import androidx.recyclerview.widget.RecyclerView;
import carbon.component.Component;

public class RowViewHolder<Type> extends RecyclerView.ViewHolder {
    private Component<Type> component;

    public RowViewHolder(Component<Type> component) {
        super(component.getView());
        this.component = component;
    }

    public Component<Type> getComponent() {
        return component;
    }
}
