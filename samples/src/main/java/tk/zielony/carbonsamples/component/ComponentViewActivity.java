package tk.zielony.carbonsamples.component;

import android.os.Bundle;

import androidx.annotation.Nullable;

import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SampleAnnotation;
import tk.zielony.carbonsamples.ThemedActivity;

@SampleAnnotation(layoutId = R.layout.activity_componentview, titleId = R.string.componentViewActivity_title)
public class ComponentViewActivity extends ThemedActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
    }
}
