package carbon.component;

import android.view.ViewGroup;

import carbon.R;
import carbon.recycler.RowFactory;
import carbon.widget.EditText;

public class IconEditTextRow extends DataBindingComponent<IconEditTextItem> {
    public static final RowFactory FACTORY = IconEditTextRow::new;

    private EditText editText;

    public IconEditTextRow(ViewGroup parent) {
        super(parent, R.layout.carbon_row_iconedittext);
        editText = (EditText) getView().findViewById(R.id.text);
    }

    public EditText getEditText() {
        return editText;
    }

    public String getText(){
        return editText.getText().toString();
    }

    public void setText(String text){
        editText.setText(text);
    }
}
