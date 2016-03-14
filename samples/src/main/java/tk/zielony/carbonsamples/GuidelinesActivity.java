package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.guidelines.ButtonsUsageActivity;
import tk.zielony.carbonsamples.guidelines.ToolbarsUsageActivity;

/**
 * Created by Marcin on 2015-06-14.
 */
public class GuidelinesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setText(getString(R.string.guidelinesActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(ButtonsUsageActivity.class, getString(R.string.buttonsUsageActivity_title)),
                new ViewModel(ToolbarsUsageActivity.class, getString(R.string.toolbarsUsageActivity_title))
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}

