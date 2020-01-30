package tk.zielony.carbonsamples.dialog

import android.os.Bundle
import android.view.View
import carbon.dialog.TextDialog
import carbon.widget.EditText
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(layoutId = R.layout.activity_simpledialog, titleId = R.string.simpleDialogActivity_title)
class SimpleDialogActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val titleText = findViewById<EditText>(R.id.titleText)
        val buttonText = findViewById<EditText>(R.id.buttonText)

        findViewById<View>(R.id.showDialog).setOnClickListener { view: View? ->
            val dialog = TextDialog(this)
            dialog.setText("Danish carrot cake toffee cupcake caramels chocolate cheesecake. Sugar plum chocolate cake dragée chocolate cake chupa chups jelly dessert. Icing lemon drops ice cream. Sesame snaps jelly cake biscuit. Ice cream halvah cookie gingerbread cookie candy donut sweet.")
            if (titleText.length() > 0) dialog.setTitle(titleText.getText())
            if (buttonText.length() > 0) dialog.addButton(buttonText.getText().toString(), null)
            dialog.show()
        }
    }
}