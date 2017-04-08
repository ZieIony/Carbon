package carbon.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import carbon.component.Component;

public class RowViewHolder<Type> extends RecyclerView.ViewHolder {
    private Component<Type> component;

    public RowViewHolder(View itemView) {
        super(itemView);
    }

    public void setComponent(Component<Type> component) {
        this.component = component;
    }

    public Component<Type> getComponent() {
        return component;
    }
}
