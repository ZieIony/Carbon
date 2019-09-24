package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import carbon.BR;
import carbon.CarbonContextWrapper;
import carbon.R;

public class BottomBar extends FrameLayout {
    public static class Item {
        private final Drawable icon;
        private final String text;

        public Item(Drawable icon, String text) {
            this.icon = icon;
            this.text = text;
        }

        public Drawable getIcon() {
            return icon;
        }

        public String getText() {
            return text;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    private LinearLayout content;
    private Item[] items;
    private View activeView;
    private int itemLayoutId;

    OnItemClickListener listener;

    public BottomBar(Context context) {
        super(context, null, R.attr.carbon_bottomBarStyle);
        initBottomBar(null, R.attr.carbon_bottomBarStyle);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_bottomBarStyle);
        initBottomBar(attrs, R.attr.carbon_bottomBarStyle);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBottomBar(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initBottomBar(attrs, defStyleAttr);
    }

    private void initBottomBar(AttributeSet attrs, int defStyleAttr) {
        inflate(getContext(), R.layout.carbon_bottombar, this);
        content = findViewById(R.id.carbon_bottomBarContent);

        if (attrs == null)
            return;

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BottomBar, defStyleAttr, R.style.carbon_BottomBar);

        itemLayoutId = a.getResourceId(R.styleable.BottomBar_carbon_itemLayout, R.layout.carbon_bottombar_item);
        int menuId = a.getResourceId(R.styleable.BottomBar_carbon_menu, 0);
        if (menuId != 0)
            setMenu(menuId);

        a.recycle();
    }

    public void setItems(Item[] items) {
        this.items = items;
        initItems();
    }

    public void setMenu(int resId) {
        Menu menu = new MenuBuilder(CarbonContextWrapper.wrap(getContext()));
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(resId, menu);
        setMenu(menu);
    }

    public void setMenu(Menu menu) {
        items = new Item[menu.size()];
        for (int i = 0; i < menu.size(); i++) {
            final MenuItem item = menu.getItem(i);
            items[i] = new Item(item.getIcon(), item.getTitle().toString());
        }
        initItems();
    }

    public void setItemLayout(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
        initItems();
    }

    private void initItems() {
        content.removeAllViews();
        content.setWeightSum(items.length);
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), itemLayoutId, this, false);
            binding.getRoot().setOnClickListener(v -> {
                if (binding.getRoot() == activeView)
                    return;
                selectItem(binding.getRoot());
                if (listener != null)
                    listener.onItemClick(item);
            });
            binding.setVariable(BR.data, item);
            content.addView(binding.getRoot(), new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
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
        return content.indexOfChild(activeView);
    }

    public void setSelectedIndex(int index) {
        selectItem(content.getChildAt(index));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
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
            Parcelable superState = in.readParcelable(BottomBar.class.getClassLoader());
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
