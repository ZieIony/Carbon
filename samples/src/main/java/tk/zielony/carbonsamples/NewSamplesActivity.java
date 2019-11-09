package tk.zielony.carbonsamples;

import android.os.Bundle;

import java.io.Serializable;

import carbon.component.PaddingItem;
import tk.zielony.carbonsamples.demo.MusicPlayerActivity;
import tk.zielony.carbonsamples.demo.ProfileActivity;
import tk.zielony.carbonsamples.demo.SearchToolbarActivity;
import tk.zielony.carbonsamples.feature.GestureDetectorActivity;
import tk.zielony.carbonsamples.feature.ShadowActivity;
import tk.zielony.carbonsamples.feature.XmlFontActivity;
import tk.zielony.carbonsamples.graph.BarChartActivity;
import tk.zielony.carbonsamples.graph.LineChartActivity;
import tk.zielony.carbonsamples.widget.BackdropActivity;
import tk.zielony.carbonsamples.widget.BannerActivity;
import tk.zielony.carbonsamples.widget.BottomNavigationViewActivity;
import tk.zielony.carbonsamples.widget.ButtonsActivity;
import tk.zielony.carbonsamples.widget.CheckBoxRadioActivity;
import tk.zielony.carbonsamples.widget.FABActivity;
import tk.zielony.carbonsamples.widget.FlowLayoutActivity;
import tk.zielony.carbonsamples.widget.NavigationViewActivity;
import tk.zielony.carbonsamples.widget.MenusActivity;

@ActivityAnnotation(title = R.string.newSamplesActivity_title, layout = R.layout.activity_samplelist)
public class NewSamplesActivity extends SampleListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        setItems(new Serializable[]{
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "These samples are new or have significant changes since the previous release",
                new SampleActivityItem(ButtonsActivity.class),
                new SampleActivityItem(FlowLayoutActivity.class),
                new SampleActivityItem(ShadowActivity.class),
                new SampleActivityItem(BottomNavigationViewActivity.class),
                new SampleActivityItem(XmlFontActivity.class, R.drawable.ic_font_download_black_24dp),
                new SampleActivityItem(BackdropActivity.class),
                new SampleActivityItem(MenusActivity.class, R.drawable.ic_menu_black_24dp),
                new SampleActivityItem(BarChartActivity.class, 0, true),
                new SampleActivityItem(LineChartActivity.class, 0, true),
                new SampleActivityItem(SearchToolbarActivity.class, R.drawable.carbon_search),
                new SampleActivityItem(MusicPlayerActivity.class, R.drawable.ic_play_arrow_black_24dp, true),
                new SampleActivityItem(ProfileActivity.class, R.drawable.ic_person_black_24dp),
                new SampleActivityItem(CheckBoxRadioActivity.class, R.drawable.carbon_checkbox_checked),
                new SampleActivityItem(GestureDetectorActivity.class, R.drawable.ic_gesture_black_24dp),
                new SampleActivityItem(FABActivity.class, R.drawable.ic_add_circle_black_24dp),
                new SampleActivityItem(BannerActivity.class),
                new SampleActivityItem(NavigationViewActivity.class, 0, true),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        });
    }

}

