package tk.zielony.carbonsamples.library;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import carbon.recycler.ItemTouchHelper;
import carbon.recycler.ListAdapter;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

public class FruitAdapter extends ListAdapter<FruitAdapter.ViewHolder, String> {
    private ItemTouchHelper helper;

    public FruitAdapter(List<String> fruits) {
        super(fruits);
    }

    public FruitAdapter(List<String> fruits, ItemTouchHelper helper) {
        super(fruits);
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
        holder.tv.setText(getItem(position));

        if (helper != null) {
            holder.reorder.setOnTouchListener((view, motionEvent) -> {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    helper.startDrag(holder);
                return true;
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;
        View reorder;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text);
            reorder = itemView.findViewById(R.id.reorder);
        }
    }
}
