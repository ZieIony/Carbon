package tk.zielony.carbonsamples.feature;

import android.os.Bundle;
import android.util.TypedValue;

import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class AutoSizeTextActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autosizetext);

        Samples.initToolbar(this, getString(R.string.autoSizeTextActivity_title));

        TextView autoSizeText = findViewById(R.id.autoSizeText);
        TextView textSize = findViewById(R.id.textSize);
        autoSizeText.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            textSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, autoSizeText.getTextSize());
            textSize.setText(autoSizeText.getTextSize() / getResources().getDisplayMetrics().scaledDensity + "sp");
        });
    }
}
