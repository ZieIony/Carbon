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
import carbon.internal.SimpleTextWatcher;

/**
 * This implementation extends EditText directly and uses TextWatcher for tracking text changes.
 * This class can be used to create new material search fields with drop down menus separated by a
 * seam.
 */
public class SearchEditText extends EditText {

    private OnFilterListener onFilterListener;
    private String prevText = "";

    public void setDataProvider(SearchDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public interface SearchDataProvider<Type> {

        int getItemCount();

        Type getItem(int i);

        String[] getItemWords(int i);
    }

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
                    search();
                prevText = text.toString();
            }
        };
        addTextChangedListener(searchTextWatcher);
    }

    private void search() {
        if (dataProvider == null) {
            return;
        }

        String currentWord = getText().toString();
        if (currentWord.length() == 0) {
            fireOnFilterEvent(null);
            return;
        }

        filter(currentWord);
        fireOnFilterEvent(filteredItems);
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

    public void filter(String word) {
        filteredItems.clear();
        if (word.length() == 0)
            return;

        for (int i = 0; i < dataProvider.getItemCount(); i++) {
            String[] itemWords = dataProvider.getItemWords(i);
            matchItem(word, i, itemWords);
        }
    }

    private void matchItem(String word, int i, String[] itemWords) {
        for (int j = 0; j < itemWords.length; j++) {
            String itemText = itemWords[j].toLowerCase();
            if (itemText.indexOf(word) == 0) {
                filteredItems.add(dataProvider.getItem(i));
                return;
            } else if (partialMatch(itemText, word)) {
                filteredItems.add(dataProvider.getItem(i));
                return;
            }
        }
    }

    private boolean partialMatch(String item, String word) {  // item: 'lemon', text: 'le'
        int i = 0, j = 0;
        String text = word.toLowerCase();
        for (; i < item.length() && j < text.length(); i++) {
            if (item.charAt(i) == text.charAt(j))
                j++;
        }
        return j == text.length();
    }

    public interface OnFilterListener {
        void onFilter(List filterResults);
    }

    public void setOnFilterListener(OnFilterListener onFilterListener) {
        this.onFilterListener = onFilterListener;
    }

    public List getFilteredItems() {
        return filteredItems;
    }
}
