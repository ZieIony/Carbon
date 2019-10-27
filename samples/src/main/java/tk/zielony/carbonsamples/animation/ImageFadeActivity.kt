package tk.zielony.carbonsamples.animation

import android.os.Bundle
import android.os.Handler
import android.view.View
import carbon.widget.ImageView
import kotlinx.android.synthetic.main.activity_imagefade.*
import tk.zielony.carbonsamples.ActivityAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity
import tk.zielony.randomdata.DataContext
import tk.zielony.randomdata.common.DrawableImageGenerator

@ActivityAnnotation(layout = R.layout.activity_imagefade, title = R.string.imageFadeActivity_title)
class ImageFadeActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()

        val imageGenerator = DrawableImageGenerator(this)
        val dataContext = DataContext()
        imageFade_grid.views.map { it as ImageView }.forEach { it.setImageDrawable(imageGenerator.next(dataContext)) }

        val handler = Handler()

        imageFade_button.setOnClickListener { view ->
            imageFade_grid.views.map { it as ImageView }.forEachIndexed { i, imageView ->
                handler.postDelayed({ imageView.animateVisibility(if (imageView.isVisible) View.INVISIBLE else View.VISIBLE) }, (i * 50).toLong())
            }
        }
    }
}
