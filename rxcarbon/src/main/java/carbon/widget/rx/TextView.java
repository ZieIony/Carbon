package carbon.widget.rx;

import android.content.Context;
import android.util.AttributeSet;

public class TextView extends carbon.widget.TextView implements RxViewInterface, RxTextViewInterface {

    public TextView(Context context) {
        super(context);
    }

    public TextView(Context context, String text) {
        super(context, text);
    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
