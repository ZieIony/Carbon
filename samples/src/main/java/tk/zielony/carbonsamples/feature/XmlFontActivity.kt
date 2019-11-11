package tk.zielony.carbonsamples.feature

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_xmlfont.*
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.CodeActivity

import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(
        layoutId = R.layout.activity_xmlfont,
        titleId = R.string.xmlFontActivity_title,
        iconId = R.drawable.ic_font_download_black_24dp
)
class XmlFontActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        code_fonts.setOnClickListener { CodeActivity.start(this, CODE_FONTS) }
    }

    companion object {
        val CODE_FONTS = """
        <carbon.widget.TextView
            android:layout_width="wrap_content"
            android:layout_height="@dimen/carbon_listItemHeight"
            android:gravity="center_vertical"
            android:text="Lato regular"
            android:textSize="20sp"
            app:carbon_font="@font/lato" />

        <carbon.widget.TextView
            android:layout_width="wrap_content"
            android:layout_height="@dimen/carbon_listItemHeight"
            android:gravity="center_vertical"
            android:text="Lato light"
            android:textSize="20sp"
            app:carbon_font="@font/lato"
            app:carbon_fontWeight="200" />

        <carbon.widget.TextView
            android:layout_width="wrap_content"
            android:layout_height="@dimen/carbon_listItemHeight"
            android:gravity="center_vertical"
            android:text="Roboto bold"
            android:textSize="20sp"
            app:carbon_font="@font/roboto"
            app:carbon_fontWeight="700" />

        <carbon.widget.TextView
            android:layout_width="wrap_content"
            android:layout_height="@dimen/carbon_listItemHeight"
            android:gravity="center_vertical"
            android:text="Roboto italic"
            android:textSize="20sp"
            android:textStyle="italic"
            app:carbon_font="@font/roboto" />

        <carbon.widget.TextView
            android:layout_width="wrap_content"
            android:layout_height="@dimen/carbon_listItemHeight"
            android:gravity="center_vertical"
            android:text="Roboto condensed regular"
            android:textSize="20sp"
            app:carbon_font="@font/roboto_condensed" />

        <carbon.widget.TextView
            android:layout_width="wrap_content"
            android:layout_height="@dimen/carbon_listItemHeight"
            android:gravity="center_vertical"
            android:text="Roboto thin italic"
            android:textSize="20sp"
            android:textStyle="italic"
            app:carbon_font="@font/roboto"
            app:carbon_fontWeight="200" />

        <carbon.widget.TextView
            android:layout_width="wrap_content"
            android:layout_height="@dimen/carbon_listItemHeight"
            android:gravity="center_vertical"
            android:text="Lobster"
            android:textSize="20sp"
            app:carbon_font="@font/lobster" />

        <carbon.widget.TextView
            android:layout_width="wrap_content"
            android:layout_height="@dimen/carbon_listItemHeight"
            android:gravity="center_vertical"
            android:text="Lekton Regular"
            android:textSize="20sp"
            app:carbon_font="@font/lekton_regular" />
        """.trimIndent()
    }
}
