package tk.zielony.carbonsamples

import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

abstract class SamplesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            if (it.layout != 0)
                setContentView(it.layout)
            if (it.title != 0)
                Samples.initToolbar(this, getString(it.title))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        javaClass.getAnnotation(ActivityAnnotation::class.java)?.let {
            if (it.menu != 0)
                menuInflater.inflate(it.menu, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun applyTheme() {
        val preferences = getSharedPreferences(ColorsActivity.THEME, Context.MODE_PRIVATE)
        setTheme(ColorsActivity.styles[preferences.getInt(ColorsActivity.STYLE, 2)].value)
        theme.applyStyle(ColorsActivity.primary[preferences.getInt(ColorsActivity.PRIMARY, 8)].value, true)
        theme.applyStyle(ColorsActivity.accents[preferences.getInt(ColorsActivity.ACCENT, 14)].value, true)
    }
}
