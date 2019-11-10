package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.widget.BackdropActivity;
import tk.zielony.carbonsamples.widget.BannerActivity;
import tk.zielony.carbonsamples.widget.BottomNavigationViewActivity;
import tk.zielony.carbonsamples.widget.BottomSheetActivity;
import tk.zielony.carbonsamples.widget.ButtonsActivity;
import tk.zielony.carbonsamples.widget.CheckBoxRadioActivity;
import tk.zielony.carbonsamples.widget.CircularProgressActivity;
import tk.zielony.carbonsamples.widget.DropDownActivity;
import tk.zielony.carbonsamples.widget.ExpandableRecyclerActivity;
import tk.zielony.carbonsamples.widget.ExpansionPanelActivity;
import tk.zielony.carbonsamples.widget.FABActivity;
import tk.zielony.carbonsamples.widget.FlowLayoutActivity;
import tk.zielony.carbonsamples.widget.NavigationViewActivity;
import tk.zielony.carbonsamples.widget.MenusActivity;
import tk.zielony.carbonsamples.widget.ProgressBarsActivity;
import tk.zielony.carbonsamples.widget.RecyclerActivity;
import tk.zielony.carbonsamples.widget.SeekBarActivity;
import tk.zielony.carbonsamples.widget.SnackbarActivity;
import tk.zielony.carbonsamples.widget.TableLayoutActivity;
import tk.zielony.carbonsamples.widget.TabsActivity;
import tk.zielony.carbonsamples.widget.TextFieldsActivity;

@ActivityAnnotation(title = R.string.widgetsActivity_title, layout = R.layout.activity_samplelist)
public class WidgetsActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Widgets with sample styles, data and applications",
                new SampleActivityItem(BannerActivity.class),
                new SampleActivityItem(CheckBoxRadioActivity.class, R.drawable.carbon_checkbox_checked),
                new SampleActivityItem(ButtonsActivity.class),
                new SampleActivityItem(FABActivity.class, R.drawable.ic_add_circle_black_24dp),
                new SampleActivityItem(CircularProgressActivity.class),
                new SampleActivityItem(MenusActivity.class, R.drawable.ic_menu_black_24dp),
                new SampleActivityItem(ProgressBarsActivity.class),
                new SampleActivityItem(SnackbarActivity.class),
                new SampleActivityItem(TextFieldsActivity.class, R.drawable.ic_text_fields_black_24dp),
                new SampleActivityItem(TabsActivity.class),
                new SampleActivityItem(RecyclerActivity.class, R.drawable.ic_view_stream_black_24dp),
                new SampleActivityItem(ExpandableRecyclerActivity.class),
                new SampleActivityItem(ExpansionPanelActivity.class),
                new SampleActivityItem(DropDownActivity.class, R.drawable.carbon_dropdown),
                new SampleActivityItem(NavigationViewActivity.class, 0, true),
                new SampleActivityItem(SeekBarActivity.class),
                new SampleActivityItem(FlowLayoutActivity.class),
                new SampleActivityItem(TableLayoutActivity.class),
                new SampleActivityItem(BottomNavigationViewActivity.class),
                new SampleActivityItem(BottomSheetActivity.class, 0, true),
                new SampleActivityItem(BackdropActivity.class),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}
