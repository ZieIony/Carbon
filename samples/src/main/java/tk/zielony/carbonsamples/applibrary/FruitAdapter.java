package tk.zielony.carbonsamples.applibrary;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import carbon.widget.ItemTouchHelper;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-05-09.
 */
public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder, String> {
    private List<String> fruits;
    private ItemTouchHelper helper;

    public FruitAdapter(List<String> fruits, ItemTouchHelper helper) {
        this.fruits = fruits;
        this.helper = helper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_reorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv.setText(fruits.get(position));

        if (helper != null) {
            holder.reorder.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                        helper.startDrag(holder);
                    return true;
                }
            });
        }
    }

    @Override
    public String getItem(int position) {
        return fruits.get(position);
    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;
        View reorder;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text);
            reorder = itemView.findViewById(R.id.reorder);
        }
    }
}
