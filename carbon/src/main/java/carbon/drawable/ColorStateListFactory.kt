package carbon.drawable

import android.content.Context
import android.content.res.ColorStateList
import carbon.Carbon
import carbon.R

object ColorStateListFactory {
    private fun make(context: Context, defaultColor: Int, activated: Int, disabled: Int,
                     invalid: Int = Carbon.getThemeColor(context, R.attr.colorError)): ColorStateList {
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
                          invalid: Int = Carbon.getThemeColor(context, R.attr.colorError)): ColorStateList {
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
                      invalid: Int = Carbon.getThemeColor(context, R.attr.colorError)): ColorStateList {
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
                           invalid: Int = Carbon.getThemeColor(context, R.attr.colorError)): ColorStateList {
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

    fun makeHighlight(context: Context) = make(context,
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated) and 0xffffff)),
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorError) and 0xffffff)))

    fun makeHighlightPrimary(context: Context) = make(context,
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorPrimary) and 0xffffff)),
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorError) and 0xffffff))
    )

    fun makeHighlightSecondary(context: Context) = make(context,
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorSecondary) and 0xffffff)),
            0,
            (0x12000000 or (Carbon.getThemeColor(context, R.attr.colorError) and 0xffffff))
    )

    fun makeMenuSelection(context: Context) = MenuSelectionDrawable(
            Carbon.getThemeDimen(context, R.attr.carbon_menuSelectionRadius),
            Carbon.getThemeDimen(context, R.attr.carbon_menuSelectionInset),
            makeHighlight(context))

    fun makeMenuSelectionPrimary(context: Context) = MenuSelectionDrawable(
            Carbon.getThemeDimen(context, R.attr.carbon_menuSelectionRadius),
            Carbon.getThemeDimen(context, R.attr.carbon_menuSelectionInset),
            makeHighlightPrimary(context))

    fun makeMenuSelectionSecondary(context: Context) = MenuSelectionDrawable(
            Carbon.getThemeDimen(context, R.attr.carbon_menuSelectionRadius),
            Carbon.getThemeDimen(context, R.attr.carbon_menuSelectionInset),
            makeHighlightSecondary(context))

    fun makePrimary(context: Context) = makeAlpha2(context,
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makePrimaryInverse(context: Context) = makeAlpha2(context,
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )

    fun makeSecondary(context: Context) = makeAlpha2(context,
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makeSecondaryInverse(context: Context) = makeAlpha2(context,
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )


    fun makeControl(context: Context) = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControl),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlActivated),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makeControlInverse(context: Context) = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControlInverse),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlActivatedInverse),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )

    fun makeControlPrimary(context: Context) = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControl),
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makeControlPrimaryInverse(context: Context) = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControlInverse),
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )

    fun makeControlSecondary(context: Context) = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControl),
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabled)
    )

    fun makeControlSecondaryInverse(context: Context) = makeAlpha(context,
            Carbon.getThemeColor(context, R.attr.carbon_colorControlInverse),
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_colorControlDisabledInverse)
    )


    fun makeTextSecondary(context: Context) = make2(context,
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiary)
    )

    fun makeTextSecondaryInverse(context: Context) = make2(context,
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse)
    )

    fun makeTextPrimary(context: Context) = make2(context,
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiary)
    )

    fun makeTextPrimaryInverse(context: Context) = make2(context,
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse)
    )


    fun makePrimaryText(context: Context) = make2(context,
            Carbon.getThemeColor(context, android.R.attr.textColorPrimary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiary)
    )

    fun makePrimaryTextInverse(context: Context) = make2(context,
            Carbon.getThemeColor(context, android.R.attr.textColorPrimaryInverse),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse)
    )

    fun makeSecondaryText(context: Context) = make2(context,
            Carbon.getThemeColor(context, android.R.attr.textColorSecondary),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiary)
    )

    fun makeSecondaryTextInverse(context: Context) = make2(context,
            Carbon.getThemeColor(context, android.R.attr.textColorSecondaryInverse),
            Carbon.getThemeColor(context, android.R.attr.textColorTertiaryInverse)
    )


    fun makeIcon(context: Context) = make2(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColor),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabled)
    )

    fun makeIconInverse(context: Context) = make2(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColorInverse),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabledInverse)
    )

    fun makeIconPrimary(context: Context) = make(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColor),
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabled)
    )

    fun makeIconPrimaryInverse(context: Context) = make(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColorInverse),
            Carbon.getThemeColor(context, R.attr.colorPrimary),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabledInverse)
    )

    fun makeIconSecondary(context: Context) = make(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColor),
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabled)
    )

    fun makeIconSecondaryInverse(context: Context) = make(context,
            Carbon.getThemeColor(context, R.attr.carbon_iconColorInverse),
            Carbon.getThemeColor(context, R.attr.colorSecondary),
            Carbon.getThemeColor(context, R.attr.carbon_iconColorDisabledInverse)
    )

}
