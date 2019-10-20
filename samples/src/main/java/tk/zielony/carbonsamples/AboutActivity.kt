package tk.zielony.carbonsamples

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_about.*

@ActivityAnnotation(layout = R.layout.activity_about, title = R.string.aboutActivity_title)
class AboutActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        website.setOnClickListener {
            val url = "https://github.com/ZieIony/Carbon"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        issue.setOnClickListener {
            val url = "https://github.com/ZieIony/Carbon/issues"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        version.text = "Carbon version: ${carbon.BuildConfig.VERSION_NAME}"
    }
}
