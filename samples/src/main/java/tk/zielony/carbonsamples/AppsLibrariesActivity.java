package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import carbon.Carbon;
import carbon.internal.DebugOverlay;
import carbon.widget.ImageView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.applibrary.CalculatorActivity;
import tk.zielony.carbonsamples.applibrary.DesignActivity;
import tk.zielony.carbonsamples.applibrary.DrawerActivity;
import tk.zielony.carbonsamples.applibrary.PicassoActivity;
import tk.zielony.carbonsamples.applibrary.RecyclerCardsActivity;


public class AppsLibrariesActivity extends Activity {

    private boolean debugEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final DebugOverlay overlay = new DebugOverlay(this);

        final ImageView debug = (ImageView) findViewById(R.id.debug);
        debug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!debugEnabled) {
                    debug.setTint(Carbon.getThemeColor(AppsLibrariesActivity.this, R.attr.carbon_iconColor));
                    overlay.show();
                    debugEnabled = true;
                } else {
                    debug.setTint(Carbon.getThemeColor(AppsLibrariesActivity.this, R.attr.colorControlNormal));
                    overlay.dismiss();
                    debugEnabled = false;
                }
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setTitle(getString(R.string.appsLibrariesActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(CalculatorActivity.class, getString(R.string.calculatorActivity_title), true),
                new ViewModel(PicassoActivity.class, getString(R.string.picassoActivity_title)),
                new ViewModel(RecyclerCardsActivity.class, getString(R.string.recyclerCardsActivity_title)),
                new ViewModel(DrawerActivity.class, getString(R.string.drawerActivity_title)),
                new ViewModel(DesignActivity.class, getString(R.string.designActivity_title))
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
