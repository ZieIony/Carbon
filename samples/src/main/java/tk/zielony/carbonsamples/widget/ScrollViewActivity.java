package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import carbon.widget.ScrollView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

public class ScrollViewActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        scrollView.setHeader(R.layout.header_scrollview);
    }
}
