package tk.zielony.carbonsamples.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import carbon.widget.ExpandableRecyclerView;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

public class ExpandableFruitAdapter extends ExpandableRecyclerView.Adapter<ExpandableFruitAdapter.ChildViewHolder, ExpandableRecyclerView.SimpleGroupViewHolder, String, String> {
    private List<String> fruits;

    public ExpandableFruitAdapter(List<String> fruits) {
        this.fruits = fruits;
    }


    @Override
    public int getGroupItemCount() {
        return fruits.size();
    }

    @Override
    public int getChildItemCount(int group) {
        return 3;
    }

    @Override
    public String getGroupItem(int position) {
        return fruits.get(position);
    }

    @Override
    public String getChildItem(int group, int position) {
        return "Subitem " + position;
    }

    @Override
    protected ExpandableRecyclerView.SimpleGroupViewHolder onCreateGroupViewHolder(ViewGroup parent) {
        return new ExpandableRecyclerView.SimpleGroupViewHolder(parent.getContext());
    }

    @Override
    protected ExpandableFruitAdapter.ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_drawer, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public int getChildItemViewType(int group, int position) {
        return 1;
    }

    @Override
    public void onBindGroupViewHolder(ExpandableRecyclerView.SimpleGroupViewHolder holder, int group) {
        super.onBindGroupViewHolder(holder, group);
        holder.setText(getGroupItem(group));
    }

    @Override
    public void onBindChildViewHolder(ExpandableFruitAdapter.ChildViewHolder holder, int group, final int position) {
        super.onBindChildViewHolder(holder, group, position);
        holder.tv.setText(getChildItem(group, position));
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;

        public ChildViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.text);
        }
    }

}
