package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.feature.ThemeActivity
import tk.zielony.carbonsamples.themes.CurrentThemeActivity
import tk.zielony.carbonsamples.themes.WindowsThemeActivity

@ActivityAnnotation(title = R.string.themesActivity_title, layout = R.layout.activity_samplelist)
class ThemesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "What can be done by applying different themes",
                SampleActivityItem(ThemeActivity::class.java),
                SampleActivityItem(CurrentThemeActivity::class.java),
                SampleActivityItem(WindowsThemeActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
