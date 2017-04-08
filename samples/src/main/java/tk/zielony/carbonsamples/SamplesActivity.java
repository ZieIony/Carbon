package tk.zielony.carbonsamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class SamplesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Samples.applyTheme(this);
        super.onCreate(savedInstanceState);
    }

}
