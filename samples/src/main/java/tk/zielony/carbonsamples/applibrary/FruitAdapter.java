package tk.zielony.carbonsamples.applibrary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import carbon.widget.RecyclerView;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-05-09.
 */
public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder,String> {
    private String[] fruits;

    public FruitAdapter(String[] fruits) {
        this.fruits = fruits;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_drawer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        holder.tv.setText(fruits[position]);
    }

    @Override
    public String getItem(int position) {
        return fruits[position];
    }

    @Override
    public int getItemCount() {
        return fruits.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
