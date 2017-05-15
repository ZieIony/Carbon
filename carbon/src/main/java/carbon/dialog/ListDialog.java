package carbon.dialog;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import carbon.R;
import carbon.recycler.RowFactory;
import carbon.recycler.RowListAdapter;
import carbon.widget.LinearLayout;
import carbon.widget.RecyclerView;

public class ListDialog<Type> extends DialogBase {
    protected RecyclerView recyclerView;
    protected RowListAdapter adapter;
    protected List<Type> items;
    protected RecyclerView.OnItemClickedListener listener;
    private RecyclerView.OnItemClickedListener internalListener = getInternalListener();

    public ListDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public ListDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init() {
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf);
        recyclerView.setPadding(0, padding, 0, padding);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        super.setContentView(recyclerView, null);
    }

    protected RecyclerView.OnItemClickedListener getInternalListener() {
        return (view, item, position) -> {
            if (listener != null)
                listener.onItemClicked(view, item, position);
            dismiss();
        };
    }

    protected void dividerCallback(int contentHeight) {
        int padding = getContext().getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf);
        int height = padding * 2;
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            height += recyclerView.getChildAt(i).getHeight();
        }
        height += recyclerView.getAdapter().getItemCount() - recyclerView.getChildCount();
        if (height <= contentHeight) {
            if (topDivider != null)
                topDivider.setVisibility(View.GONE);
            if (bottomDivider != null)
                bottomDivider.setVisibility(View.GONE);
        } else {
            if (topDivider != null)
                topDivider.setVisibility(View.VISIBLE);
            if (bottomDivider != null)
                bottomDivider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        throw new RuntimeException("not supported");
    }

    @Override
    public void setContentView(@NonNull View view) {
        throw new RuntimeException("not supported");
    }

    @Override
    public void setContentView(@NonNull View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("not supported");
    }

    public void setItems(Type[] items, RowFactory<Type> factory) {
        this.items = Arrays.asList(items);
        adapter = new RowListAdapter<>(this.items, factory);
        adapter.setOnItemClickedListener(internalListener);
        recyclerView.setAdapter(adapter);
    }

    public void setItems(List<Type> items, RowFactory<Type> factory) {
        this.items = items;
        adapter = new RowListAdapter<>(items, factory);
        adapter.setOnItemClickedListener(internalListener);
        recyclerView.setAdapter(adapter);
    }

    public void setOnItemClickedListener(RecyclerView.OnItemClickedListener listener) {
        this.listener = listener;
    }
}
