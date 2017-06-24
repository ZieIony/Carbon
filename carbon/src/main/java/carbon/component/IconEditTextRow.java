package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.widget.EditText;

public class IconEditTextRow<Type extends IconEditTextItem> extends DataBindingComponent<Type> {

    private EditText editText;

    public IconEditTextRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_iconedittext);
        editText = getView().findViewById(R.id.carbon_text);
    }

    public EditText getEditText() {
        return editText;
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        editText.setText(text);
    }
}
