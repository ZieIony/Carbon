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
import tk.zielony.carbonsamples.widget.DropDownActivity;
import tk.zielony.carbonsamples.widget.ExpandableRecyclerActivity;
import tk.zielony.carbonsamples.widget.ExpansionPanelActivity;
import tk.zielony.carbonsamples.widget.FloatingActionMenuActivity;
import tk.zielony.carbonsamples.widget.FlowLayoutActivity;
import tk.zielony.carbonsamples.widget.NavigationViewActivity;
import tk.zielony.carbonsamples.widget.ProgressBarsActivity;
import tk.zielony.carbonsamples.widget.RecyclerActivity;
import tk.zielony.carbonsamples.widget.SeekBarActivity;
import tk.zielony.carbonsamples.widget.SnackbarActivity;
import tk.zielony.carbonsamples.widget.TableLayoutActivity;
import tk.zielony.carbonsamples.widget.TabsActivity;
import tk.zielony.carbonsamples.widget.TextFieldsActivity;

@ActivityAnnotation(title = R.string.widgetsActivity_title)
public class WidgetsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Samples.initToolbar(this);

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Widgets with sample styles, data and applications",
                new SampleActivityItem(BannerActivity.class),
                new SampleActivityItem(CheckBoxRadioActivity.class),
                new SampleActivityItem(ButtonsActivity.class),
                new SampleActivityItem(CircularProgressActivity.class),
                new SampleActivityItem(ProgressBarsActivity.class),
                new SampleActivityItem(SnackbarActivity.class),
                new SampleActivityItem(PercentLayoutActivity.class),
                new SampleActivityItem(TextFieldsActivity.class),
                new SampleActivityItem(TabsActivity.class),
                new SampleActivityItem(RecyclerActivity.class),
                new SampleActivityItem(ExpandableRecyclerActivity.class),
                new SampleActivityItem(ExpansionPanelActivity.class),
                new SampleActivityItem(DropDownActivity.class),
                new SampleActivityItem(NavigationViewActivity.class),
                new SampleActivityItem(SeekBarActivity.class),
                new SampleActivityItem(FlowLayoutActivity.class),
                new SampleActivityItem(TableLayoutActivity.class),
                new SampleActivityItem(FloatingActionMenuActivity.class),
                new SampleActivityItem(BottomBarActivity.class, true),
                new SampleActivityItem(BottomSheetActivity.class, true),
                new SampleActivityItem(BackdropActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
