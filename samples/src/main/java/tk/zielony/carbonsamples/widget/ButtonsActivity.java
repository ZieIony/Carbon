package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(layout = R.layout.activity_buttons, title = R.string.buttonsActivity_title)
public class ButtonsActivity extends ThemedActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);
    }
}

