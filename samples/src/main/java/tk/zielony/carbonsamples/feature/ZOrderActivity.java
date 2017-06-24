package tk.zielony.carbonsamples.feature;

import android.os.Bundle;
import android.widget.FrameLayout;

import carbon.shadow.ShadowView;
import carbon.widget.Button;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class ZOrderActivity extends SamplesActivity {
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zorder);

        Samples.initToolbar(this, getString(R.string.zOrderActivity_title));

        final FrameLayout layout = findViewById(R.id.layout);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            ((ShadowView) layout.getChildAt(0)).setElevation(getResources().getDimension(R.dimen.carbon_1dip) * (flag ? 2 : 3));
            ((ShadowView) layout.getChildAt(1)).setElevation(getResources().getDimension(R.dimen.carbon_1dip) * (flag ? 3 : 2));
            flag = !flag;
            layout.postInvalidate();
        });
    }
}
