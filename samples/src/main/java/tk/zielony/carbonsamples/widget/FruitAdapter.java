package tk.zielony.carbonsamples.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import carbon.recycler.ListAdapter;
import carbon.view.SelectionMode;
import carbon.widget.CheckBox;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

public class FruitAdapter extends ListAdapter<FruitAdapter.ViewHolder, String> {
    private View.OnTouchListener onTouchListener;

    public FruitAdapter(List<String> fruits) {
        super(fruits);
        setSelectionMode(SelectionMode.SINGLE);
    }

    public FruitAdapter(List<String> fruits, View.OnTouchListener onTouchListener) {
        super(fruits);
        setSelectionMode(SelectionMode.SINGLE);
        this.onTouchListener = onTouchListener;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_reorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.tv.setText(getItem(position));
        holder.checkBox.setChecked(getSelectedIndices().contains(position));
        holder.reorder.setOnTouchListener(onTouchListener);
        holder.itemView.setSelected(getSelectedIndices().contains(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;
        View reorder;
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text);
            reorder = itemView.findViewById(R.id.reorder);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
