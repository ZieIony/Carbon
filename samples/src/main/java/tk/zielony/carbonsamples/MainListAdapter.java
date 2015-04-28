package tk.zielony.carbonsamples;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Marcin on 2014-12-15.
 */
class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {
    private final ViewModel[] items;

    public MainListAdapter(ViewModel[] items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(items[position].name);
        holder.beta.setVisibility(items[position].beta ? View.VISIBLE : View.GONE);
        holder.lollipop.setVisibility(items[position].lollipop ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), items[position].klass));
            }
        });
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
        View beta, lollipop;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.text);
            beta = view.findViewById(R.id.beta);
            lollipop = view.findViewById(R.id.lollipop);

        }
    }
}
