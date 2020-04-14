package tk.zielony.carbonsamples

import android.os.Bundle
import tk.zielony.carbonsamples.dialog.*

@SampleAnnotation(titleId = R.string.dialogsActivity_title, layoutId = R.layout.activity_samplelist)
class DialogsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                "Various dialog windows",
                SampleActivityItem(ListDialogActivity::class.java),
                SampleActivityItem(ProgressDialogActivity::class.java),
                SampleActivityItem(SelectDialogActivity::class.java),
                SampleActivityItem(SimpleDialogActivity::class.java),
                SampleActivityItem(DialogFragmentActivity::class.java)
        ))
    }
}
