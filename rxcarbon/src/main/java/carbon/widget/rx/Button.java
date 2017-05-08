package carbon.widget.rx;

import android.content.Context;
import android.util.AttributeSet;

public class Button extends carbon.widget.Button implements RxViewInterface, RxTextViewInterface {

    public Button(Context context) {
        super(context);
    }

    public Button(Context context, String text, OnClickListener listener) {
        super(context, text, listener);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
