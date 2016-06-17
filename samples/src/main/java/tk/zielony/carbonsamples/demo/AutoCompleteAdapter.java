package tk.zielony.carbonsamples.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.widget.AutoCompleteTextView;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-29.
 */
public class AutoCompleteAdapter extends RecyclerView.Adapter<AutoCompleteAdapter.ViewHolder> implements Filterable {
    String[] originalStrings;
    List<String> strings;
    private OnHintClicked onHintClicked;
    private AutoCompleteTextView.OnAutoCompleteListener onAutoCompleteListener;

    public AutoCompleteAdapter(String[] strings) {
        this.originalStrings = strings;
        this.strings = Arrays.asList(originalStrings);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_autocomplete, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.text.setText(strings.get(i));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHintClicked.onHintClicked(strings.get(i));
            }
        });
    }

    public String getItem(int position) {
        return strings.get(position);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> values = new ArrayList<>();
                for (String s : originalStrings)
                    if (s.toLowerCase().startsWith(constraint.toString().toLowerCase()))
                        values.add(s);
                results.values = values;
                results.count = values.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                strings = (List<String>) results.values;
                notifyDataSetChanged();
                onAutoCompleteListener.onAutoComplete();
            }
        };
    }

    public void setOnHintClicked(OnHintClicked onHintClicked) {
        this.onHintClicked = onHintClicked;
    }

    public void setOnAutoCompleteListener(AutoCompleteTextView.OnAutoCompleteListener onAutoCompleteListener) {
        this.onAutoCompleteListener = onAutoCompleteListener;
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
