package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import carbon.component.PaddingItem;

@ActivityAnnotation(title = R.string.favouritesActivity_title, layout = R.layout.activity_samplelist)
public class FavouritesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        List<Serializable> favourites = new ArrayList<>();
        favourites.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));
        favourites.add("Tap the star icon next to a sample to add it to this list");
        for (Map.Entry<String, String> entry : allPreferences.entrySet()) {
            try {
                favourites.add(new SampleActivityItem((Class<? extends Activity>) Class.forName(entry.getKey())));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        favourites.add(new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)));

        setItems(favourites);
    }

}

