package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.library.ConstraintLayoutActivity;
import tk.zielony.carbonsamples.library.DesignActivity;
import tk.zielony.carbonsamples.library.PicassoActivity;
import tk.zielony.carbonsamples.library.RecyclerCardsActivity;


public class LibrariesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.librariesActivity_title));

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "The most popular libraries used with Carbon",
                new SampleActivityItem(PicassoActivity.class, getString(R.string.picassoActivity_title)),
                new SampleActivityItem(RecyclerCardsActivity.class, getString(R.string.recyclerCardsActivity_title)),
                new SampleActivityItem(DesignActivity.class, getString(R.string.designActivity_title)),
                new SampleActivityItem(ConstraintLayoutActivity.class, getString(R.string.constraintLayoutActivity_title)),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
