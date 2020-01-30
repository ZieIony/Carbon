package tk.zielony.carbonsamples.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import carbon.component.DefaultImageTextSubtextDateItem
import carbon.component.ImageTextSubtextDateRow
import carbon.dialog.ListDialog
import carbon.widget.EditText
import com.annimon.stream.Stream
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.RandomData
import tk.zielony.randomdata.Target
import tk.zielony.randomdata.common.DateGenerator
import tk.zielony.randomdata.common.DrawableImageGenerator
import tk.zielony.randomdata.common.TextGenerator
import tk.zielony.randomdata.person.StringNameGenerator
import tk.zielony.randomdata.transformer.DateToStringTransformer

@SampleAnnotation(layoutId = R.layout.activity_listdialog, titleId = R.string.listDialogActivity_title)
class ListDialogActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()

        val titleText = findViewById<EditText>(R.id.titleText)

        val items = Stream.generate { DefaultImageTextSubtextDateItem() }.limit(9).toList()

        val randomData = RandomData()
        randomData.addGenerator(Drawable::class.java, DrawableImageGenerator(this))
        randomData.addGenerator(String::class.java, StringNameGenerator().withMatcher {
            f: Target -> f.name == "text" && f.declaringClass == DefaultImageTextSubtextDateItem::class.java
        })
        randomData.addGenerator(String::class.java, TextGenerator().withMatcher { f: Target -> f.name == "subtext" })
        randomData.addGenerator(String::class.java, DateGenerator().withTransformer(DateToStringTransformer()))
        randomData.fill(items)

        findViewById<View>(R.id.button).setOnClickListener { view: View? ->
            val dialog = ListDialog<DefaultImageTextSubtextDateItem>(this)
            dialog.setItems(items, { ImageTextSubtextDateRow<DefaultImageTextSubtextDateItem>(it) })
            if (titleText.length() > 0) dialog.setTitle(titleText.getText())
            dialog.show()
        }
    }
}