package carbon.widget.rx;

import android.content.Context;
import android.util.AttributeSet;

public class CheckBox extends carbon.widget.CheckBox implements RxViewInterface, RxTextViewInterface, RxCheckableInterface {

    public CheckBox(Context context) {
        super(context);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
