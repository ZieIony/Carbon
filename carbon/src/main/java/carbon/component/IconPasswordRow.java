package carbon.component;

import android.text.method.PasswordTransformationMethod;
import android.view.ViewGroup;

import carbon.R;
import carbon.widget.EditText;

public class IconPasswordRow<Type extends IconPasswordItem> extends DataBindingComponent<Type> {

    private EditText editText;

    public IconPasswordRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_iconpassword);
        editText = getView().findViewById(R.id.carbon_text);
        editText.setTransformationMethod(new PasswordTransformationMethod());
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
