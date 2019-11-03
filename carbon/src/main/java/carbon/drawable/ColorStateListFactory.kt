package carbon.drawable

import android.content.Context
import android.content.res.ColorStateList
import carbon.Carbon
import carbon.R

object ColorStateListFactory {
    private fun make(context: Context, defaultColor: Int, activated: Int, disabled: Int,
                     invalid: Int = Carbon.getThemeColor(context, R.attr.carbon_colorError)): ColorStateList {
        return ColorStateList(
                arrayOf(
                        intArrayOf(-android.R.attr.state_enabled),
                        intArrayOf(R.attr.carbon_state_invalid),
                        intArrayOf(R.attr.carbon_state_indeterminate),
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_activated),
                        intArrayOf(android.R.attr.state_selected),
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf()
                ),
                intArrayOf(
                        disabled,
                        invalid,
                        activated,
                        activated,
                        activated,
                        activated,
                        activated,
                        defaultColor
                )

        )
    }

    private fun makeAlpha(context: Context, defaultColor: Int, activated: Int, disabled: Int,
                          invalid: Int = Carbon.getThemeColor(context, R.attr.carbon_colorError)): ColorStateList {
        return AlphaWithParentDrawable.AlphaWithParentColorStateList(
                arrayOf(
                        intArrayOf(-android.R.attr.state_enabled),
                        intArrayOf(R.attr.carbon_state_invalid),
                        intArrayOf(R.attr.carbon_state_indeterminate),
                        intArrayOf(android.R.attr.state_checked),
                        intArrayOf(android.R.attr.state_activated),
                        intArrayOf(android.R.attr.state_selected),
                        intArrayOf(android.R.attr.state_focused),
                        intArrayOf()
                ),
                intArrayOf(
                        disabled,
                        invalid,
                        activated,
                        activated,
                        activated,
                        activated,
                        activated,
                        defaultColor
                )

        )
    }

    private fun make2(context: Context, defaultColor: Int, disabled: Int,
                      invalid: Int = Carbon.getThemeColor(context, R.attr.carbon_colorError)): ColorStateList {
        return ColorStateList(
                arrayOf(
                        intArrayOf(-android.R.attr.state_enabled),
                        intArrayOf(R.attr.carbon_state_invalid),
                        intArrayOf()
                ),
                intArrayOf(
                        disabled,
                        invalid,
                        defaultColor
                )

        )
    }

    private fun makeAlpha2(context: Context, defaultColor: Int, disabled: Int,
                           invalid: Int = Carbon.getThemeColor(context, R.attr.carbon_colorError)): ColorStateList {
        return AlphaWithParentDrawable.AlphaWithParentColorStateList(
                arrayOf(
                        intArrayOf(-android.R.attr.state_enabled),
                        intArrayOf(R.attr.carbon_state_invalid),
                        intArrayOf()
                ),
                intArrayOf(
                        disabled,
                        invalid,
                        defaultColor
                )

        )
    }

    fun makeHighlight(context: Context): ColorStateList = make(context,
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated) and 0xffffff)),
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorError) and 0xffffff)))

    fun makeHighlightPrimary(context: Context): ColorStateList = make(context,
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorPrimary) and 0xffffff)),
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorError) and 0xffffff))
    )

    fun makeHighlightSecondary(context: Context): ColorStateList = make(context,
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorSecondary) and 0xffffff)),
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorError) and 0xffffff))
    )

    fun makePrimary(context: Context): ColorStateList = makeAlpha2(context,
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makePrimaryInverse(context: Context): ColorStateList = makeAlpha2(context,
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )

    fun makeSecondary(context: Context): ColorStateList = makeAlpha2(context,
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makeSecondaryInverse(context: Context): ColorStateList = makeAlpha2(context,
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )


    fun makeControl(context: Context): ColorStateList = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControl),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makeControlInverse(context: Context): ColorStateList = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControlInverse),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlActivatedInverse),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )

    fun makeControlPrimary(context: Context): ColorStateList = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControl),
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makeControlPrimaryInverse(context: Context): ColorStateList = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControlInverse),
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )

    fun makeControlSecondary(context: Context): ColorStateList = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControl),
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makeControlSecondaryInverse(context: Context): ColorStateList = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControlInverse),
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )


    fun makeTextSecondary(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiary)
    )

    fun makeTextSecondaryInverse(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse)
    )

    fun makeTextPrimary(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiary)
    )

    fun makeTextPrimaryInverse(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse)
    )


    fun makePrimaryText(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, android.R.attr.textColorPrimary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiary)
    )

    fun makePrimaryTextInverse(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, android.R.attr.textColorPrimaryInverse),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse)
    )

    fun makeSecondaryText(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, android.R.attr.textColorSecondary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiary)
    )

    fun makeSecondaryTextInverse(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, android.R.attr.textColorSecondaryInverse),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse)
    )


    fun makeIcon(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColor),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabled)
    )

    fun makeIconInverse(context: Context): ColorStateList = make2(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColorInverse),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabledInverse)
    )

    fun makeIconPrimary(context: Context): ColorStateList = make(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColor),
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabled)
    )

    fun makeIconPrimaryInverse(context: Context): ColorStateList = make(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColorInverse),
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabledInverse)
    )

    fun makeIconSecondary(context: Context): ColorStateList = make(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColor),
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabled)
    )

    fun makeIconSecondaryInverse(context: Context): ColorStateList = make(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColorInverse),
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabledInverse)
    )

}
