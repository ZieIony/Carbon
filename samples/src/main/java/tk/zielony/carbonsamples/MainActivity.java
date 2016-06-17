package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import carbon.Carbon;
import carbon.internal.DebugOverlay;
import carbon.widget.ImageView;
import carbon.widget.RecyclerView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Samples.initToolbar(this,getString(R.string.app_name));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(WidgetsActivity.class, getString(R.string.widgetsActivity_title)),
                new ViewModel(FeaturesActivity.class, getString(R.string.featuresActivity_title)),
                new ViewModel(AnimationsActivity.class, getString(R.string.animationsActivity_title)),
                new ViewModel(AppsLibrariesActivity.class, getString(R.string.appsLibrariesActivity_title)),
                new ViewModel(DemosActivity.class, getString(R.string.demosActivity_title)),
                new ViewModel(GuidelinesActivity.class, getString(R.string.guidelinesActivity_title))
        };
        recyclerView.setLayoutManager(getResources().getBoolean(R.bool.tablet) ?
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false) :
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
