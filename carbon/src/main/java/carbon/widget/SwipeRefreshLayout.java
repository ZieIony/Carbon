package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Marcin on 2016-06-10.
 */
@Deprecated
public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {
    public SwipeRefreshLayout(Context context) {
        super(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRefreshingAsync(final boolean refreshing) {
        post(new Runnable() {
            @Override
            public void run() {
                setRefreshing(refreshing);
            }
        });
    }
}
