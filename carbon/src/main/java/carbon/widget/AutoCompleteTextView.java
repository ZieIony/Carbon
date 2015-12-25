package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Filterable;

import carbon.R;
import carbon.animation.AnimatedView;

/**
 * Created by Marcin on 2015-02-14.
 *
 * This implementation extends EditText directly and uses TextWatcher for tracking text changes.
 * This class can be used to create new material search fields with drop down menus separated by a seam.
 */
public class AutoCompleteTextView extends EditText implements TouchMarginView, AnimatedView {
    protected Filterable adapter;
    protected TextWatcher autoCompleteTextWatcher;

    /**
     * Listener for watching for auto complete events.
     */
    public interface OnAutoCompleteListener {
        void onAutoComplete();
    }

    public AutoCompleteTextView(Context context) {
        super(context);
        initAutoCompleteTextView();
    }

    /**
     * XML constructor. Gets default parameters from R.attr.carbon_editTextStyle.
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
        autoCompleteTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        };
        addTextChangedListener(autoCompleteTextWatcher);
    }

    /**
     * Sets an adapter with items used for auto completion. The adapter cannot be null.
     * @param adapter
     */
    public <T extends Filterable> void setAdapter(@NonNull T adapter) {
        this.adapter = adapter;
    }

    /**
     * Replaces the current text with s. Used by Adapter to set the selected item as text.
     * @param s text to replace with
     */
    public void performCompletion(String s) {
        removeTextChangedListener(autoCompleteTextWatcher);
        setText(s);
        setSelection(s.length());
        addTextChangedListener(autoCompleteTextWatcher);
    }
}
