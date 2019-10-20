package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

import carbon.widget.DropDown;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(layout = R.layout.activity_dropdown, title = R.string.dropDownActivityActivity_title)
public class DropDownActivity extends ThemedActivity {
    private static String[] months = new String[]{
            "Jan", "Feb", "Mar",
            "Apr", "May", "Jun",
            "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        String[] days = new String[31];
        for (int i = 0; i < days.length; i++)
            days[i] = "" + (i + 1);
        DropDown day = findViewById(R.id.day);
        day.setItems(days);

        DropDown month = findViewById(R.id.month);
        month.setItems(months);

        String[] years = new String[30];
        for (int i = 0; i < years.length; i++)
            years[i] = "" + (i + 1987);
        DropDown year = findViewById(R.id.year);
        year.setItems(years);
    }
}
