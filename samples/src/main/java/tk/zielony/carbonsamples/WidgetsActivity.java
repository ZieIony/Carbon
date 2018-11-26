package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.feature.PercentLayoutActivity;
import tk.zielony.carbonsamples.widget.BackdropActivity;
import tk.zielony.carbonsamples.widget.BannerActivity;
import tk.zielony.carbonsamples.widget.BottomBarActivity;
import tk.zielony.carbonsamples.widget.BottomSheetActivity;
import tk.zielony.carbonsamples.widget.ButtonsActivity;
import tk.zielony.carbonsamples.widget.CheckBoxRadioActivity;
import tk.zielony.carbonsamples.widget.CircularProgressActivity;
import tk.zielony.carbonsamples.widget.ExpansionPanelActivity;
import tk.zielony.carbonsamples.widget.NavigationViewActivity;
import tk.zielony.carbonsamples.widget.DropDownActivity;
import tk.zielony.carbonsamples.widget.ExpandableRecyclerActivity;
import tk.zielony.carbonsamples.widget.FloatingActionMenuActivity;
import tk.zielony.carbonsamples.widget.FlowLayoutActivity;
import tk.zielony.carbonsamples.widget.ProgressBarsActivity;
import tk.zielony.carbonsamples.widget.RecyclerActivity;
import tk.zielony.carbonsamples.widget.SeekBarActivity;
import tk.zielony.carbonsamples.widget.SnackbarActivity;
import tk.zielony.carbonsamples.widget.TableLayoutActivity;
import tk.zielony.carbonsamples.widget.TabsActivity;
import tk.zielony.carbonsamples.widget.TextFieldsActivity;

public class WidgetsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this, getString(R.string.widgetsActivity_title));

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Widgets with sample styles, data and applications",
                new SampleActivityItem(BannerActivity.class, getString(R.string.bannerActivity_title)),
                new SampleActivityItem(CheckBoxRadioActivity.class, getString(R.string.checkBoxRadioActivity_title)),
                new SampleActivityItem(ButtonsActivity.class, getString(R.string.buttonsActivity_title)),
                new SampleActivityItem(CircularProgressActivity.class, getString(R.string.circularProgressActivity_title)),
                new SampleActivityItem(ProgressBarsActivity.class, getString(R.string.progressBarsActivity_title)),
                new SampleActivityItem(SnackbarActivity.class, getString(R.string.snackbarActivity_title)),
                new SampleActivityItem(PercentLayoutActivity.class, getString(R.string.percentLayoutActivity_title)),
                new SampleActivityItem(TextFieldsActivity.class, getString(R.string.textFieldsActivity_title)),
                new SampleActivityItem(TabsActivity.class, getString(R.string.tabsActivity_title)),
                new SampleActivityItem(RecyclerActivity.class, getString(R.string.recyclerViewActivity_title)),
                new SampleActivityItem(ExpandableRecyclerActivity.class, getString(R.string.expandableRecyclerActivity_title)),
                new SampleActivityItem(ExpansionPanelActivity.class, getString(R.string.expansionPanelActivity_title)),
                new SampleActivityItem(DropDownActivity.class, getString(R.string.dropDownActivityActivity_title)),
                new SampleActivityItem(NavigationViewActivity.class, getString(R.string.navigationViewActivity_title)),
                new SampleActivityItem(SeekBarActivity.class, getString(R.string.seekBarActivity_title)),
                new SampleActivityItem(FlowLayoutActivity.class, getString(R.string.flowLayoutActivity_title)),
                new SampleActivityItem(TableLayoutActivity.class, getString(R.string.tableLayoutActivity_title)),
                new SampleActivityItem(FloatingActionMenuActivity.class, getString(R.string.floatingActionMenuActivity_title)),
                new SampleActivityItem(BottomBarActivity.class, getString(R.string.bottomBarActivity_title), true),
                new SampleActivityItem(BottomSheetActivity.class, getString(R.string.bottomSheetActivity_title), true),
                new SampleActivityItem(BackdropActivity.class, getString(R.string.backdropActivity_title)),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
