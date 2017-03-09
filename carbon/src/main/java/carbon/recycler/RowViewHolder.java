package carbon.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Marcin on 2016-06-07.
 */
public class RowViewHolder extends RecyclerView.ViewHolder {
    private Component component;

    public RowViewHolder(View itemView) {
        super(itemView);
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
