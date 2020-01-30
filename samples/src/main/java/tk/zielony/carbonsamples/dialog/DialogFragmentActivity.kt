package tk.zielony.carbonsamples.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import carbon.widget.Toolbar
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(layoutId = R.layout.activity_dialogfragment, titleId = R.string.dialogFragmentActivity_title)
class DialogFragmentActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        findViewById<View>(R.id.showDialog).setOnClickListener { view: View? ->
            SimpleDialogFragment().show(supportFragmentManager, "dialog")
        }
    }
}

class SimpleDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?) =
            carbon.dialog.Dialog(context!!)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.activity_about, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationIcon(R.drawable.carbon_clear)
        toolbar.setNavigationOnClickListener { dismiss() }
    }
}