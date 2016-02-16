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
import tk.zielony.carbonsamples.widget.FloatingActionMenuActivity;
import tk.zielony.carbonsamples.widget.FlowLayoutActivity;
import tk.zielony.carbonsamples.widget.NavigationViewActivity;
import tk.zielony.carbonsamples.widget.ProgressBarsActivity;
import tk.zielony.carbonsamples.widget.RecentsActivity;
import tk.zielony.carbonsamples.widget.RecyclerActivity;
import tk.zielony.carbonsamples.widget.SVGActivity;
import tk.zielony.carbonsamples.widget.ScrollViewActivity;
import tk.zielony.carbonsamples.widget.SeekBarActivity;
import tk.zielony.carbonsamples.widget.SnackbarActivity;
import tk.zielony.carbonsamples.widget.SpinnerActivity;
import tk.zielony.carbonsamples.widget.TableLayoutActivity;
import tk.zielony.carbonsamples.widget.TabsActivity;
import tk.zielony.carbonsamples.widget.TextFieldsActivity;
import tk.zielony.carbonsamples.widget.ToolbarActivity;

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
                new ViewModel(SpinnerActivity.class, "Spinner"),
                new ViewModel(NavigationViewActivity.class, "NavigationView"),
                new ViewModel(SeekBarActivity.class, "SeekBar"),
                new ViewModel(ToolbarActivity.class, "Toolbar"),
                new ViewModel(FlowLayoutActivity.class, "FlowLayout & Chips"),
                new ViewModel(TableLayoutActivity.class, "TableLayout"),
                new ViewModel(FloatingActionMenuActivity.class, "FloatingActionMenu")
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
