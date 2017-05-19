package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.comparison.CardViewComparisonActivity;
import tk.zielony.carbonsamples.comparison.TextViewComparisonActivity;


public class ComparisonActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.comparisonActivity_title));

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Carbon fixes some of Android's issues and provides widgets and features which behave consistently across all supported versions of Android",
                new SampleActivityItem(TextViewComparisonActivity.class, getString(R.string.textViewComparisonActivity_title)),
                new SampleActivityItem(CardViewComparisonActivity.class, getString(R.string.cardViewComparisonActivity_title)),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
