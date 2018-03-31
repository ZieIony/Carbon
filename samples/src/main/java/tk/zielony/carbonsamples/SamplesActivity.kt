package tk.zielony.carbonsamples

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class SamplesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)

        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            if (it.layout != 0)
                setContentView(it.layout)
            if (it.title != 0)
                Samples.initToolbar(this, getString(it.title))
        }
    }

    private fun applyTheme() {
        val preferences = getSharedPreferences(ColorsActivity.THEME, Context.MODE_PRIVATE)
        setTheme(ColorsActivity.styles[preferences.getInt(ColorsActivity.STYLE, 1)].value)
        theme.applyStyle(ColorsActivity.primary[preferences.getInt(ColorsActivity.PRIMARY, 8)].value, true)
        theme.applyStyle(ColorsActivity.accents[preferences.getInt(ColorsActivity.ACCENT, 14)].value, true)
    }
}
