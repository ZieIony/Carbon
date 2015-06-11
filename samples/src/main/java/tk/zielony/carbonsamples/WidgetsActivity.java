package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.widget.ButtonsActivity;
import tk.zielony.carbonsamples.widget.CheckBoxRadioActivity;
import tk.zielony.carbonsamples.widget.CircularProgressActivity;
import tk.zielony.carbonsamples.widget.DialogActivity;
import tk.zielony.carbonsamples.widget.PopupMenuActivity;
import tk.zielony.carbonsamples.widget.ProgressBarsActivity;
import tk.zielony.carbonsamples.widget.RecentsActivity;
import tk.zielony.carbonsamples.widget.RecyclerActivity;
import tk.zielony.carbonsamples.widget.SVGActivity;
import tk.zielony.carbonsamples.widget.ScrollViewActivity;
import tk.zielony.carbonsamples.widget.SnackbarActivity;
import tk.zielony.carbonsamples.widget.TabsActivity;
import tk.zielony.carbonsamples.widget.TextFieldsActivity;

public class WidgetsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setText("Widgets");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(CheckBoxRadioActivity.class, "CheckBoxes & RadioButtons"),
                new ViewModel(ButtonsActivity.class, "Buttons"),
                new ViewModel(DialogActivity.class, "Dialog", true),
                new ViewModel(CircularProgressActivity.class, "Circular progress"),
                new ViewModel(ProgressBarsActivity.class, "Progress bars", true),
                new ViewModel(SnackbarActivity.class, "Snackbar"),
                new ViewModel(SVGActivity.class, "SVG icons"),
                new ViewModel(TextFieldsActivity.class, "Text fields"),
                new ViewModel(TabsActivity.class, "Tabs"),
                new ViewModel(RecentsActivity.class, "Recents"),
                new ViewModel(ScrollViewActivity.class, "ScrollView"),
                new ViewModel(RecyclerActivity.class, "RecyclerView"),
                new ViewModel(PopupMenuActivity.class,"Popup menu")
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
