package carbon.widget;

import android.content.Context;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;

/**
 * Created by Marcin on 2015-06-25.
 */
public class NavigationView extends RecyclerView {
    private Menu menu;

    public NavigationView(Context context) {
        super(context);
        initNavigationView();
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigationView();
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigationView();
    }

    private void initNavigationView() {
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void setMenu(int resId) {
        Menu menu = new MenuBuilder(getContext());
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(resId, menu);
        setMenu(menu);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        if (getAdapter() == null) {
            setAdapter(new Adapter());
        }
        ((Adapter) getAdapter()).setItems(menu);
    }

    public Menu getMenu() {
        return menu;
    }

    public static class Adapter extends RecyclerView.Adapter<ViewHolder, MenuItem> {

        private Menu items = null;

        @Override
        public MenuItem getItem(int position) {
            return items.getItem(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.carbon_navigation_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            super.onBindViewHolder(holder, position);
            holder.tv.setText(items.getItem(position).getTitle());
            holder.iv.setImageDrawable(items.getItem(position).getIcon());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setItems(Menu items) {
            this.items = items;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.carbon_itemText);
            iv = (ImageView) itemView.findViewById(R.id.carbon_itemIcon);
        }
    }
}
