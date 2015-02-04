package pl.zielony.samples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import pl.zielony.carbon.animation.TransitionLayout;

/**
 * Created by Marcin on 2015-01-24.
 */
public class RadialTransitionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radial_transition);

        final TransitionLayout transitionView = (TransitionLayout) findViewById(R.id.transition);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionView.setHotspot(v);
                transitionView.startTransition(Math.random()>0.5f? TransitionLayout.TransitionType.RadialExpand: TransitionLayout.TransitionType.RadialCollapse);
            }
        });
    }
}
