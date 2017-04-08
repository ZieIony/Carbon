package tk.zielony.carbonsamples.applibrary;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import java.util.Arrays;
import java.util.List;

import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

public class RecyclerCardsActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_cards);

        List<ViewModel> items = Arrays.asList(new ViewModel(), new ViewModel(), new ViewModel(), new ViewModel(), new ViewModel(), new ViewModel(), new ViewModel());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerAdapter(items, R.layout.card));

        recyclerView.setHeader(R.layout.header_scrollview);
    }
}
