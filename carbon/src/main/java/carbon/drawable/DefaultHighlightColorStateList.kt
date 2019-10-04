package carbon.drawable

import android.content.Context
import android.content.res.ColorStateList

import carbon.Carbon
import carbon.R

class DefaultHighlightColorStateList(context: Context) : ColorStateList(
        arrayOf(
                intArrayOf(R.attr.carbon_state_invalid),
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_activated),
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf()
        ),
        intArrayOf(
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorError) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated) and 0xffffff)),
                0
        )
)

class DefaultHighlightColorAccentStateList(context: Context) : ColorStateList(
        arrayOf(
                intArrayOf(R.attr.carbon_state_invalid),
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_activated),
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf()
        ),
        intArrayOf(
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorError) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorAccent) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorAccent) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorAccent) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorAccent) and 0xffffff)),
                0
        )
)

class DefaultHighlightColorPrimaryStateList(context: Context) : ColorStateList(
        arrayOf(
                intArrayOf(R.attr.carbon_state_invalid),
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_activated),
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf()
        ),
        intArrayOf(
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorError) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorPrimary) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorPrimary) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorPrimary) and 0xffffff)),
                (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorPrimary) and 0xffffff)),
                0
        )
)
