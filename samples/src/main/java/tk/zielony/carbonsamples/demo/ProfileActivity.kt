package tk.zielony.carbonsamples.demo

import android.os.Bundle
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@ActivityAnnotation(layout = R.layout.activity_profile, title = R.string.profileActivity_title)
class ProfileActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
    }

    override fun applyTheme() {
        super.applyTheme()
        theme.applyStyle(R.style.Translucent, true)
    }
}
