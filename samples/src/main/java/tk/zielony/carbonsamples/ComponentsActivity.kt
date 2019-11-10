package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.component.*

@ActivityAnnotation(title = R.string.componentsActivity_title, layout = R.layout.activity_samplelist)
class ComponentsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Lists and forms composed of reusable components with data binding",
                SampleActivityItem(IconTextListItemActivity::class.java),
                SampleActivityItem(AvatarTextListItemActivity::class.java),
                SampleActivityItem(ImageTextSubtextDateListItemActivity::class.java),
                SampleActivityItem(AvatarTextRatingSubtextDateListItemActivity::class.java),
                SampleActivityItem(RegisterActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
