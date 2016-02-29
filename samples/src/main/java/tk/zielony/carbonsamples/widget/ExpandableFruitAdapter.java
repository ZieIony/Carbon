package tk.zielony.carbonsamples.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import carbon.widget.ExpandableRecyclerView;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-05-09.
 */
public class ExpandableFruitAdapter extends ExpandableRecyclerView.Adapter<RecyclerView.ViewHolder, String> {
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
    protected RecyclerView.ViewHolder onCreateGroupViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_expandablerecyclerview_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_drawer, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public int getChildItemViewType(int group, int position) {
        return 1;
    }

    @Override
    public void onBindGroupViewHolder(RecyclerView.ViewHolder holder, int group) {
        super.onBindGroupViewHolder(holder, group);
        GroupViewHolder h = (GroupViewHolder) holder;
        h.tv.setText(getGroupItem(group));
    }

    @Override
    public void onBindChildViewHolder(RecyclerView.ViewHolder holder, int group, final int position) {
        super.onBindChildViewHolder(holder, group, position);
        ChildViewHolder h = (ChildViewHolder) holder;
        h.tv.setText(getChildItem(group, position));
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;

        public ChildViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text);
        }
    }

    public class GroupViewHolder extends ExpandableRecyclerView.GroupViewHolder {
        private final TextView tv;

        public GroupViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
