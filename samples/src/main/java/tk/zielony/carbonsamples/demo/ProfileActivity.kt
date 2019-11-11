package tk.zielony.carbonsamples.demo

import android.os.Bundle
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(
        layoutId = R.layout.activity_profile,
        titleId = R.string.profileActivity_title,
        iconId = R.drawable.ic_person_black_24dp
)
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
