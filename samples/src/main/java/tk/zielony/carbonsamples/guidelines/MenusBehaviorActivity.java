package tk.zielony.carbonsamples.guidelines;

import android.os.Bundle;
import android.support.annotation.Nullable;

import carbon.widget.DropDown;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class MenusBehaviorActivity extends SamplesActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menusbahavior);

        Samples.initToolbar(this, getString(R.string.menusBehaviorActivity_title));

        DropDown<String> dropDown = findViewById(R.id.dropDown);
        dropDown.setItems(new String[]{"NY", "NC", "ND"});
    }
}
