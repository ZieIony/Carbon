package carbon.widget.rx;

import android.content.Context;
import android.util.AttributeSet;

public class CheckedTextView extends carbon.widget.CheckedTextView implements RxViewInterface, RxTextViewInterface, RxCheckableInterface {

    public CheckedTextView(Context context) {
        super(context);
    }

    public CheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
