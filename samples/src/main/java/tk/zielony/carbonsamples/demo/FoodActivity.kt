package tk.zielony.carbonsamples.demo

import android.graphics.drawable.Drawable
import android.os.Bundle
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import java.io.Serializable

@ActivityAnnotation(layout = R.layout.activity_food, title = R.string.foodActivity_title)
class FoodActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
    }
}

class FoodHeaderItem(val image: Drawable, val text: String) : Serializable