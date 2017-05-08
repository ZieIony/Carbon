package carbon.widget.rx;

import android.content.Context;
import android.util.AttributeSet;

public class AutoCompleteEditText extends carbon.widget.AutoCompleteEditText implements RxViewInterface, RxTextViewInterface {

    public AutoCompleteEditText(Context context) {
        super(context);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
