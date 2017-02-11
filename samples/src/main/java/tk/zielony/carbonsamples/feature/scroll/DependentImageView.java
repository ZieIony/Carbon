package tk.zielony.carbonsamples.feature.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import carbon.widget.FloatingActionButton;

/**
 * Created by Marcin on 2017-02-10.
 */

public class DependentImageView extends FloatingActionButton implements BehaviorView {
    public DependentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void dependedViewChanged(View view) {
        Log.e("dasd", "" + view.getHeight());
        if (view.getHeight() == ((CollapsingLayout) view).getMinimumHeight() && getVisibility() == VISIBLE && getAnimator() == null) {
            setVisibility(INVISIBLE);
        } else if (view.getHeight() != ((CollapsingLayout) view).getMinimumHeight() && getVisibility() != VISIBLE && getAnimator() == null) {
            setVisibility(VISIBLE);
        }
    }
}
