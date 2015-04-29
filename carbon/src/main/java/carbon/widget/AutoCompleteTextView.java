package carbon.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Filterable;
import android.widget.ListAdapter;

import carbon.R;

/**
 * Created by Marcin on 2015-02-14.
 */
public class AutoCompleteTextView extends EditText implements TouchMarginView, AnimatedView {
    Filterable adapter;

    public static interface OnAutoCompleteListener{
        void onAutoComplete();
    }

    OnAutoCompleteListener autoCompleteListener;

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
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
                if(autoCompleteListener!=null)
                    autoCompleteListener.onAutoComplete();
            }
        });
    }

    public <T extends Filterable> void setAdapter(T adapter) {
        this.adapter = adapter;
    }

    public void setOnAutoCompleteListener(OnAutoCompleteListener autoCompleteListener) {
        this.autoCompleteListener = autoCompleteListener;
    }
}
