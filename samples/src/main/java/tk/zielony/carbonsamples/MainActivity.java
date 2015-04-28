package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setText(R.string.app_name);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbar.findViewById(R.id.icon1).setVisibility(View.GONE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.findViewById(R.id.icon2).setVisibility(View.GONE);
                    }
                }, 100);
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(WidgetsActivity.class, "Widgets"),
                new ViewModel(FeaturesActivity.class, "Features"),
                new ViewModel(AnimationsActivity.class, "Animations"),
                new ViewModel(AppsLibrariesActivity.class, "Apps & Libraries"),
                new ViewModel(DemosActivity.class, "Demos")
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
