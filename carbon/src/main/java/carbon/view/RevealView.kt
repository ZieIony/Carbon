package carbon.view

import android.animation.Animator

interface RevealView {

    fun createCircularReveal(hotspot: android.view.View, startRadius: Float, finishRadius: Float): Animator

    fun createCircularReveal(x: Int, y: Int, startRadius: Float, finishRadius: Float): Animator

    companion object {
        const val MAX_RADIUS = -1f
    }
}
