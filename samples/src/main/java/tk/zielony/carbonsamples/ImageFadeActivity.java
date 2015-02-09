package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Marcin on 2015-01-22.
 */
public class ImageFadeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagefade);

        final View cow = findViewById(R.id.mazda);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cow.setVisibility(cow.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
    }
}
