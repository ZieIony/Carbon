package tk.zielony.carbonsamples.animation;

import android.app.Activity;
import android.os.Bundle;

import carbon.drawable.ripple.LollipopDrawablesCompat;
import carbon.drawable.ripple.RippleDrawableFroyo;
import carbon.widget.FrameLayout;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class RippleActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ripple);

        RippleDrawableFroyo ripple = (RippleDrawableFroyo) LollipopDrawablesCompat.getDrawable(getResources(), R.drawable.ripple);
        FrameLayout layout = (FrameLayout) findViewById(R.id.layout);
        layout.setRippleDrawable(ripple);
    }
}
