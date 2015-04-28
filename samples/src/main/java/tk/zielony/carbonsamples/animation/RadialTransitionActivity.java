package tk.zielony.carbonsamples.animation;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import carbon.beta.TransitionLayout;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-01-24.
 */
public class RadialTransitionActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radial_transition);

        getSupportFragmentManager().beginTransaction().add(R.id.container, new SimpleFragment()).commit();

        final TransitionLayout transitionView = (TransitionLayout) findViewById(R.id.transition);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionView.setHotspot(v);
                transitionView.startTransition((transitionView.getCurrentChild() + 1) % transitionView.getChildCount(), TransitionLayout.TransitionType.Radial, TransitionLayout.DEFAULT_DURATION, Math.random() > 0.5f);
            }
        });
    }
}
