package tk.zielony.carbonsamples.animation;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
class ListRippleAdapter extends BaseAdapter {
    private final String[] items;

    public ListRippleAdapter(String[] items) {
        this.items = items;
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
            view = View.inflate(parent.getContext(), R.layout.row_ripple, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(items[position]);
        return view;
    }
}
