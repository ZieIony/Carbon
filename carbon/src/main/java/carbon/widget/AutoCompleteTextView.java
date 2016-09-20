package carbon.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import carbon.R;
import carbon.animation.AnimatedView;
import carbon.internal.SimpleTextWatcher;
import carbon.recycler.Row;
import carbon.recycler.RowListAdapter;

/**
 * Created by Marcin on 2015-02-14.
 * <p/>
 * This implementation extends EditText directly and uses TextWatcher for tracking text changes.
 * This class can be used to create new material search fields with drop down menus separated by a seam.
 */
public class AutoCompleteTextView extends EditText implements TouchMarginView, AnimatedView {

    public static final int FILTERING_START = 0, FILTERING_PARTIAL = 1;

    private boolean autoCompleting = false;
    private int prevOptions;

    public void setDataProvider(AutoCompleteDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @SuppressLint("ParcelCreator")
    public static class HintSpan extends ForegroundColorSpan {

        public HintSpan(int color) {
            super(color);
        }
    }

    public static class FilteringResult implements Comparable<FilteringResult> {
        int type;
        Spannable text;

        public FilteringResult(int type, Spannable text) {
            this.type = type;
            this.text = text;
        }

        public int getType() {
            return type;
        }

        public Spannable getText() {
            return text;
        }

        @Override
        public int compareTo(@NonNull FilteringResult o) {
            if (type != o.type)
                return type - o.type;
            if (type == FILTERING_PARTIAL) {
                if (text.length() != o.text.length())
                    return text.length() - o.text.length();
            }
            return text.toString().compareTo(o.text.toString());
        }
    }

    public static abstract class AutoCompleteDataProvider {

        public abstract int getItemCount();

        public abstract String getItem(int i);
    }

    protected RowListAdapter<FilteringResult> adapter = new RowListAdapter<>(AutoCompleteRow::new);
    protected TextWatcher autoCompleteTextWatcher;
    AutoCompleteDataProvider dataProvider;

    public AutoCompleteTextView(Context context) {
        super(context);
        initAutoCompleteTextView();
    }

    /**
     * XML constructor. Gets default parameters from R.attr.carbon_editTextStyle.
     *
     * @param context
     * @param attrs
     */
    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAutoCompleteTextView();
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAutoCompleteTextView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAutoCompleteTextView();
    }

