package carbon.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import carbon.CarbonContextWrapper;
import carbon.R;
import carbon.component.MenuItem;
import carbon.component.NavigationRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.internal.Menu;
import carbon.recycler.RowListAdapter;

public class NavigationView extends RecyclerView {
    private OnItemClickedListener onItemClickedListener;
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

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    protected void fireOnItemClickedEvent(View view, Object item, int position) {
        if (onItemClickedListener != null)
            onItemClickedListener.onItemClicked(view, item, position);
    }

    public void setMenu(int resId) {
        CarbonContextWrapper contextWrapper = new CarbonContextWrapper(getContext());
        Menu menu = new Menu(contextWrapper);
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(resId, menu);
        setMenuInternal(menu);
    }

    public void setMenu(android.view.Menu baseMenu) {
        CarbonContextWrapper contextWrapper = new CarbonContextWrapper(getContext());
        Menu menu = new Menu(contextWrapper);
        for (int i = 0; i < baseMenu.size(); i++) {
            android.view.MenuItem menuItem = baseMenu.getItem(i);
            this.menu.add(menuItem.getGroupId(), menuItem.getItemId(), menuItem.getOrder(), menuItem.getTitle()).setIcon(menuItem.getIcon()).setVisible(menuItem.isVisible()).setEnabled(menuItem.isEnabled());
        }
        setMenuInternal(menu);
    }

    private void setMenuInternal(Menu menu) {
        this.menu = menu;
        if (getAdapter() == null) {
            RowListAdapter<Serializable> adapter = new RowListAdapter<>(MenuItem.class, NavigationRow.FACTORY);
            adapter.addFactory(PaddingItem.class, PaddingRow.FACTORY);
            setAdapter(adapter);
        }
        ArrayList<Serializable> items = new ArrayList<>();
        items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        items.addAll(menu.getVisibleItems());
        items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        ((RowListAdapter<Serializable>) getAdapter()).setItems(items);
    }

    public android.view.Menu getMenu() {
        return menu;
    }

}
