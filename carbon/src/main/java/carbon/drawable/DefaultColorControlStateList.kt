package carbon.drawable

import android.content.Context
import android.content.res.ColorStateList

import carbon.Carbon
import carbon.R

class DefaultColorControlStateList(context: Context) : AlphaWithParentDrawable.Marker, ColorStateList(
        arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_activated),
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf()
        ),
        intArrayOf(
                Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated),
                Carbon.getThemeColor(context, R.attr.carbon_colorControl)
        )
)

class DefaultColorControlInverseStateList(context: Context) : AlphaWithParentDrawable.Marker, ColorStateList(
        arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_activated),
                intArrayOf(android.R.attr.state_selected),
                intArrayOf(android.R.attr.state_focused),
                intArrayOf()
        ),
        intArrayOf(
                Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlActivatedInverse),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlActivatedInverse),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlActivatedInverse),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlActivatedInverse),
                Carbon.getThemeColor(context, R.attr.carbon_colorControlInverse)
        )
)
