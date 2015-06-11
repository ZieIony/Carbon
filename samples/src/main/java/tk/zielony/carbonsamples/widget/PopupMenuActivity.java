package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;

import carbon.widget.Spinner;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.applibrary.FruitAdapter;

/**
 * Created by Marcin on 2015-06-10.
 */
public class PopupMenuActivity extends Activity {
    private static String[] fruits = new String[]{
            "Lime", "Lemon", "Orange", "Strawberry", "Blueberry", "Plum"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popupmenu);

        Spinner spinner = (Spinner) findViewById(R.id.button1);
        spinner.setAdapter(new FruitAdapter(fruits));
    }
}
