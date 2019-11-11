package tk.zielony.carbonsamples.demo

import android.graphics.drawable.Drawable
import android.os.Bundle
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import java.io.Serializable

@SampleAnnotation(layoutId = R.layout.activity_food, titleId = R.string.foodActivity_title)
class FoodActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
    }
}

class FoodHeaderItem(val image: Drawable, val text: String) : Serializable