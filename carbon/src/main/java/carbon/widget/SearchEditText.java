package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import carbon.R;
import carbon.internal.SearchHelper;
import carbon.internal.SimpleTextWatcher;

/**
 * This implementation extends EditText directly and uses TextWatcher for tracking text changes.
 * This class can be used to create new material search fields with drop down menus separated by a
 * seam.
 */
public class SearchEditText extends EditText {

    public enum MatchMode {
        START, ADJACENT, NONADJACENT
    }

    private OnFilterListener onFilterListener;
    private String prevText = "";
    private MatchMode matchMode = MatchMode.ADJACENT;
    private int searchThreshold = 2;

    protected TextWatcher searchTextWatcher;
    SearchDataProvider dataProvider;

    public SearchEditText(Context context) {
        super(context, null, R.attr.carbon_searchEditTextStyle);
        initSearchEditText();
    }

    /**
     * XML constructor. Gets default parameters from R.attr.carbon_editTextStyle.
     *
     * @param context
     * @param attrs
     */
    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_searchEditTextStyle);
        initSearchEditText();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, R.attr.carbon_searchEditTextStyle);
        initSearchEditText();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, R.attr.carbon_searchEditTextStyle, defStyleRes);
        initSearchEditText();
    }

    private void initSearchEditText() {
        searchTextWatcher = new SimpleTextWatcher() {

            @Override
            public void afterTextChanged(Editable text) {
                if (!prevText.equals(text.toString()))
                    filter();
                prevText = text.toString();
            }
        };
        addTextChangedListener(searchTextWatcher);
    }

    public <Type> void setDataProvider(SearchDataProvider<Type> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void setMatchMode(MatchMode mode) {
        this.matchMode = mode;
    }

    public void setSearchThreshold(int threshold) {
        this.searchThreshold = threshold;
    }

    public int getSearchThreshold() {
        return searchThreshold;
    }

    private void fireOnFilterEvent(List filteredItems) {
        if (onFilterListener != null)
            onFilterListener.onFilter(filteredItems);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        prevText = getText().toString();
        super.setText(text, type);
    }

    @Override
    public Editable getText() {
        try {
            return super.getText();
        } catch (ClassCastException e) {
            return new SpannableStringBuilder("");
        }
    }

    List filteredItems = new ArrayList();

    public void filter() {
        filter(getText().toString());
    }

    public void filter(String query) {
        if (dataProvider == null) {
            return;
        }

        filteredItems.clear();
        if (query.length() < searchThreshold) {
            fireOnFilterEvent(null);
            return;
        }

        for (int i = 0; i < dataProvider.getItemCount(); i++) {
            String[] itemWords = dataProvider.getItemWords(dataProvider.getItem(i));
            matchItem(query, i, itemWords);
        }
        fireOnFilterEvent(filteredItems);
    }

    private void matchItem(String query, int i, String[] itemWords) {
        for (String itemWord : itemWords) {
            String itemText = itemWord.toLowerCase();
            if (matchMode == MatchMode.START && itemText.indexOf(query) == 0) {
                filteredItems.add(dataProvider.getItem(i));
                return;
            } else if (matchMode == MatchMode.ADJACENT && itemText.contains(query)) {
                filteredItems.add(dataProvider.getItem(i));
                return;
            } else if (matchMode == MatchMode.NONADJACENT && SearchHelper.nonadjacentMatch(itemText, query)) {
                filteredItems.add(dataProvider.getItem(i));
                return;
            }
        }
    }

    public interface OnFilterListener<Type> {
        void onFilter(List<Type> filterResults);
    }

    public <Type> void setOnFilterListener(OnFilterListener<Type> onFilterListener) {
        this.onFilterListener = onFilterListener;
    }

    public <Type> List<Type> getFilteredItems() {
        return filteredItems;
    }
}
