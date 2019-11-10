package tk.zielony.carbonsamples.widget

import android.os.Bundle
import carbon.component.*
import carbon.recycler.RowFactory
import carbon.recycler.RowListAdapter
import carbon.widget.BackdropLayout
import kotlinx.android.synthetic.main.activity_backdrop.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.IntegerGenerator
import tk.zielony.randomdata.common.StringDateGenerator
import tk.zielony.randomdata.common.TextGenerator
import tk.zielony.randomdata.person.DrawableAvatarGenerator
import tk.zielony.randomdata.person.StringNameGenerator
import java.io.Serializable

@ActivityAnnotation(layout = R.layout.activity_backdrop, title = R.string.backdropActivity_title)
class BackdropActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        run {
            val items = listOf(
                    DefaultIconTextItem(resources.getDrawable(R.drawable.ic_android_black_24dp), "Android"),
                    DefaultIconTextItem(resources.getDrawable(R.drawable.ic_person_black_24dp), "Person"),
                    DefaultIconTextItem(resources.getDrawable(R.drawable.ic_email_black_24dp), "Email"),
                    DefaultIconTextItem(resources.getDrawable(R.drawable.ic_comment_black_24dp), "Comment")
            )
            backdrop_recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            backdrop_recycler.adapter = RowListAdapter(items, RowFactory<IconTextItem> { parent -> IconTextRow(parent) }).apply {
                setOnItemClickedListener { view, type, position -> backdrop_backdropLayout.closeLayout() }
            }

            backdrop_menuIcon.setOnClickListener { backdrop_backdropLayout.toggleLayout(BackdropLayout.Side.START) }
        }

        run {
            val items = listOf(
                    PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                    DefaultHeaderItem("Header"),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultHeaderItem("Header"),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultHeaderItem("Header"),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultHeaderItem("Header"),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    DefaultAvatarTextRatingSubtextDateItem(),
                    PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)))

            val randomData = RandomData()
            randomData.addGenerators(arrayOf(DrawableAvatarGenerator(this), StringNameGenerator().withMatcher { f -> f.name == "text" && f.declaringClass == DefaultAvatarTextRatingSubtextDateItem::class.java }, IntegerGenerator(0, 5).withMatcher { f -> f.name == "rating" }, TextGenerator().withMatcher { f -> f.name == "subtext" }, StringDateGenerator()))
            randomData.fill(items)


            val adapter: RowListAdapter<Serializable> = RowListAdapter(DefaultAvatarTextRatingSubtextDateItem::class.java, RowFactory<DefaultAvatarTextRatingSubtextDateItem> { AvatarTextRatingSubtextDateRow(it) })
            adapter.putFactory(PaddingItem::class.java, { PaddingRow(it) })
            adapter.putFactory(DefaultHeaderItem::class.java, { PaddedHeaderRow(it) })


            backdrop_contentRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            backdrop_contentRecycler.adapter = adapter
            adapter.items = items
        }
    }
}
