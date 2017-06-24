package carbon.beta;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;

import carbon.Carbon;
import carbon.R;
import carbon.component.BottomSheetCell;
import carbon.component.BottomSheetRow;
import carbon.component.DividerItem;
import carbon.component.DividerRow;
import carbon.component.MenuItem;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.internal.Menu;
import carbon.recycler.RowListAdapter;
import carbon.widget.LinearLayout;
import carbon.widget.RecyclerView;
import carbon.widget.TextView;

public class BottomSheetLayout extends LinearLayout {

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

    public void setMenu(final android.view.Menu baseMenu) {
        menu = Carbon.getMenu(getContext(), baseMenu);
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
        items.addAll(menu.getVisibleItems());
        if (style == Style.List) {
            for (int i = 0; i < items.size() - 1; i++) {
                if (((android.view.MenuItem) items.get(i)).getGroupId() != ((android.view.MenuItem) items.get(i + 1)).getGroupId())
                    items.add(++i, new DividerItem());
            }
            items.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        }

        RowListAdapter<Serializable> adapter = new RowListAdapter<>(MenuItem.class, style == Style.List ? BottomSheetRow::new : BottomSheetCell::new);
        adapter.addFactory(PaddingItem.class, PaddingRow::new);
        adapter.addFactory(DividerItem.class, DividerRow::new);
        adapter.setItems(items);

        recycler.setAdapter(adapter);
    }
}
