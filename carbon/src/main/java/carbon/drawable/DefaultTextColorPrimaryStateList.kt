package carbon.drawable

import android.content.Context
import android.content.res.ColorStateList

import carbon.Carbon
import carbon.R

class DefaultTextColorPrimaryStateList(context: Context) : ColorStateList(
        arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(R.attr.carbon_state_invalid),
                intArrayOf()
        ),
        intArrayOf(
                Carbon.getThemeColor(context, android.R.attr.textColorTertiary),
                Carbon.getThemeColor(context, R.attr.carbon_colorError),
                Carbon.getThemeColor(context, R.attr.colorPrimary)
        )
)

class DefaultTextColorPrimaryInverseStateList(context: Context) : ColorStateList(
        arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(R.attr.carbon_state_invalid),
                intArrayOf()
        ),
        intArrayOf(
                Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse),
                Carbon.getThemeColor(context, R.attr.carbon_colorError),
                Carbon.getThemeColor(context, R.attr.colorPrimary)
        )
)
