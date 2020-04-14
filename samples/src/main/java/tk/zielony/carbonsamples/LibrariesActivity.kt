package tk.zielony.carbonsamples

import android.os.Bundle
import tk.zielony.carbonsamples.library.ConstraintLayoutActivity
import tk.zielony.carbonsamples.library.PicassoActivity

@SampleAnnotation(titleId = R.string.librariesActivity_title, layoutId = R.layout.activity_samplelist)
class LibrariesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                "The most popular libraries used with Carbon",
                SampleActivityItem(PicassoActivity::class.java),
                SampleActivityItem(ConstraintLayoutActivity::class.java)
        ))
    }

}
