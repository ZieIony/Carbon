package tk.zielony.carbonsamples.widget

import android.graphics.drawable.Drawable
import android.os.Bundle
import carbon.component.*
import carbon.recycler.RowFactory
import carbon.recycler.RowListAdapter
import carbon.widget.BackdropLayout
import kotlinx.android.synthetic.main.activity_backdrop.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.common.DateGenerator
import tk.zielony.randomdata.common.IntegerGenerator
import tk.zielony.randomdata.common.TextGenerator
import tk.zielony.randomdata.person.DrawableAvatarGenerator
import tk.zielony.randomdata.person.StringNameGenerator
import tk.zielony.randomdata.transformer.DateToStringTransformer
import java.io.Serializable

@SampleAnnotation(layoutId = R.layout.activity_backdrop, titleId = R.string.backdropActivity_title)
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
                    DefaultAvatarTextRatingSubtextDateItem())

            val randomData = RandomData().apply {
                addGenerator(Drawable::class.java, DrawableAvatarGenerator(this@BackdropActivity))
                addGenerator(String::class.java, StringNameGenerator().withMatcher { f -> f.name == "text" && f.declaringClass == DefaultAvatarTextRatingSubtextDateItem::class.java })
                addGenerator(Int::class.java, IntegerGenerator(0, 5).withMatcher { f -> f.name == "rating" })
                addGenerator(String::class.java, TextGenerator().withMatcher { f -> f.name == "subtext" })
                addGenerator(String::class.java, DateGenerator().withTransformer(DateToStringTransformer()))
            }
            randomData.fill(items)


            val adapter: RowListAdapter<Serializable> = RowListAdapter(DefaultAvatarTextRatingSubtextDateItem::class.java, RowFactory<DefaultAvatarTextRatingSubtextDateItem> { AvatarTextRatingSubtextDateRow(it) })
            adapter.putFactory(DefaultHeaderItem::class.java, { PaddedHeaderRow(it) })


            backdrop_contentRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
            backdrop_contentRecycler.adapter = adapter
            adapter.items = items
        }
    }
}
