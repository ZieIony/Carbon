package tk.zielony.carbonsamples

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import carbon.dialog.MultiSelectDialog
import carbon.drawable.CheckedState
import carbon.internal.DebugOverlay
import carbon.widget.CheckBox
import carbon.widget.ImageView
import carbon.widget.Toolbar
import java.util.*

abstract class SamplesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            if (it.layout != 0)
                setContentView(it.layout)
            if (it.title != 0)
                title = getString(it.title)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            if (it.menu != 0)
                menuInflater.inflate(it.menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    fun initToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = title
        toolbar?.setIconVisible(toolbar.icon != null)

        val debug = findViewById<ImageView>(R.id.debug)
        debug?.setOnClickListener(object : View.OnClickListener {
            var debugEnabled = false
            var debugOptions = arrayOf("bounds", "grid", "hit rects", "margins", "paddings", "rulers", "text sizes")

            val overlay = DebugOverlay(this@SamplesActivity)

            init {
                overlay.isDrawBoundsEnabled = false
                overlay.isDrawGridEnabled = false
                overlay.isDrawHitRectsEnabled = false
                overlay.isDrawMarginsEnabled = false
                overlay.isDrawPaddingsEnabled = false
                overlay.isDrawRulersEnabled = false
                overlay.isDrawTextSizesEnabled = false
            }

            override fun onClick(view: View) {
                val listDialog = MultiSelectDialog<String>(this@SamplesActivity)
                listDialog.setItems(debugOptions)
                listDialog.setTitle("Debug options")
                val initialItems = ArrayList<String>()
                if (overlay.isDrawBoundsEnabled)
                    initialItems.add("bounds")
                if (overlay.isDrawGridEnabled)
                    initialItems.add("grid")
                if (overlay.isDrawHitRectsEnabled)
                    initialItems.add("hit rects")
                if (overlay.isDrawMarginsEnabled)
                    initialItems.add("margins")
                if (overlay.isDrawPaddingsEnabled)
                    initialItems.add("paddings")
                if (overlay.isDrawRulersEnabled)
                    initialItems.add("rulers")
                if (overlay.isDrawTextSizesEnabled)
                    initialItems.add("text sizes")
                listDialog.selectedItems = initialItems
                listDialog.show()
                listDialog.setOnDismissListener { dialogInterface ->
                    val selectedItems = listDialog.selectedItems
                    overlay.isDrawBoundsEnabled = selectedItems.contains("bounds")
                    overlay.isDrawGridEnabled = selectedItems.contains("grid")
                    overlay.isDrawHitRectsEnabled = selectedItems.contains("hit rects")
                    overlay.isDrawMarginsEnabled = selectedItems.contains("margins")
                    overlay.isDrawPaddingsEnabled = selectedItems.contains("paddings")
                    overlay.isDrawRulersEnabled = selectedItems.contains("rulers")
                    overlay.isDrawTextSizesEnabled = selectedItems.contains("text sizes")

                    if (!debugEnabled && !selectedItems.isEmpty()) {
                        overlay.show()
                        debugEnabled = true
                    } else if (debugEnabled && selectedItems.isEmpty()) {
                        overlay.dismiss()
                        debugEnabled = false
                    }
                }
            }
        })

        val checkBox = findViewById<CheckBox>(R.id.enabled)
        checkBox?.setOnCheckedChangeListener { compoundButton, checked ->
            val views = ArrayList<View>()
            views.add(window.decorView.rootView)
            while (!views.isEmpty()) {
                val v = views.removeAt(0)
                if (v.id == R.id.toolbar)
                    continue
                v.isEnabled = checked == CheckedState.CHECKED
                if (v is ViewGroup) {
                    for (i in 0 until v.childCount)
                        views.add(v.getChildAt(i))
                }
            }
        }
    }

}
