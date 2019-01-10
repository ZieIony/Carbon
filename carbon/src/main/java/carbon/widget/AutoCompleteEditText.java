package carbon.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import carbon.internal.SimpleTextWatcher;

/**
 * This implementation extends EditText directly and uses TextWatcher for tracking text changes.
 * This class can be used to create new material search fields with drop down menus separated by a
 * seam.
 */
public class AutoCompleteEditText extends EditText {

    public static final int FILTERING_START = 0, FILTERING_PARTIAL = 1;

    private boolean autoCompleting = false;
    private int prevOptions;
    private OnFilterListener onFilterListener;
    private String prevText = "";

    public void setDataProvider(AutoCompleteDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @SuppressLint("ParcelCreator")
    public static class HintSpan extends ForegroundColorSpan {

        public HintSpan(int color) {
            super(color);
        }
    }

    public static class FilterResult implements Comparable<FilterResult>, Serializable {
        int type;
        Spannable text;
        private Object item;

        public FilterResult(int type, Spannable text, Object item) {
            this.type = type;
            this.text = text;
            this.item = item;
        }

        public int getType() {
            return type;
        }

        public Spannable getText() {
            return text;
        }

        public Object getItem() {
            return item;
        }

        @Override
        public int compareTo(@NonNull FilterResult o) {
            if (type != o.type)
                return type - o.type;
            if (type == FILTERING_PARTIAL) {
                if (text.length() != o.text.length())
                    return text.length() - o.text.length();
            }
            return text.toString().compareTo(o.text.toString());
        }

        @Override
        public boolean equals(Object obj) {
            return text.equals(((FilterResult) obj).text);
        }
    }

    public interface AutoCompleteDataProvider<Type> {

        int getItemCount();

        Type getItem(int i);

        String[] getItemWords(int i);
    }

    protected TextWatcher autoCompleteTextWatcher;
    AutoCompleteDataProvider dataProvider;

    public AutoCompleteEditText(Context context) {
        super(context);
        initAutoCompleteEditText();
    }

    /**
     * XML constructor. Gets default parameters from R.attr.carbon_editTextStyle.
     *
     * @param context
     * @param attrs
     */
    public AutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAutoCompleteEditText();
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAutoCompleteEditText();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoCompleteEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAutoCompleteEditText();
    }

    private void initAutoCompleteEditText() {
        autoCompleteTextWatcher = new SimpleTextWatcher() {

            @Override
            public void afterTextChanged(Editable text) {
                if (!prevText.equals(text.toString()))
                    autoComplete();
                prevText = text.toString();
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
                AutoCompleteEditText.super.setImeOptions(prevOptions);
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

        for (HintSpan span : spans) {
            text.delete(text.getSpanStart(span), text.getSpanEnd(span));
        }

        Word currentWord = getCurrentWord();
        if (currentWord == null || currentWord.length() == 0) {
            fireOnFilterEvent(null);
            return;
        }

        autoCompleting = true;
        filter(currentWord);
        fireOnFilterEvent(filteredItems);

        if (filteredItems.size() != 0 && filteredItems.get(0).type == FILTERING_START) {
            String word = filteredItems.get(0).text.toString();
            String remainingPart = word.substring(currentWord.preCursor.length());
            text.insert(selStart, remainingPart);
            HintSpan span = new HintSpan(getCurrentHintTextColor());
            setSelection(selStart);
            text.setSpan(span, selStart, selStart + remainingPart.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            AutoCompleteEditText.super.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }

        autoCompleting = false;
    }

    private void fireOnFilterEvent(List<FilterResult> filteredItems) {
        if (onFilterListener != null)
            onFilterListener.onFilter(filteredItems);
    }

    static class Word {
        String preCursor;
        String postCursor;

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
            position = text.getSpanStart(spans[0]);
        word.postCursor = text.subSequence(position, i).toString();
        if (word.length() == 0) {
            text.delete(getSelectionStart(), i);
            return null;
        }

        return word;
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
        }
        autoComplete();
        autoCompleting = false;
        super.onSelectionChanged(selStart, selEnd);
    }

    List<FilterResult> filteredItems = new ArrayList<>();

    public void filter(AutoCompleteEditText.Word word) {
        filteredItems.clear();
        if (word.length() == 0)
            return;

        String preCursor = word.preCursor.toLowerCase();
        for (int i = 0; i < dataProvider.getItemCount(); i++) {
            String[] itemWords = dataProvider.getItemWords(i);
            matchItem(word, preCursor, i, itemWords);
        }
        Collections.sort(filteredItems);
    }

    private void matchItem(Word word, String preCursor, int i, String[] itemWords) {
        for (int j = 0; j < itemWords.length; j++) {
            String itemText = itemWords[j];
            if (itemText.length() == word.length())
                continue;
            itemText = itemText.toLowerCase();
            if (itemText.indexOf(preCursor) == 0 && word.postCursor.length() == 0) {
                Spannable spannable = new SpannableStringBuilder(itemText);
                spannable.setSpan(new HintSpan(getCurrentHintTextColor()), preCursor.length(), itemText.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                filteredItems.add(new FilterResult(AutoCompleteEditText.FILTERING_START, spannable, dataProvider.getItem(i)));
                return;
            } else {
                Spannable spannable = partialMatch(itemText, word);
                if (spannable != null) {
                    filteredItems.add(new FilterResult(AutoCompleteEditText.FILTERING_PARTIAL, spannable, dataProvider.getItem(i)));
                    return;
                }
            }
        }
    }

    private Spannable partialMatch(String item, AutoCompleteEditText.Word word) {  // item: 'lemon', text: 'le'
        Spannable spannable = new SpannableStringBuilder(item);
        int i = 0, j = 0;
        String text = word.toString().toLowerCase();
        for (; i < item.length() && j < text.length(); i++) {
            if (item.charAt(i) == text.charAt(j)) {
                j++;
            } else {
                spannable.setSpan(new AutoCompleteEditText.HintSpan(getCurrentHintTextColor()), i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        spannable.setSpan(new AutoCompleteEditText.HintSpan(getCurrentHintTextColor()), i, item.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (j == text.length())
            return spannable;
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
        if (spans.length > 1)
            throw new IllegalStateException("more than one HintSpan");
        Word word = getCurrentWord();
        if (word == null)
            throw new IllegalStateException("no word to complete");
        autoCompleting = true;
        //for (HintSpan span : spans)
        //    text.delete(text.getSpanStart(span), text.getSpanEnd(span));
        text.delete(selStart, selStart + word.postCursor.length());
        text.delete(selStart - word.preCursor.length(), selStart);
        text.insert(selStart - word.preCursor.length(), s);
        setSelection(selStart - word.preCursor.length() + s.length());
        fireOnFilterEvent(null);
        super.setImeOptions(prevOptions);
        autoCompleting = false;
    }

    public interface OnFilterListener {
        void onFilter(List<FilterResult> filterResults);
    }

    public void setOnFilterListener(OnFilterListener onFilterListener) {
        this.onFilterListener = onFilterListener;
    }

    public List<FilterResult> getFilteredItems() {
        return filteredItems;
    }
}
