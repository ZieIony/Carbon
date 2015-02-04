package pl.zielony.samples;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Marcin on 2014-12-15.
 */
class MainListAdapter extends BaseAdapter {
    private final String[] items;
    private boolean[] beta;

    public MainListAdapter(String[] items, boolean[] beta) {
        this.items = items;
        this.beta = beta;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = View.inflate(parent.getContext(), R.layout.row_main, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(items[position]);

        view.findViewById(R.id.beta).setVisibility(beta[position] ? View.VISIBLE : View.INVISIBLE);
        return view;
    }
}
