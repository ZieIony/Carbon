package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;

/**
 * Created by Marcin on 2015-06-11.
 */
public class Spinner extends EditText {
    PopupMenu popupMenu;

    public Spinner(Context context) {
        this(context, null);
    }

    public Spinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_spinnerStyle);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        popupMenu = new PopupMenu(context);
        defaultAdapter = new Adapter();
        popupMenu.setAdapter(defaultAdapter);
        popupMenu.setTint(getTint());

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.setWidth((int) (getWidth() + getResources().getDimension(R.dimen.carbon_padding) * 2));
                popupMenu.show(Spinner.this);
            }
        });
    }

    public void setAdapter(final RecyclerView.Adapter adapter) {
        if (adapter == null) {
            popupMenu.setAdapter(defaultAdapter);
            defaultAdapter.setOnItemClickedListener(onItemClickedListener);
        } else {
            popupMenu.setAdapter(adapter);
            adapter.setOnItemClickedListener(onItemClickedListener);
        }
    }

    public RecyclerView.Adapter getAdapter() {
        return popupMenu.getAdapter();
    }

    RecyclerView.OnItemClickedListener onItemClickedListener = new RecyclerView.OnItemClickedListener() {
        @Override
        public void onItemClicked(int position) {
            setText(popupMenu.getAdapter().getItem(position).toString());
            popupMenu.dismiss();
        }
    };

    Adapter defaultAdapter;

    public void setItems(String[] items) {
        popupMenu.setAdapter(defaultAdapter);
        defaultAdapter.setOnItemClickedListener(onItemClickedListener);
        defaultAdapter.setItems(items);
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder, String> {

        private String[] items = new String[0];

        @Override
        public String getItem(int position) {
            return items[position];
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.carbon_popup_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            super.onBindViewHolder(holder, position);
            holder.tv.setText(items[position]);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }


        public void setItems(String[] items) {
            this.items = items;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.carbon_itemText);
        }
    }


    // -------------------------------
    // tint
    // -------------------------------

    @Override
    public void setTint(ColorStateList list) {
        super.setTint(list);
        if (popupMenu != null)
            popupMenu.setTint(list);
    }
}
