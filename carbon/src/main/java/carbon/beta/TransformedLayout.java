package carbon.beta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import carbon.widget.FrameLayout;

public class TransformedLayout extends FrameLayout {

    private Matrix matrix = new Matrix();
    private Matrix inverse = new Matrix();

    public TransformedLayout(Context context) {
        super(context);
    }

    public TransformedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TransformedLayout(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TransformedLayout(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
        matrix.invert(inverse);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent event) {
        MotionEvent eventCopy = MotionEvent.obtain(event);
        eventCopy.transform(inverse);
        boolean result = super.dispatchTouchEvent(eventCopy);
        eventCopy.recycle();
        return result;
    }

    @Override
    protected void dispatchDraw(@NonNull Canvas canvas) {
        int saveCount = canvas.save();
        canvas.setMatrix(matrix);
        super.dispatchDraw(canvas);
        canvas.restoreToCount(saveCount);
    }
}
