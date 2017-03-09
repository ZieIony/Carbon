package tk.zielony.carbonsamples;

import tk.zielony.carbonsamples.SamplesActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import tk.zielony.carbonsamples.applibrary.CalculatorActivity;
import tk.zielony.carbonsamples.applibrary.ConstraintLayoutActivity;
import tk.zielony.carbonsamples.applibrary.DesignActivity;
import tk.zielony.carbonsamples.applibrary.DrawerActivity;
import tk.zielony.carbonsamples.applibrary.PicassoActivity;
import tk.zielony.carbonsamples.applibrary.RecyclerCardsActivity;


public class AppsLibrariesActivity extends SamplesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        Samples.initToolbar(this, getString(R.string.appsLibrariesActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(CalculatorActivity.class, getString(R.string.calculatorActivity_title), true),
                new ViewModel(PicassoActivity.class, getString(R.string.picassoActivity_title)),
                new ViewModel(RecyclerCardsActivity.class, getString(R.string.recyclerCardsActivity_title)),
                new ViewModel(DrawerActivity.class, getString(R.string.drawerActivity_title)),
                new ViewModel(DesignActivity.class, getString(R.string.designActivity_title)),
                new ViewModel(ConstraintLayoutActivity.class, getString(R.string.constraintLayoutActivity_title))
        };
        recyclerView.setLayoutManager(getResources().getBoolean(R.bool.tablet) ?
                new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false) :
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
