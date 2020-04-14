package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SampleAnnotation(
        titleId = R.string.favouritesActivity_title,
        layoutId = R.layout.activity_samplelist,
        iconId = R.drawable.ic_star_black_24dp
)
public class FavouritesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        List<Serializable> favourites = new ArrayList<>();
        favourites.add("Tap the star iconId next to a sample to add it to this list");
        for (Map.Entry<String, String> entry : allPreferences.entrySet()) {
            try {
                favourites.add(new SampleActivityItem((Class<? extends Activity>) Class.forName(entry.getKey())));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        setItems(favourites);
    }

}

