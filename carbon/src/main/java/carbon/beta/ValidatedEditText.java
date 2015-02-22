package carbon.beta;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import carbon.R;
import carbon.widget.OnValidateListener;
import carbon.widget.TextView;

/**
 * Created by Marcin on 2015-01-13.
 */
public class ValidatedEditText extends android.widget.FrameLayout {
    OnValidateListener validatelistener;
    private boolean isValid;

    public ValidatedEditText(Context context) {
        super(context);
        init();
    }

    public ValidatedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ValidatedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.carbon_validated_edittext, this);
        EditText editText = (EditText) findViewById(R.id.text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validatelistener != null) {
                    isValid = validatelistener.onValidate(s.toString());
                    findViewById(R.id.error).setVisibility(isValid ? View.INVISIBLE : View.VISIBLE);
                    invalidate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setOnValidateListener(OnValidateListener listener) {
        this.validatelistener = listener;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setError(String text) {
        TextView error = (TextView) findViewById(R.id.error);
        error.setText(text);
    }
}
