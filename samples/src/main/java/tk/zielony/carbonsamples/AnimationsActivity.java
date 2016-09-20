package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import tk.zielony.carbonsamples.animation.ImageFadeActivity;
import tk.zielony.carbonsamples.animation.ListRippleActivity;
import tk.zielony.carbonsamples.animation.RadialTransitionActivity;
import tk.zielony.carbonsamples.animation.RippleActivity;
import tk.zielony.carbonsamples.animation.RippleComparisonActivity;
import tk.zielony.carbonsamples.animation.WidgetAnimationsActivity;


public class AnimationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        Samples.initToolbar(this, getString(R.string.animationsActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(WidgetAnimationsActivity.class, getString(R.string.widgetAnimationsActivity_title)),
                new ViewModel(ImageFadeActivity.class, getString(R.string.imageFadeActivity_title)),
                new ViewModel(ListRippleActivity.class, getString(R.string.listRippleActivity_title)),
                new ViewModel(RippleComparisonActivity.class, getString(R.string.rippleComparisonActivity_title), false, true),
                new ViewModel(RadialTransitionActivity.class, getString(R.string.radialTransitionActivity_title)),
                new ViewModel(RippleActivity.class, getString(R.string.rippleActivity_title))
        };
        recyclerView.setLayoutManager(getResources().getBoolean(R.bool.tablet) ?
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false) :
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
