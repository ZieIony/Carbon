package tk.zielony.carbonsamples.widget

import android.os.Bundle
import carbon.component.NavigationHeader
import kotlinx.android.synthetic.main.activity_navigationview.*
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.person.DrawableAvatarGenerator
import tk.zielony.randomdata.person.Gender
import tk.zielony.randomdata.person.StringEmailGenerator
import tk.zielony.randomdata.person.StringNameGenerator

@SampleAnnotation(layoutId = R.layout.activity_navigationview, titleId = R.string.navigationViewActivity_title)
class NavigationViewActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val avatarGenerator = DrawableAvatarGenerator(this)
        val nameGenerator = StringNameGenerator(Gender.Both)
        val emailGenerator = StringEmailGenerator()

        val header = NavigationHeader(this)
        header.setItem(NavigationHeader.Item(avatarGenerator.next(), nameGenerator.next(), emailGenerator.next()))
        drawerMenu.setHeader(header)
    }

    override fun applyTheme() {
        super.applyTheme()
        theme.applyStyle(R.style.Translucent, true)
    }
}
