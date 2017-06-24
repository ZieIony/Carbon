package carbon.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import carbon.Carbon;
import carbon.R;
import carbon.component.Component;
import carbon.component.DividerItem;
import carbon.component.DividerRow;
import carbon.component.MenuItem;
import carbon.component.NavigationRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.internal.Menu;
import carbon.recycler.RowListAdapter;

public class NavigationView extends RecyclerView {
    private OnItemClickedListener onItemClickedListener;
    private Menu menu;
    private View header;

    private class CustomHeaderItem implements Serializable {
    }

    private class CustomHeaderRow implements Component<CustomHeaderItem> {

        private View view;

        CustomHeaderRow(View view) {
            this.view = view;
        }

        @Override
        public View getView() {
            return view;
        }

        @Override
        public void bind(CustomHeaderItem data) {
        }
    }

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
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    protected void fireOnItemClickedEvent(View view, Object item, int position) {
        if (onItemClickedListener != null)
            onItemClickedListener.onItemClicked(view, item, position);
    }

    public void setMenu(int resId) {
        setMenuInternal(Carbon.getMenu(getContext(), resId));
    }

    public void setMenu(android.view.Menu baseMenu) {
        setMenuInternal(Carbon.getMenu(getContext(), baseMenu));
    }

    private void setMenuInternal(Menu menu) {
        this.menu = menu;

        RowListAdapter<Serializable> adapter = new RowListAdapter<>(MenuItem.class, NavigationRow::new);
        adapter.addFactory(PaddingItem.class, PaddingRow::new);
        adapter.addFactory(DividerItem.class, DividerRow::new);
        setAdapter(adapter);

        ArrayList<Serializable> items = new ArrayList<>();
        items.addAll(menu.getVisibleItems());
        for (int i = 0; i < items.size() - 1; i++) {
            if (((android.view.MenuItem) items.get(i)).getGroupId() != ((android.view.MenuItem) items.get(i + 1)).getGroupId())
                items.add(++i, new DividerItem());
        }
        items.add(0, new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        if (header != null) {
            items.add(new CustomHeaderItem());
            adapter.addFactory(CustomHeaderItem.class, parent -> new CustomHeaderRow(header));
        }

        ((RowListAdapter<Serializable>) getAdapter()).setItems(items);
    }

    public android.view.Menu getMenu() {
        return menu;
    }

    public void addHeader(View header) {
        this.header = header;
    }
}
