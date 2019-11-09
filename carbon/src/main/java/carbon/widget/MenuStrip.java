package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import carbon.Carbon;
import carbon.R;
import carbon.component.Component;
import carbon.component.DataBindingComponent;
import carbon.component.DividerItem;
import carbon.recycler.RowListAdapter;

public class MenuStrip extends RecyclerView {
    public static class Item implements Serializable {
        private int id;
        private Drawable icon;
        private CharSequence text;
        private ColorStateList iconTint;
        private int groupId;

        public Item() {
        }

        public Item(int id, Drawable icon, CharSequence text) {
            this.id = id;
            this.icon = icon;
            this.text = text;
        }

        public Item(MenuItem menuItem) {
            id = menuItem.getItemId();
            try {   // breaks preview
                this.icon = menuItem.getIcon();
            } catch (Exception e) {
            }
            this.text = menuItem.getTitle();
            iconTint = MenuItemCompat.getIconTintList(menuItem);
            groupId = menuItem.getGroupId();
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

        public void setTitle(CharSequence text) {
            this.text = text;
        }

        public CharSequence getTitle() {
            return text;
        }

        public void setIconTintList(ColorStateList iconTint) {
            this.iconTint = iconTint;
        }

        public ColorStateList getIconTintList() {
            return iconTint;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }
    }

    private static class SeparatorComponent implements Component<DividerItem> {
        View view;

        SeparatorComponent(ViewGroup parent, int layoutId) {
            view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }

        @Override
        public View getView() {
            return view;
        }
    }

    private Item[] items;
    private int itemLayoutId, separatorLayoutId;
    carbon.view.Orientation orientation;
    RowListAdapter<Serializable> adapter;

    RecyclerView.OnItemClickedListener<Item> onItemClickedListener;

    public MenuStrip(Context context) {
        super(context, null, R.attr.carbon_menuStripStyle);
        initMenuStrip(null, R.attr.carbon_menuStripStyle);
    }

    public MenuStrip(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_menuStripStyle);
        initMenuStrip(attrs, R.attr.carbon_menuStripStyle);
    }

    public MenuStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initMenuStrip(attrs, defStyleAttr);
    }

    private void initMenuStrip(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MenuStrip, defStyleAttr, R.style.carbon_MenuStrip);

        setOrientation(carbon.view.Orientation.values()[a.getInt(R.styleable.MenuStrip_android_orientation, carbon.view.Orientation.VERTICAL.ordinal())]);
        itemLayoutId = a.getResourceId(R.styleable.MenuStrip_carbon_itemLayout, R.layout.carbon_menustrip_item);
        separatorLayoutId = a.getResourceId(R.styleable.MenuStrip_carbon_separatorLayout, 0);
        int menuId = a.getResourceId(R.styleable.MenuStrip_carbon_menu, 0);
        if (menuId != 0)
            setMenu(menuId);

        a.recycle();
    }

    public void setOrientation(carbon.view.Orientation orientation) {
        this.orientation = orientation;
        initItems();
    }

    public carbon.view.Orientation getOrientation() {
        return orientation;
    }

    public void setMenuItems(Item[] items) {
        this.items = items;
        initItems();
    }

    public Item[] getMenuItems() {
        return items;
    }

    public void setMenu(int resId) {
        setMenu(Carbon.getMenu(getContext(), resId));
    }

    public void setMenu(Menu menu) {
        items = new Item[menu.size()];
        for (int i = 0; i < menu.size(); i++)
            items[i] = new Item(menu.getItem(i));
        initItems();
    }

    public void setItemLayout(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
        initItems();
    }

    public void setSeparatorLayout(int separatorLayoutId) {
        this.separatorLayoutId = separatorLayoutId;
        initItems();
    }

    public void setOnItemClickedListener(OnItemClickedListener<Item> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
        initItems();
    }

    private void initItems() {
        if (items == null)
            return;

        List<Serializable> items = new ArrayList<>(Arrays.asList(this.items));
        for (int i = 0; i < items.size() - 1; i++) {
            if (((Item) items.get(i)).getGroupId() != ((Item) items.get(i + 1)).getGroupId())
                items.add(++i, new DividerItem());
        }

        setLayoutManager(new LinearLayoutManager(getContext(), orientation == carbon.view.Orientation.VERTICAL ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL, false));
        adapter = new RowListAdapter<>(Item.class, parent -> new DataBindingComponent<>(parent, itemLayoutId));
        int separatorLayoutId = this.separatorLayoutId;
        if (separatorLayoutId == 0)
            separatorLayoutId = orientation == carbon.view.Orientation.VERTICAL ? R.layout.carbon_menustrip_hseparator_item : R.layout.carbon_menustrip_vseparator_item;
        int finalSeparatorLayoutId = separatorLayoutId;
        adapter.addFactory(DividerItem.class, parent -> new SeparatorComponent(parent, finalSeparatorLayoutId));

        adapter.setOnItemClickedListener(Item.class, (view, item, position) -> {
            if (onItemClickedListener != null)
                onItemClickedListener.onItemClicked(view, item, position);
        });

        setAdapter(adapter);
        adapter.setItems(items);
    }
}
