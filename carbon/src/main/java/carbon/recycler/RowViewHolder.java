package carbon.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Marcin on 2016-06-07.
 */
public class RowViewHolder extends RecyclerView.ViewHolder {
    private Row row;

    public RowViewHolder(View itemView) {
        super(itemView);
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public Row getRow() {
        return row;
    }
}
