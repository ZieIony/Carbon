package tk.zielony.carbonsamples

import android.os.Bundle
import carbon.component.PaddingItem
import tk.zielony.carbonsamples.dialog.ListDialogActivity
import tk.zielony.carbonsamples.dialog.ProgressDialogActivity
import tk.zielony.carbonsamples.dialog.SelectDialogActivity
import tk.zielony.carbonsamples.dialog.SimpleDialogActivity

@ActivityAnnotation(title = R.string.dialogsActivity_title, layout = R.layout.activity_samplelist)
class DialogsActivity : SampleListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        setItems(listOf(
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Various dialog windows",
                SampleActivityItem(ListDialogActivity::class.java),
                SampleActivityItem(ProgressDialogActivity::class.java),
                SampleActivityItem(SelectDialogActivity::class.java),
                SampleActivityItem(SimpleDialogActivity::class.java),
                PaddingItem(resources.getDimensionPixelSize(R.dimen.carbon_paddingHalf))
        ))
    }
}
