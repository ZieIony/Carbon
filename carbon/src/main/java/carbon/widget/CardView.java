package carbon.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import carbon.R;

/**
 * Created by Marcin on 2015-02-12.
 * <p/>
 * This class is just a RelativeLayout with a dedicated style making it look like a
 * real CardView. To make a CardView of one of the other layouts you have to
 * set it's corner radius to 2dp, add a shadow and a margin.
 */
@Deprecated
public class CardView extends RelativeLayout {

    public CardView(Context context) {
        super(context, null, R.attr.carbon_cardViewStyle);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.carbon_cardViewStyle);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
