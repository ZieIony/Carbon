package tk.zielony.carbonsamples.library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import carbon.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class PicassoView : ImageView, Target {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
        setImageBitmap(bitmap)
        animateVisibility(View.VISIBLE)
    }

    override fun onBitmapFailed(errorDrawable: Drawable) {}

    override fun onPrepareLoad(placeHolderDrawable: Drawable) {
        animateVisibility(View.INVISIBLE)
    }
}
