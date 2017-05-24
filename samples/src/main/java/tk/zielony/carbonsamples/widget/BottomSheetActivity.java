package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import carbon.beta.BottomSheetLayout;
import carbon.widget.DropDown;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class BottomSheetActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottomsheet);

        Samples.initToolbar(this, getString(R.string.bottomSheetActivity_title));

        BottomSheetLayout bottomSheet = (BottomSheetLayout) findViewById(R.id.bottomSheet);
        bottomSheet.setStyle(BottomSheetLayout.Style.List);
        bottomSheet.setTitle("Menu");
        bottomSheet.setMenu(R.menu.menu_navigation);

        DropDown<String> dropDown = (DropDown<String>) findViewById(R.id.dropDown);
        String[] items = {"List", "Grid"};
        dropDown.setItems(items);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bottomSheet.setStyle(items[position].equals("List") ? BottomSheetLayout.Style.List : BottomSheetLayout.Style.Grid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
