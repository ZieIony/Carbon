package carbon.drawable

import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable


open class MenuSelectionDrawable : Drawable {
    private val radius: Float
    private val inset: Float
    private val color: ColorStateList
    private val paint: Paint
    private val rect: RectF

    constructor(radius: Float, inset: Float, color: ColorStateList) : super() {
        this.radius = radius
        this.inset = inset
        this.color = color
        this.paint = Paint(Paint.ANTI_ALIAS_FLAG)
        this.rect = RectF()
    }

    override fun draw(canvas: Canvas) {
        paint.color = color.getColorForState(state, color.defaultColor)
        canvas.drawRoundRect(rect, radius, radius, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun isStateful() = true

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        rect.set(left + inset, top + inset, right - inset, bottom - inset)
    }
}