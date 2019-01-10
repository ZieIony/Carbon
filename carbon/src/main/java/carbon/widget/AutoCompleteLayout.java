package carbon.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import carbon.Carbon;
import carbon.R;
import carbon.component.Component;
import carbon.recycler.DividerItemDecoration;
import carbon.recycler.RowListAdapter;

public class AutoCompleteLayout extends LinearLayout {
    AutoCompleteEditText search;
    RecyclerView results;
    protected RowListAdapter<AutoCompleteEditText.FilterResult> adapter = new RowListAdapter<>(AutoCompleteEditText.FilterResult.class, AutoCompleteRow::new);

    public AutoCompleteLayout(Context context) {
        super(context);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.AutoCompleteLayout, R.attr.carbon_autoCompleteLayoutStyle, R.styleable.AutoCompleteLayout_carbon_theme), attrs, R.attr.carbon_autoCompleteLayoutStyle);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.AutoCompleteLayout, R.attr.carbon_autoCompleteLayoutStyle, R.styleable.AutoCompleteLayout_carbon_theme), attrs, defStyleAttr);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(Carbon.getThemedContext(context, attrs, R.styleable.AutoCompleteLayout, R.attr.carbon_autoCompleteLayoutStyle, R.styleable.AutoCompleteLayout_carbon_theme), attrs, defStyleAttr, defStyleRes);
        initAutoCompleteLayout();
    }

    private void initAutoCompleteLayout() {
        View.inflate(getContext(), R.layout.carbon_autocompletelayout, this);
        setOrientation(VERTICAL);
        search = findViewById(R.id.carbon_autoCompleteSearch);
        results = findViewById(R.id.carbon_autoCompleteResults);
        results.setLayoutManager(new LinearLayoutManager(getContext()));
        ColorDrawable colorDrawable = new ColorDrawable(Carbon.getThemeColor(getContext(), R.attr.carbon_dividerColor));
        int dividerWidth = getResources().getDimensionPixelSize(R.dimen.carbon_1dip);
        results.addItemDecoration(new DividerItemDecoration(colorDrawable, dividerWidth));
        results.setAdapter(adapter);
        search.setOnFilterListener(filteringResults -> {
            if (filteringResults == null) {
                adapter.setItems(new ArrayList<>());
                return;
            }

            adapter.setItems(new ArrayList<>(filteringResults));
        });
        adapter.setOnItemClickedListener((view, item, position) -> search.performCompletion(item.text.toString()));
    }

    public void setDataProvider(AutoCompleteEditText.AutoCompleteDataProvider dataProvider) {
        search.setDataProvider(dataProvider);
    }

    public static class AutoCompleteRow implements Component<AutoCompleteEditText.FilterResult> {

        private final carbon.widget.TextView text;
        private final View view;

        public AutoCompleteRow(ViewGroup parent) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carbon_autocompletelayout_row, parent, false);
            text = view.findViewById(R.id.carbon_autoCompleteLayoutRowText);
        }

        @Override
        public View getView() {
            return view;
        }

        @Override
        public void bind(AutoCompleteEditText.FilterResult data) {
            text.setText(data.getItem().toString());
        }
    }
}
