package tk.zielony.carbonsamples.component

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.component.*
import carbon.drawable.VectorDrawable
import carbon.recycler.RowListAdapter
import kotlinx.android.synthetic.main.activity_register.*
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import java.io.Serializable

@SampleAnnotation(layoutId = R.layout.activity_register, titleId = R.string.registerActivity_title)
class RegisterActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        val adapter = RowListAdapter<Serializable>().apply {
            putFactory(DefaultIconEditTextItem::class.java, { IconEditTextRow(it) })
            putFactory(PaddingItem::class.java, { PaddingRow(it) })
            putFactory(DividerItem::class.java, { DividerRow(it) })
            putFactory(DefaultIconPasswordItem::class.java, { IconPasswordRow(it) })
            putFactory(DefaultIconDropDownItem::class.java, { IconDropDownRow<DefaultIconDropDownItem<*>, String>(it) })
        }

        adapter.items = listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                DefaultIconEditTextItem(VectorDrawable(resources, R.raw.profile), "login", ""),
                DefaultIconEditTextItem(VectorDrawable(resources, R.raw.email), "email", ""),
                DefaultIconPasswordItem(VectorDrawable(resources, R.raw.lock), "password", ""),
                DefaultIconPasswordItem(null, "retype password", ""),
                DefaultIconDropDownItem(VectorDrawable(resources, R.raw.gender), "sex", arrayOf("Male", "Female"), "Male"),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)))

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }
}