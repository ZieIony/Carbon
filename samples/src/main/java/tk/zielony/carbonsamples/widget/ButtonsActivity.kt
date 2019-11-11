package tk.zielony.carbonsamples.widget

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_buttons.*
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.CodeActivity
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(layoutId = R.layout.activity_buttons, titleId = R.string.buttonsActivity_title)
class ButtonsActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        code_buttons.setOnClickListener { CodeActivity.start(this, CODE_BUTTONS) }
        code_buttonsInversed.setOnClickListener { CodeActivity.start(this, CODE_BUTTONS_INVERSED) }
        code_buttonsColored.setOnClickListener { CodeActivity.start(this, CODE_BUTTONS_COLORED) }
        code_buttonsColoredInversed.setOnClickListener { CodeActivity.start(this, CODE_BUTTONS_COLORED_INVERSED) }
        code_buttonsCustom.setOnClickListener { CodeActivity.start(this, CODE_BUTTONS_CUSTOM) }
    }

    companion object {
        val CODE_BUTTONS = """
             <carbon.widget.Button
                    style="@style/carbon_Button"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_margin="@dimen/carbon_padding"
                    android:text="Contained Button" />

                <carbon.widget.Button
                    style="@style/carbon_Button.Outlined"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_margin="@dimen/carbon_padding"
                    android:text="Outlined button" />

                <carbon.widget.Button
                    style="@style/carbon_Button.Flat"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_margin="@dimen/carbon_padding"
                    android:text="Flat button" />
        """.trimIndent()

        val CODE_BUTTONS_INVERSED = """
                    <carbon.widget.Button
                        style="@style/carbon_Button.Inverse"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_margin="@dimen/carbon_padding"
                        android:text="Contained Button" />

                    <carbon.widget.Button
                        style="@style/carbon_Button.Outlined.Inverse"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_margin="@dimen/carbon_padding"
                        android:text="Outlined button" />

                    <carbon.widget.Button
                        style="@style/carbon_Button.Flat.Inverse"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_margin="@dimen/carbon_padding"
                        android:text="Flat button" />
        """.trimIndent()

        val CODE_BUTTONS_COLORED = """
                <carbon.widget.Button
                    style="@style/carbon_Button.Colored"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_margin="@dimen/carbon_padding"
                    android:tag="enable"
                    android:text="Contained Button" />

                <carbon.widget.Button
                    style="@style/carbon_Button.Outlined.Colored"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_margin="@dimen/carbon_padding"
                    android:tag="enable"
                    android:text="Outlined button" />

                <carbon.widget.Button
                    style="@style/carbon_Button.Flat.Colored"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_margin="@dimen/carbon_padding"
                    android:tag="enable"
                    android:text="flat button" />
        """.trimIndent()

        val CODE_BUTTONS_COLORED_INVERSED = """
                    <carbon.widget.Button
                        style="@style/carbon_Button.Colored.Inverse"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_margin="@dimen/carbon_padding"
                        android:text="Contained Button" />

                    <carbon.widget.Button
                        style="@style/carbon_Button.Outlined.Colored.Inverse"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_margin="@dimen/carbon_padding"
                        android:text="Outlined button" />

                    <carbon.widget.Button
                        style="@style/carbon_Button.Flat.Colored.Inverse"
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_margin="@dimen/carbon_padding"
                        android:text="flat button" />
        """.trimIndent()

        val CODE_BUTTONS_CUSTOM = """
            <carbon.widget.Button
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_margin="@dimen/carbon_padding"
                android:background="@drawable/buttonshape"
                android:tag="enable"
                android:text="Custom shape"
                android:textColor="@color/carbon_textColorPrimary_dark"
                app:carbon_backgroundTint="@null"
                app:carbon_cornerRadius="0dp"
                app:carbon_cornerRadiusTopStart="24dp" />

            <carbon.widget.Button
                style="@style/carbon_Button"
                android:layout_width="wrap_content"
                android:layout_height="36dp"
                android:layout_margin="@dimen/carbon_padding"
                android:background="#ffffffff"
                android:paddingLeft="@dimen/carbon_padding"
                android:paddingTop="0dp"
                android:paddingRight="@dimen/carbon_padding"
                android:paddingBottom="0dp"
                android:tag="enable"
                android:text="Inline Style"
                android:textColor="#ff0000ff"
                app:carbon_backgroundTint="@null"
                app:carbon_cornerCut="4dp"
                app:carbon_elevation="@dimen/carbon_elevationLow"
                app:carbon_rippleColor="#40ff0000"
                app:carbon_touchMarginBottom="6dp"
                app:carbon_touchMarginLeft="100dp"
                app:carbon_touchMarginRight="100dp"
                app:carbon_touchMarginTop="6dp" />

            <carbon.widget.Button
                android:layout_width="wrap_content"
                android:layout_height="36dp"
                android:layout_margin="@dimen/carbon_padding"
                android:background="@color/carbon_white"
                android:tag="enable"
                android:text="Rounded with ripple"
                android:textColor="@color/carbon_amber_700"
                app:carbon_cornerRadius="18dp"
                app:carbon_rippleColor="#40ff0000"
                app:carbon_stroke="@color/carbon_amber_700"
                app:carbon_strokeWidth="2dp" />

            <carbon.widget.Button
                android:layout_width="wrap_content"
                android:layout_height="56dp"
                android:layout_margin="@dimen/carbon_padding"
                android:background="@drawable/randomdata_background0"
                android:tag="enable"
                android:text="Custom"
                android:textColor="@color/carbon_white"
                android:textSize="@dimen/carbon_textSizeHeadline"
                app:carbon_cornerCutBottomStart="16dp"
                app:carbon_cornerCutTopEnd="16dp"
                app:carbon_cornerRadiusBottomEnd="8dp"
                app:carbon_cornerRadiusTopStart="8dp"
                app:carbon_rippleColor="#4000ff00"
                app:carbon_stroke="@color/carbon_green_700"
                app:carbon_strokeWidth="2dp" />
        """.trimIndent()
    }
}

