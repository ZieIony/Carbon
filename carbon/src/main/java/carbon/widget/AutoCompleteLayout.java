package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import carbon.R;
import carbon.component.Component;
import carbon.recycler.DividerItemDecoration;
import carbon.recycler.RowListAdapter;

@Deprecated
public class AutoCompleteLayout extends LinearLayout {
    AutoCompleteEditText search;
    RecyclerView results;
    protected RowListAdapter<AutoCompleteEditText.FilterResult> adapter = new RowListAdapter<>(AutoCompleteEditText.FilterResult.class, AutoCompleteRow::new);

    public AutoCompleteLayout(Context context) {
        super(context);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_autoCompleteLayoutStyle);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAutoCompleteLayout();
    }

    public AutoCompleteLayout(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAutoCompleteLayout();
    }

    private void initAutoCompleteLayout() {
        View.inflate(getContext(), R.layout.carbon_autocompletelayout, this);
        setOrientation(VERTICAL);
        search = findViewById(R.id.carbon_autoCompleteSearch);
        results = findViewById(R.id.carbon_autoCompleteResults);
        results.setLayoutManager(new LinearLayoutManager(getContext()));
        results.addItemDecoration(new DividerItemDecoration(getContext()));
        results.setAdapter(adapter);
        search.setOnFilterListener(filteringResults -> {
            if (filteringResults == null) {
                adapter.setItems(new ArrayList<>());
                return;
            }

            //adapter.setItems(new ArrayList<>(filteringResults));
        });
        adapter.setOnItemClickedListener((view, item, position) -> search.performCompletion(item.text.toString()));
    }

    public void setDataProvider(SearchAdapter dataProvider) {
        search.setDataProvider(dataProvider);
    }

    public static class AutoCompleteRow extends Component<AutoCompleteEditText.FilterResult> {

        private final carbon.widget.TextView text;

        public AutoCompleteRow(ViewGroup parent) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carbon_autocompletelayout_row, parent, false);
            text = view.findViewById(R.id.carbon_autoCompleteLayoutRowText);
        }

        @Override
        public void bind(AutoCompleteEditText.FilterResult data) {
            text.setText(data.getItem().toString());
        }
    }
}
