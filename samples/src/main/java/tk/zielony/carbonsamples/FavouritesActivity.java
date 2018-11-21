package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import carbon.component.PaddingItem;

public class FavouritesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.favouritesActivity_title));

        List<Serializable> favourites = new ArrayList<>();
        favourites.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        favourites.add("Tap the star icon next to a sample to add it to this list");
        for (Map.Entry<String, String> entry : allPreferences.entrySet()) {
            try {
                favourites.add(new SampleActivityItem((Class<? extends Activity>) Class.forName(entry.getKey()), entry.getValue()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        favourites.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        Serializable[] items = new Serializable[favourites.size()];
        favourites.toArray(items);
        setItems(items);
    }

}

