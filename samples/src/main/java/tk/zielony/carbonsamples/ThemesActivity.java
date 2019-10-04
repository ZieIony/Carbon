package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.feature.ThemeActivity;
import tk.zielony.carbonsamples.themes.CurrentThemeActivity;
import tk.zielony.carbonsamples.themes.WindowsThemeActivity;

@ActivityAnnotation(title = R.string.themesActivity_title, layout = R.layout.activity_samplelist)
public class ThemesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Widgets with sample styles, data and applications",
                new SampleActivityItem(ThemeActivity.class),
                new SampleActivityItem(CurrentThemeActivity.class),
                new SampleActivityItem(WindowsThemeActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
