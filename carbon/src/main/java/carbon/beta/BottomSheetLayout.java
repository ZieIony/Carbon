package carbon.beta;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;

import carbon.Carbon;
import carbon.R;
import carbon.component.BottomSheetCell;
import carbon.component.BottomSheetRow;
import carbon.recycler.RowListAdapter;
import carbon.recycler.ViewItemDecoration;
import carbon.widget.LinearLayout;
import carbon.widget.NavigationView;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;

public class BottomSheetLayout extends LinearLayout {

    public static class Item implements Serializable {
        private ColorStateList iconTint;
        private int groupId;
        private Drawable icon;
        private CharSequence title;

        public Item() {
        }

        public Item(MenuItem menuItem) {
            groupId = menuItem.getGroupId();
            icon = menuItem.getIcon();
            iconTint = MenuItemCompat.getIconTintList(menuItem);
            title = menuItem.getTitle();
        }

        public int getGroupId() {
            return groupId;
        }

        public Drawable getIcon() {
            return icon;
        }

        public ColorStateList getIconTintList() {
            return iconTint;
        }

        public CharSequence getTitle() {
            return title;
        }
    }

    public enum Style {
        List, Grid
    }

    private Menu menu;
    private android.view.MenuItem.OnMenuItemClickListener listener;
    private TextView titleTv;
    private RecyclerView recycler;
    private Style style = Style.List;

    public BottomSheetLayout(Context context) {
        super(context);
        initBottomSheet();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBottomSheet();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBottomSheet();
    }

    public BottomSheetLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBottomSheet();
    }

    private void initBottomSheet() {
        View.inflate(getContext(), R.layout.carbon_bottomsheet, this);
        setOrientation(VERTICAL);
        titleTv = findViewById(R.id.carbon_bottomSheetTitle);
        recycler = findViewById(R.id.carbon_bottomSheetRecycler);
    }

    public void setOnMenuItemClickListener(android.view.MenuItem.OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    public void setMenu(int resId) {
        menu = Carbon.getMenu(getContext(), resId);
        updateRecycler();
    }

    public void setMenu(final Menu baseMenu) {
        menu = baseMenu;
        updateRecycler();
    }

    public android.view.Menu getMenu() {
        return menu;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
        updateRecycler();
    }

    public void setTitle(String title) {
        titleTv.setText(title);
        titleTv.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
        updateRecycler();
    }

    private void updateRecycler() {
        if (menu == null)
            return;

        recycler.setLayoutManager(style == Style.List ? new LinearLayoutManager(getContext()) : new GridLayoutManager(getContext(), 3));

        ArrayList<Serializable> items = new ArrayList<>();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isVisible())
                items.add(new Item(menu.getItem(i)));
        }
        if (style == Style.List) {
            for (int i = 0; i < recycler.getItemDecorationCount(); i++)
                recycler.removeItemDecorationAt(0);

            ViewItemDecoration dividerItemDecoration = new ViewItemDecoration(getContext(), R.layout.carbon_menustrip_hseparator_item);
            dividerItemDecoration.setDrawAfter(position -> position < items.size() - 1 &&
                    items.get(position) instanceof NavigationView.Item &&
                    items.get(position + 1) instanceof NavigationView.Item &&
                    ((NavigationView.Item) items.get(position)).getGroupId() != ((NavigationView.Item) items.get(position + 1)).getGroupId());
            recycler.addItemDecoration(dividerItemDecoration);

            ViewItemDecoration paddingItemDecoration = new ViewItemDecoration(getContext(), R.layout.carbon_row_padding);
            paddingItemDecoration.setDrawAfter(position -> position == items.size() - 1);
            recycler.addItemDecoration(paddingItemDecoration);
        }

        RowListAdapter<Serializable> adapter = new RowListAdapter<>();
        adapter.putFactory(Item.class, style == Style.List ? BottomSheetRow::new : BottomSheetCell::new);
        adapter.setItems(items);

        recycler.setAdapter(adapter);
    }
}
