package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;

import carbon.R;

/**
 * Created by Marcin on 2015-02-12.
 * <p/>
 * This class is just a LinearLayout with a dedicated style making it look like a
 * real CardView. To make a CardView of one of the other layouts you have to
 * set it's corner radius to 2dp, add a shadow and a margin.
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
