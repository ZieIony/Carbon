package tk.zielony.carbonsamples.widget;

import android.os.Bundle;

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
        dropDown.setOnSelectionChangedListener((item, position) -> bottomSheet.setStyle(item.equals("List") ? BottomSheetLayout.Style.List : BottomSheetLayout.Style.Grid));
    }
}
