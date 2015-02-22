package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;

import carbon.widget.Button;

/**
 * Created by Marcin on 2014-12-15.
 */
public class ButtonsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        findViewById(R.id.button).setBackgroundColor(0xff00ff00);

        Button button = (Button) findViewById(R.id.bunny);
        button.setRect(false);
    }
}
