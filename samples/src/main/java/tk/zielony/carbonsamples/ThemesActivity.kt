package tk.zielony.carbonsamples

import android.os.Bundle
import tk.zielony.carbonsamples.feature.ThemeActivity
import tk.zielony.carbonsamples.themes.CurrentThemeActivity
import tk.zielony.carbonsamples.themes.WindowsThemeActivity

@SampleAnnotation(titleId = R.string.themesActivity_title, layoutId = R.layout.activity_samplelist)
class ThemesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                "What can be done by applying different themes",
                SampleActivityItem(ThemeActivity::class.java),
                SampleActivityItem(CurrentThemeActivity::class.java),
                SampleActivityItem(WindowsThemeActivity::class.java)
        ))
    }

}
