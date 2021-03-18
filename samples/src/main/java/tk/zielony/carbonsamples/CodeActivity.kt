package tk.zielony.carbonsamples

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.annimon.stream.Stream
import kotlinx.android.synthetic.main.activity_code.*
import java.util.regex.Pattern

@SampleAnnotation(layoutId = R.layout.activity_code, titleId = R.string.codeActivity_title)
class CodeActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val codeString = intent.getStringExtra(CODE)
        code.text = colorSyntax(codeString!!)

        toolbar?.setOnMenuItemClicked { view, item, position ->
            when (item.id) {
                R.id.share -> {
                    val sharingIntent = Intent(Intent.ACTION_SEND);
                    sharingIntent.type = "text/plain";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Carbon code");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, code.text);
                    startActivity(Intent.createChooser(sharingIntent, "Share"));
                }
            }
        }
    }

    private fun colorSyntax(text: String): Spannable {
        val builder = SpannableStringBuilder(text)

        val quotedTextColor = resources.getColor(R.color.carbon_green_400)
        val keywordColor = resources.getColor(R.color.carbon_blue_400)

        val pattern = Pattern.compile("\"([^\"]*)\"")
        val matcher = pattern.matcher(text)
        while (matcher.find()) {
            builder.setSpan(ForegroundColorSpan(quotedTextColor), matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }

        val keyword = arrayOf("carbon")
        Stream.of(*keyword).forEach { k ->
            var index = 0
            while (index < text.length) {
                index = text.indexOf(k, index)
                if (index == -1)
                    break
                builder.setSpan(ForegroundColorSpan(keywordColor), index, index + k.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                index += k.length
            }
        }
        return builder
    }

    companion object {
        fun start(context: Context, codeText: String) {
            context.startActivity(Intent(context, CodeActivity::class.java).also { it.putExtra(CODE, codeText) })
        }

        private const val CODE = "code"
    }
}
