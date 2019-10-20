package tk.zielony.carbonsamples

import android.content.Context
import android.os.Bundle

abstract class ThemedActivity : SamplesActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        applyTheme()
        super.onCreate(savedInstanceState)
    }

    open protected fun applyTheme() {
        val preferences = getSharedPreferences(ColorsActivity.THEME, Context.MODE_PRIVATE)
        setTheme(ColorsActivity.styles[preferences.getInt(ColorsActivity.STYLE, 2)].value)
        theme.applyStyle(ColorsActivity.primary[preferences.getInt(ColorsActivity.PRIMARY, 8)].value, true)
        theme.applyStyle(ColorsActivity.secondary[preferences.getInt(ColorsActivity.SECONDARY, 14)].value, true)
        theme.applyStyle(ColorsActivity.fonts[preferences.getInt(ColorsActivity.FONT, 0)].value, true)
    }
}
