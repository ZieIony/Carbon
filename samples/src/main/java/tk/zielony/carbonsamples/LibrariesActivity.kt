package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.library.ConstraintLayoutActivity
import tk.zielony.carbonsamples.library.PicassoActivity

@ActivityAnnotation(title = R.string.librariesActivity_title, layout = R.layout.activity_samplelist)
class LibrariesActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "The most popular libraries used with Carbon",
                SampleActivityItem(PicassoActivity::class.java),
                SampleActivityItem(ConstraintLayoutActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
