package carbon.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import carbon.R;
import carbon.animation.AnimatedView;
import carbon.internal.SimpleTextWatcher;

/**
 * Created by Marcin on 2015-02-14.
 * <p/>
 * This implementation extends EditText directly and uses TextWatcher for tracking text changes.
 * This class can be used to create new material search fields with drop down menus separated by a seam.
 */
public class AutoCompleteTextView extends EditText implements TouchMarginView, AnimatedView {
    public static final int FILTERING_START = 0, FILTERING_MIDDLE = 1, FILTERING_PARTIAL = 2;

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
        public abstract List<FilteringResult> filter(Word word);
    }

    protected AutoCompleteAdapter adapter = new AutoCompleteAdapter();
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
                if (autoCompleting)
                    return;
                HintSpan[] spans = text.getSpans(0, length(), HintSpan.class);
                if (spans.length > 1)
                    throw new IllegalStateException("more than one HintSpan");
                int selStart = getSelectionStart();
                if (selStart != getSelectionEnd())
                    return;

                Word currentWord = getCurrentWord();
                if (currentWord == null) {
                    adapter.items.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }

                autoCompleting = true;
                for (HintSpan span : spans)
                    text.delete(text.getSpanStart(span), text.getSpanEnd(span));

                List<FilteringResult> filter = dataProvider.filter(currentWord);
                adapter.setItems(filter);
                adapter.notifyDataSetChanged();

                if (filter.size() != 0 && filter.get(0).type == FILTERING_START && currentWord.postCursor.length() == 0) {
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
        };
        addTextChangedListener(autoCompleteTextWatcher);
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    autoCompleting = true;
                    Editable text = getText();
                    HintSpan[] spans = text.getSpans(0, length(), HintSpan.class);
                    if (spans.length > 1)
                        throw new IllegalStateException("more than one HintSpan");
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
            }
        });
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
            for (HintSpan span : spans) {
                if (selStart >= text.getSpanStart(span) && selStart < text.getSpanEnd(span)) {
                    setSelection(text.getSpanStart(span));
                    break;
                } else if (selStart == text.getSpanEnd(span)) {
                    text.removeSpan(span);
                    adapter.items.clear();
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
            super.setImeOptions(prevOptions);
            autoCompleting = false;
        }
        super.onSelectionChanged(selStart, selEnd);
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
        String word = getCurrentWord().preCursor;
        if (word == null)
            throw new IllegalStateException("no word to complete");
        autoCompleting = true;
        for (HintSpan span : spans)
            text.delete(text.getSpanStart(span), text.getSpanEnd(span));
        text.delete(selStart - word.length(), selStart);
        text.insert(selStart - word.length(), s);
        setSelection(selStart - word.length() + s.length());
        adapter.items.clear();
        adapter.notifyDataSetChanged();
        super.setImeOptions(prevOptions);
        autoCompleting = false;
    }

    public AutoCompleteAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(AutoCompleteAdapter adapter) {
        this.adapter = adapter;
    }

    public class AutoCompleteAdapter extends RecyclerView.Adapter<carbon.widget.AutoCompleteTextView.AutoCompleteAdapter.ViewHolder> {
        private List<FilteringResult> items = new ArrayList<>();
        private OnHintClicked onHintClicked;

        public AutoCompleteAdapter() {
        }

        public void setItems(List<FilteringResult> items) {
            this.items = items;
        }

        @Override
        public carbon.widget.AutoCompleteTextView.AutoCompleteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new carbon.widget.AutoCompleteTextView.AutoCompleteAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carbon_autocomplete_row, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(final carbon.widget.AutoCompleteTextView.AutoCompleteAdapter.ViewHolder viewHolder, final int i) {
            viewHolder.text.setText(items.get(i).text);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String item = items.get(viewHolder.getAdapterPosition()).text.toString();
                    performCompletion(item);
                    if (onHintClicked != null)
                        onHintClicked.onHintClicked(item);
                }
            });
        }

        public String getItem(int position) {
            return items.get(position).toString();
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setOnHintClicked(OnHintClicked onHintClicked) {
            this.onHintClicked = onHintClicked;
        }

        public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
            carbon.widget.TextView text;

            public ViewHolder(View itemView) {
                super(itemView);
                text = (carbon.widget.TextView) itemView.findViewById(R.id.text);
            }
        }
    }

}
