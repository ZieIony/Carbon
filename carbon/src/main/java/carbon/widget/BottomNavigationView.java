package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;

import carbon.Carbon;
import carbon.R;
import carbon.component.Component;
import carbon.component.LayoutComponent;
import carbon.databinding.CarbonBottomnavigationviewItemBinding;
import carbon.drawable.ColorStateListFactory;
import carbon.recycler.RowFactory;

public class BottomNavigationView extends LinearLayout {
    public static class Item {
        private int id;
        private Drawable icon;
        private CharSequence text;
        private ColorStateList iconTint;

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
    }

    private static class ItemComponent extends LayoutComponent<Item> {
        ItemComponent(ViewGroup parent) {
            super(parent, R.layout.carbon_bottomnavigationview_item);
        }

        private final CarbonBottomnavigationviewItemBinding binding = CarbonBottomnavigationviewItemBinding.bind(getView());

        @Override
        public void bind(Item data) {
            binding.carbonBottomIcon.setImageDrawable(data.icon);
            binding.carbonBottomIcon.setTintList(data.getIconTintList() != null ? data.getIconTintList() : ColorStateListFactory.INSTANCE.makeIconSecondary(getView().getContext()));
            binding.carbonBottomText.setText(data.text);
            binding.carbonBottomText.setTextColor(data.getIconTintList() != null ? data.getIconTintList() : ColorStateListFactory.INSTANCE.makeIconSecondary(getView().getContext()));
        }
    }

    private Item[] items;
    private View activeView;
    private RowFactory<Item> itemFactory;

    RecyclerView.OnItemClickedListener<Item> listener;

    public BottomNavigationView(Context context) {
        super(context, null, R.attr.carbon_bottomNavigationViewStyle);
        initBottomNavigationView(null, R.attr.carbon_bottomNavigationViewStyle, R.style.carbon_BottomNavigationView);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_bottomNavigationViewStyle);
        initBottomNavigationView(attrs, R.attr.carbon_bottomNavigationViewStyle, R.style.carbon_BottomNavigationView);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBottomNavigationView(attrs, defStyleAttr, R.style.carbon_BottomNavigationView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBottomNavigationView(attrs, defStyleAttr, defStyleRes);
    }

    private void initBottomNavigationView(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BottomNavigationView, defStyleAttr, defStyleRes);

        itemFactory = ItemComponent::new;
        int menuId = a.getResourceId(R.styleable.BottomNavigationView_carbon_menu, 0);
        if (menuId != 0)
            setMenu(menuId);

        a.recycle();
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

    @Deprecated
    public void setItemLayout(int itemLayoutId) {
    }

    public void setItemFactory(RowFactory<Item> factory) {
        this.itemFactory = factory;
        initItems();
    }

    private void initItems() {
        removeAllViews();
        int width = getOrientation() == HORIZONTAL ? 0 : ViewGroup.LayoutParams.WRAP_CONTENT;
        int height = getOrientation() != HORIZONTAL ? 0 : ViewGroup.LayoutParams.WRAP_CONTENT;
        setWeightSum(items.length);
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (!isInEditMode()) {
                Component<Item> component = itemFactory.create(this);
                int finalI = i;
                component.getView().setOnClickListener(v -> {
                    if (component.getView() == activeView)
                        return;
                    selectItem(component.getView());
                    if (listener != null)
                        listener.onItemClicked(component.getView(), item, finalI);
                });
                component.bind(item);
                addView(component.getView(), new LinearLayout.LayoutParams(width, height, 1));
            } else {
                View view = new LinearLayout(getContext());
                addView(view, new LinearLayout.LayoutParams(width, height, 1));
            }
        }
    }

    private void selectItem(final View item) {
        if (activeView != null)
            activeView.setSelected(false);
        activeView = item;
        if (activeView != null)
            activeView.setSelected(true);
    }

    public int getSelectedIndex() {
        if (activeView == null)
            return -1;
        return indexOfChild(activeView);
    }

    public void setSelectedIndex(int index) {
        selectItem(getChildAt(index));
    }

    public void setOnItemClickListener(RecyclerView.OnItemClickedListener<Item> listener) {
        this.listener = listener;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.selectedIndex = getSelectedIndex();

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.setSelectedIndex(ss.selectedIndex);
    }

    static class SavedState implements Parcelable {
        public static final SavedState EMPTY_STATE = new SavedState() {
        };

        int selectedIndex;

        Parcelable superState;

        SavedState() {
            superState = null;
        }

        SavedState(Parcelable superState) {
            this.superState = superState != EMPTY_STATE ? superState : null;
        }

        private SavedState(Parcel in) {
            Parcelable superState = in.readParcelable(BottomNavigationView.class.getClassLoader());
            this.superState = superState != null ? superState : EMPTY_STATE;
            this.selectedIndex = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            out.writeParcelable(superState, flags);
            out.writeInt(this.selectedIndex);
        }

        public Parcelable getSuperState() {
            return superState;
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}
