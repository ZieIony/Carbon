package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import carbon.Carbon;
import carbon.internal.DebugOverlay;
import carbon.widget.ImageView;
import carbon.widget.RecyclerView;
import carbon.widget.Toolbar;
import tk.zielony.carbonsamples.widget.ButtonsActivity;
import tk.zielony.carbonsamples.widget.CheckBoxRadioActivity;
import tk.zielony.carbonsamples.widget.CircularProgressActivity;
import tk.zielony.carbonsamples.widget.ExpandableRecyclerActivity;
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
    public boolean debugEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final DebugOverlay overlay = new DebugOverlay(this);

        final ImageView debug = (ImageView) findViewById(R.id.debug);
        debug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!debugEnabled) {
                    debug.setTint(Carbon.getThemeColor(WidgetsActivity.this, R.attr.carbon_iconColor));
                    overlay.show();
                    debugEnabled = true;
                } else {
                    debug.setTint(Carbon.getThemeColor(WidgetsActivity.this, R.attr.colorControlNormal));
                    overlay.dismiss();
                    debugEnabled = false;
                }
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setIconVisible(true);
        toolbar.setTitle(getString(R.string.widgetsActivity_title));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        ViewModel[] items = new ViewModel[]{
                new ViewModel(CheckBoxRadioActivity.class, getString(R.string.checkBoxRadioActivity_title)),
                new ViewModel(ButtonsActivity.class, getString(R.string.buttonsActivity_title)),
                new ViewModel(CircularProgressActivity.class, getString(R.string.circularProgressActivity_title)),
                new ViewModel(ProgressBarsActivity.class, getString(R.string.progressBarsActivity_title)),
                new ViewModel(SnackbarActivity.class, getString(R.string.snackbarActivity_title)),
                new ViewModel(SVGActivity.class, getString(R.string.svgActivity_title)),
                new ViewModel(TextFieldsActivity.class, getString(R.string.textFieldsActivity_title)),
                new ViewModel(TabsActivity.class, getString(R.string.tabsActivity_title)),
                new ViewModel(RecentsActivity.class, getString(R.string.recentsActivity_title)),
                new ViewModel(ScrollViewActivity.class, getString(R.string.scrollViewActivity_title)),
                new ViewModel(RecyclerActivity.class, getString(R.string.recyclerViewActivity_title)),
                new ViewModel(ExpandableRecyclerActivity.class, getString(R.string.expandableRecyclerActivity_title)),
                new ViewModel(SpinnerActivity.class, getString(R.string.spinnerActivityActivity_title)),
                new ViewModel(NavigationViewActivity.class, getString(R.string.navigationViewActivity_title)),
                new ViewModel(SeekBarActivity.class, getString(R.string.seekBarActivity_title)),
                new ViewModel(ToolbarActivity.class, getString(R.string.toolbarActivity_title)),
                new ViewModel(FlowLayoutActivity.class, getString(R.string.flowLayoutActivity_title)),
                new ViewModel(TableLayoutActivity.class, getString(R.string.tableLayoutActivity_title)),
                new ViewModel(FloatingActionMenuActivity.class, getString(R.string.floatingActionMenuActivity_title))
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new MainListAdapter(items));
    }

}
