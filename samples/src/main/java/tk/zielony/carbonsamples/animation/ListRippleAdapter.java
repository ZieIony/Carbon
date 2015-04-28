package tk.zielony.carbonsamples.animation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
class ListRippleAdapter extends RecyclerView.Adapter<ListRippleAdapter.ViewHolder> {
    private final String[] items;

    public ListRippleAdapter(String[] items) {
        this.items = items;
    }

    @Override
    public ListRippleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.row_ripple, null));
    }

    @Override
    public void onBindViewHolder(ListRippleAdapter.ViewHolder holder, int position) {
        holder.textView.setText(items[position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
