package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.component.*

@SampleAnnotation(
        titleId = R.string.componentsActivity_title,
        layoutId = R.layout.activity_samplelist,
        iconId = R.drawable.ic_view_compact_black_24dp
)
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
                SampleActivityItem(ComponentViewActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }

}
