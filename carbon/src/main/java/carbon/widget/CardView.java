package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;

import carbon.R;

/**
 * Created by Marcin on 2015-02-12.
 */
public class CardView extends LinearLayout {

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_cardViewStyle);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