    private void initAutoCompleteTextView() {
        autoCompleteTextWatcher = new SimpleTextWatcher() {

            @Override
            public void afterTextChanged(Editable text) {
                autoComplete();
            }
        };
        addTextChangedListener(autoCompleteTextWatcher);
        setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                autoCompleting = true;
                Editable text = getText();
                HintSpan[] spans = text.getSpans(0, length(), HintSpan.class);
                if (spans.length > 1) {
                    throw new IllegalStateException("more than one HintSpan");
                }
                int cursorPosition = getSelectionStart();
                for (HintSpan span : spans) {
                    if (cursorPosition == text.getSpanStart(span)) {
                        text.removeSpan(span);
                        break;
                    }
                }
                setSelection(cursorPosition);
                AutoCompleteTextView.super.setImeOptions(prevOptions);
                autoCompleting = false;
            }
            return false;
        });
    }

    private void autoComplete() {
        if (dataProvider == null) {
            return;
        }
        Editable text = getText();
        if (autoCompleting) {
            return;
        }
        HintSpan[] spans = text.getSpans(0, length(), HintSpan.class);
        if (spans.length > 1) {
            throw new IllegalStateException("more than one HintSpan");
        }
        int selStart = getSelectionStart();
        if (selStart != getSelectionEnd()) {
            return;
        }

        Word currentWord = getCurrentWord();
        if (currentWord == null) {
            adapter.items.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        autoCompleting = true;
        for (HintSpan span : spans) {
            text.delete(text.getSpanStart(span), text.getSpanEnd(span));
        }

        List<FilteringResult> filter = filter(currentWord);
        adapter.setItems(filter);
        adapter.notifyDataSetChanged();

        if (filter.size() != 0 && filter.get(0).type == FILTERING_START) {
            String word = filter.get(0).text.toString();
            String remainingPart = word.substring(currentWord.preCursor.length());
            text.insert(selStart, remainingPart);
            HintSpan span = new HintSpan(getCurrentHintTextColor());
            setSelection(selStart);
            text.setSpan(span, selStart, selStart + remainingPart.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            AutoCompleteTextView.super.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }

        autoCompleting = false;
    }

    public static class Word {
        public String preCursor;
        public String postCursor;

        @Override
        public String toString() {
            return preCursor + postCursor;
        }

        public int length() {
            return preCursor.length() + postCursor.length();
        }
    }

    private Word getCurrentWord() {
        if (getSelectionStart() != getSelectionEnd())
            return null;
        int position = getSelectionStart();
        Editable text = getText();

        Word word = new Word();
        int i;
        for (i = position - 1; i >= 0; i--) {
            char c = text.charAt(i);
            if (!Character.isLetterOrDigit(c))
                break;
        }
        word.preCursor = text.subSequence(i + 1, position).toString();
        for (i = position; i < length(); i++) {
            char c = text.charAt(i);
            if (!Character.isLetterOrDigit(c))
                break;
        }
        HintSpan[] spans = text.getSpans(0, length(), HintSpan.class);
        if (spans.length > 0)
            position = text.getSpanEnd(spans[0]);
        word.postCursor = text.subSequence(position, i).toString();

        return word;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (autoCompleting)
            return;
        if (selStart == selEnd) {
            Editable text = getText();
            HintSpan[] spans = text.getSpans(0, length(), HintSpan.class);
            if (spans.length > 1)
                throw new IllegalStateException("more than one HintSpan");
            autoCompleting = true;
            if (spans.length == 1) {
                HintSpan span = spans[0];
                if (selStart >= text.getSpanStart(span) && selStart < text.getSpanEnd(span)) {
                    setSelection(text.getSpanStart(span));
                } else if (selStart == text.getSpanEnd(span)) {
                    text.removeSpan(span);
                    super.setImeOptions(prevOptions);
                }
            }
            autoCompleting = false;
        }
        autoComplete();
        super.onSelectionChanged(selStart, selEnd);
    }

    List<AutoCompleteTextView.FilteringResult> filteredItems = new ArrayList<>();

    public List<AutoCompleteTextView.FilteringResult> filter(AutoCompleteTextView.Word word) {
        filteredItems.clear();
        if (word.length() == 0) {
            return filteredItems;
        }
        String text = word.preCursor.toLowerCase();
        for (int i = 0; i < dataProvider.getItemCount(); i++) {
            String item = dataProvider.getItem(i);
            if (item.length() == word.length()) {
                continue;
            }
            item = item.toLowerCase();
            if (item.indexOf(text) == 0 && word.postCursor.length() == 0) {
                Spannable spannable = new SpannableStringBuilder(item);
                spannable.setSpan(new AutoCompleteTextView.HintSpan(getCurrentHintTextColor()), text.length(), item.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                filteredItems.add(new AutoCompleteTextView.FilteringResult(AutoCompleteTextView.FILTERING_START, spannable));
            } else {
                Spannable spannable = partialMatch(item, word);
                if (spannable != null) {
                    filteredItems.add(new AutoCompleteTextView.FilteringResult(AutoCompleteTextView.FILTERING_PARTIAL, spannable));
                }
            }
        }
        Collections.sort(filteredItems);
        return filteredItems;
    }

    private Spannable partialMatch(String item, AutoCompleteTextView.Word word) {  // item: 'lemon', text: 'le'
        Spannable spannable = new SpannableStringBuilder(item);
        int i = 0, j = 0;
        String text = word.toString().toLowerCase();
        for (; i < item.length() && j < text.length(); i++) {
            if (item.charAt(i) == text.charAt(j)) {
                j++;
            } else {
                spannable.setSpan(new AutoCompleteTextView.HintSpan(getCurrentHintTextColor()), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        spannable.setSpan(new AutoCompleteTextView.HintSpan(getCurrentHintTextColor()), i, item.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (j == text.length()) {
            return spannable;
        }
        return null;
    }

    @Override
    public void setImeOptions(int imeOptions) {
        super.setImeOptions(imeOptions);
        prevOptions = imeOptions;
    }

    /**
     * Replaces the current word with s. Used by Adapter to set the selected item as text.
     *
     * @param s text to replace with
     */
    public void performCompletion(String s) {
        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        if (selStart != selEnd)
            return;
        Editable text = getText();
        HintSpan[] spans = text.getSpans(0, length(), HintSpan.class);
        if (spans.length > 1) {
            throw new IllegalStateException("more than one HintSpan");
        }
        Word word = getCurrentWord();
        if (word == null) {
            throw new IllegalStateException("no word to complete");
        }
        autoCompleting = true;
        for (HintSpan span : spans) {
            text.delete(text.getSpanStart(span), text.getSpanEnd(span));
        }
        text.delete(selStart, selStart + word.postCursor.length());
        text.delete(selStart - word.preCursor.length(), selStart);
        text.insert(selStart - word.preCursor.length(), s);
        setSelection(selStart - word.preCursor.length() + s.length());
        adapter.items.clear();
        adapter.notifyDataSetChanged();
        super.setImeOptions(prevOptions);
        autoCompleting = false;
    }

    public RowListAdapter<FilteringResult> getAdapter() {
        return adapter;
    }

    public void setAdapter(RowListAdapter<FilteringResult> adapter) {
        this.adapter = adapter;
    }

    static class AutoCompleteRow implements Row<FilteringResult> {

        private final carbon.widget.TextView text;
        private final View view;

        public AutoCompleteRow(ViewGroup parent) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carbon_autocompletelayout_row, parent, false);
            text = (carbon.widget.TextView) view.findViewById(R.id.carbon_autoCompleteLayoutRowText);
        }

        @Override
        public View getView() {
            return view;
        }

        @Override
        public void bind(FilteringResult data) {
            text.setText(data.text);
        }
    }
}
