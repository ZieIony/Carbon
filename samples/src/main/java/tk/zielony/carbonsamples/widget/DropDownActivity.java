package tk.zielony.carbonsamples.widget;

import tk.zielony.carbonsamples.SamplesActivity;
import android.os.Bundle;

import carbon.widget.DropDown;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-06-10.
 */
public class DropDownActivity extends SamplesActivity {
    private static String[] months = new String[]{
            "Jan", "Feb", "Mar",
            "Apr", "May", "Jun",
            "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dropdown);

        String[] days = new String[31];
        for (int i = 0; i < days.length; i++)
            days[i] = "" + (i + 1);
        DropDown day = (DropDown) findViewById(R.id.day);
        day.setItems(days);

        DropDown month = (DropDown) findViewById(R.id.month);
        month.setItems(months);

        String[] years = new String[30];
        for (int i = 0; i < years.length; i++)
            years[i] = "" + (i + 1987);
        DropDown year = (DropDown) findViewById(R.id.year);
        year.setItems(years);
    }
}
