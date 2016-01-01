package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.applibrary.CalculatorActivity;
import tk.zielony.carbonsamples.applibrary.DesignActivity;
import tk.zielony.carbonsamples.applibrary.DrawerActivity;
import tk.zielony.carbonsamples.applibrary.PicassoActivity;
import tk.zielony.carbonsamples.applibrary.RecyclerCardsActivity;


public class AppsLibrariesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setText("Apps & Libraries");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(CalculatorActivity.class, "Calculator", true),
                new ViewModel(PicassoActivity.class, "Picasso"),
                new ViewModel(RecyclerCardsActivity.class, "RecyclerView & cards"),
                new ViewModel(DrawerActivity.class, "Nawigation drawer"),
                new ViewModel(DesignActivity.class, "Design Support Library")
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
