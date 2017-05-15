package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class ButtonsActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        Samples.initToolbar(this, getString(R.string.buttonsActivity_title));

        findViewById(R.id.code).setOnClickListener(v -> {
            Samples.showCodeDialog(this, "<carbon.widget.Button\n" +
                    "   app:carbon_cornerRadius=\"100dp\"\n" +
                    "   app:carbon_rippleColor=\"#40ff0000\"\n" +
                    "   app:carbon_stroke=\"#7f0c001a\"\n" +
                    "   app:carbon_strokeWidth=\"2dp\"/>\n");
        });
    }
}
