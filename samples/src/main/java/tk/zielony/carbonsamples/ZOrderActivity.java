package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import carbon.shadow.ShadowView;
import carbon.widget.Button;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ZOrderActivity extends Activity {
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zorder);

        final FrameLayout layout = (FrameLayout) findViewById(R.id.layout);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ShadowView) layout.getChildAt(0)).setElevation(getResources().getDimension(R.dimen.dip)*(flag ? 2:3));
                ((ShadowView) layout.getChildAt(1)).setElevation(getResources().getDimension(R.dimen.dip)*(flag ? 3:2));
                flag = !flag;
                layout.invalidate();
            }
        });
    }
}
