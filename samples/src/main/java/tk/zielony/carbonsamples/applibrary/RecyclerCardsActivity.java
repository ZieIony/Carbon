package tk.zielony.carbonsamples.applibrary;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.Arrays;

import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;
import tk.zielony.randomdata.Generator;
import tk.zielony.randomdata.RandomData;
import tk.zielony.randomdata.common.DrawableImageGenerator;
import tk.zielony.randomdata.common.StringDateGenerator;

public class RecyclerCardsActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_cards);

        Samples.initToolbar(this, getString(R.string.recyclerCardsActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RandomData randomData = new RandomData();
        randomData.addGenerators(new Generator[]{
                new StringDateGenerator(),
                new DrawableImageGenerator(this)
        });
        ViewModel[] items = new ViewModel[5];
        randomData.fillAsync(items, () -> recyclerView.setAdapter(new RecyclerAdapter(Arrays.asList(items), R.layout.card)));
    }
}
