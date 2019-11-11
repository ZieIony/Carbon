package tk.zielony.carbonsamples.feature

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import carbon.gesture.GestureDetector
import carbon.gesture.OnGestureListener
import kotlinx.android.synthetic.main.activity_gesturedetector.*
import tk.zielony.carbonsamples.SampleAnnotation
import tk.zielony.carbonsamples.R
import tk.zielony.carbonsamples.ThemedActivity

@SampleAnnotation(
        layoutId = R.layout.activity_gesturedetector,
        titleId = R.string.gestureDetectorActivity_title,
        iconId = R.drawable.ic_gesture_black_24dp
)
class GestureDetectorActivity : ThemedActivity(), OnGestureListener {

    var handler = Handler()
    var clearRunnable = Runnable {
        state.text = "state"
        state.background = null
        testText.text = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar()
        val detector = GestureDetector(this)
        detector.addOnGestureListener(this)
        testArea.setOnDispatchTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> onDown(event)
                MotionEvent.ACTION_UP -> onUp(event)
                MotionEvent.ACTION_MOVE -> onMove(event)
                MotionEvent.ACTION_CANCEL -> onCancel(event)
            }
            detector.onTouchEvent(event)
            true
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(clearRunnable)
    }

    override fun onPress(motionEvent: MotionEvent) {
        logEvent("press", R.color.carbon_amber_200)
    }

    override fun onLongPress(motionEvent: MotionEvent) {
        logEvent("long press", R.color.carbon_indigo_200)
    }

    override fun onTap(motionEvent: MotionEvent, clicks: Int) {
        logEvent("tap $clicks", R.color.carbon_green_200)
    }

    override fun onDrag(motionEvent: MotionEvent, dx: Float, dy: Float) {
        logEvent("drag", R.color.carbon_blueGrey_200)
    }

    override fun onTransform(motionEvent: MotionEvent?, cx: Float, cy: Float, dx: Float, dy: Float, rx: Float, scale: Float) {
        logEvent("transform", R.color.carbon_blue_200)
    }


    private fun onDown(motionEvent: MotionEvent) = logEvent("down")

    private fun onUp(motionEvent: MotionEvent) = logEvent("up")

    private fun onMove(motionEvent: MotionEvent) = logEvent("move")

    private fun onCancel(motionEvent: MotionEvent) = logEvent("cancel")


    private fun logEvent(text: String, color: Int? = null) {
        state.text = text
        state.background = if (color != null) ColorDrawable(resources.getColor(color)) else null
        testText.text = "${testText.text}\n${text}"
        handler.removeCallbacks(clearRunnable)
        handler.postDelayed(clearRunnable, CLEAR_TIMEOUT)
    }

    companion object {
        const val CLEAR_TIMEOUT = 2000L
    }
}
