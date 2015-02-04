package pl.zielony.carbon.beta;

import android.content.Context;
import android.util.AttributeSet;

import pl.zielony.carbon.widget.Roboto;

/**
 * Created by Marcin on 2014-11-07.
 */
public class EditText extends android.widget.EditText {

    public EditText(Context context) {
        super(context);
        init();
    }

    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        if (!isInEditMode())
            setTypeface(Roboto.getTypeface(getContext(), Roboto.Style.Regular));
    }


}
