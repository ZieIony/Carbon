package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.component.Component;
import carbon.component.DataBindingComponent;
import carbon.component.DividerItem;
import carbon.component.DividerRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.recycler.RowListAdapter;

public class NavigationView extends RecyclerView {
    public static class Item implements Serializable {
        int id;
        private Drawable icon;
        private ColorStateList tint;
        private int groupId;
        private CharSequence title;

        public Item() {
        }

        public Item(int id, Drawable icon, CharSequence title) {
            this.id = id;
            this.icon = icon;
            this.title = title;
        }

        public Item(MenuItem item) {
            id = item.getItemId();
            icon = item.getIcon();
            tint = MenuItemCompat.getIconTintList(item);
            groupId = item.getGroupId();
            title = item.getTitle();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public Drawable getIcon() {
            return icon;
        }

        public void setIconTintList(ColorStateList tint) {
            this.tint = tint;
        }

        public ColorStateList getIconTintList() {
            return tint;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setTitle(CharSequence title) {
            this.title = title;
        }

        public CharSequence getTitle() {
            return title;
        }
    }

    private OnItemClickedListener onItemClickedListener;
    private Item[] items;
    private View header;
    private int itemLayoutId;

    private static class CustomHeaderItem implements Serializable {
    }

    private static class CustomHeaderRow implements Component<CustomHeaderItem> {

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
        initNavigationView(null, R.attr.carbon_navigationViewStyle);
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigationView(attrs, R.attr.carbon_navigationViewStyle);
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigationView(attrs, defStyleAttr);
    }

    private void initNavigationView(AttributeSet attrs, int defStyleAttr) {
        setLayoutManager(new LinearLayoutManager(getContext()));

        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationView, defStyleAttr, R.style.carbon_NavigationView);

        itemLayoutId = a.getResourceId(R.styleable.NavigationView_carbon_itemLayout, R.layout.carbon_navigation_row);
        int menuId = a.getResourceId(R.styleable.NavigationView_carbon_menu, 0);
        if (menuId != 0)
            setMenu(menuId);

        a.recycle();
    }

    public void setOnItemClickedListener(OnItemClickedListener<android.view.MenuItem> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void setMenu(int resId) {
        setMenu(Carbon.getMenu(getContext(), resId));
    }

    public void setMenu(android.view.Menu menu) {
        ArrayList<Serializable> items = new ArrayList<>();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isVisible())
                items.add(new Item(menu.getItem(i)));
        }

        this.items = Stream.of(items).toArray(Item[]::new);

        initItems();
    }

    private void initItems() {
        if (items == null)
            return;

        RowListAdapter<Serializable> adapter = new RowListAdapter<>(Item.class, parent -> new DataBindingComponent<Item>(NavigationView.this, itemLayoutId) {
        });
        adapter.addFactory(PaddingItem.class, PaddingRow::new);
        adapter.addFactory(DividerItem.class, DividerRow::new);
        adapter.addFactory(CustomHeaderItem.class, parent -> new CustomHeaderRow(header));
        adapter.setOnItemClickedListener(Item.class, (view, menuItem, position) -> {
            if (onItemClickedListener != null)
                onItemClickedListener.onItemClicked(view, menuItem, position);
        });

        List<Serializable> items = new ArrayList<>(Arrays.asList(this.items));
        for (int i = 0; i < items.size() - 1; i++) {
            if (((Item) items.get(i)).getGroupId() != ((Item) items.get(i + 1)).getGroupId())
                items.add(++i, new DividerItem());
        }
        items.add(0, new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        if (header != null) {
            items.add(0, new CustomHeaderItem());
        }

        adapter.setItems(items);
        setAdapter(adapter);
    }

    public void setMenuItems(Item[] items) {
        this.items = items;

        initItems();
    }

    public Item[] getMenuItems() {
        return items;
    }

    public void setHeader(View header) {
        this.header = header;
        initItems();
    }

    public void setItemLayout(int resId) {
        itemLayoutId = resId;
        initItems();
    }
}
