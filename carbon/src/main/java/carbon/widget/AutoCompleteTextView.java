package carbon.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Filterable;

import carbon.R;

/**
 * Created by Marcin on 2015-02-14.
 */
public class AutoCompleteTextView extends EditText implements TouchMarginView, AnimatedView {
    Filterable adapter;
    TextWatcher autoCompleteTextWatcher;

    public static interface OnAutoCompleteListener{
        void onAutoComplete();
    }

    public AutoCompleteTextView(Context context) {
        this(context, null);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_editTextStyle);
    }

    public AutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
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

    public <T extends Filterable> void setAdapter(T adapter) {
        this.adapter = adapter;
    }

    public void performCompletion(String s){
        removeTextChangedListener(autoCompleteTextWatcher);
        setText(s);
        setSelection(s.length());
        addTextChangedListener(autoCompleteTextWatcher);
    }
}
