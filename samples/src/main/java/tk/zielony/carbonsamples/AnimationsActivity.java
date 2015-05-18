package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.animation.ImageFadeActivity;
import tk.zielony.carbonsamples.animation.ListRippleActivity;
import tk.zielony.carbonsamples.animation.RadialTransitionActivity;
import tk.zielony.carbonsamples.animation.RippleActivity;
import tk.zielony.carbonsamples.animation.RippleComparisonActivity;


public class AnimationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setText("Animations");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(tk.zielony.carbonsamples.animation.AnimationsActivity.class, "Widget animations"),
                new ViewModel(ImageFadeActivity.class, "Image fade"),
                new ViewModel(ListRippleActivity.class, "List ripple"),
                new ViewModel(RippleComparisonActivity.class, "Ripple comparison", false, true),
                new ViewModel(RadialTransitionActivity.class, "Radial transition"),
                new ViewModel(RippleActivity.class, "Touch ripple")
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
