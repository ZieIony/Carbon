package tk.zielony.carbonsamples;

import android.os.Bundle;

import carbon.component.ComponentItem;
import carbon.component.PaddingItem;

public class MainActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.app_name));

        setItems(new ComponentItem[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                new SampleActivityItem(AnimationsActivity.class, getString(R.string.animationsActivity_title)),
                new SampleActivityItem(ColorsActivity.class, getString(R.string.colorsActivity_title)),
                new SampleActivityItem(ComparisonActivity.class, getString(R.string.comparisonActivity_title)),
                new SampleActivityItem(ComponentsActivity.class, getString(R.string.componentsActivity_title)),
                new SampleActivityItem(DemosActivity.class, getString(R.string.demosActivity_title)),
                new SampleActivityItem(DialogsActivity.class, getString(R.string.dialogsActivity_title)),
                new SampleActivityItem(FeaturesActivity.class, getString(R.string.featuresActivity_title)),
                new SampleActivityItem(GuidelinesActivity.class, getString(R.string.guidelinesActivity_title)),
                new SampleActivityItem(LibrariesActivity.class, getString(R.string.librariesActivity_title)),
                new SampleActivityItem(WidgetsActivity.class, getString(R.string.widgetsActivity_title)),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
