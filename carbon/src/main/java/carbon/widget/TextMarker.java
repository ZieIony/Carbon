package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import carbon.R;

public class TextMarker extends View {
    Paint paint;
    Rect rect = new Rect();
    String text = "I";
    private int id;

    public TextMarker(Context context) {
        super(context);
    }

    public TextMarker(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0);
    }

    public TextMarker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextMarker, defStyleAttr, 0);

            for (int i = 0; i < a.getIndexCount(); i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.TextMarker_carbon_text) {
                    setText(a.getText(attr).toString());
                } else if (attr == R.styleable.TextMarker_carbon_textView) {
                    id = a.getResourceId(attr, 0);
                }
            }

            a.recycle();
        }
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (paint == null && id != 0) {
            paint = ((android.widget.TextView) getRootView().findViewById(id)).getPaint();
            paint.getTextBounds(text, 0, text.length(), rect);
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY));
    }

    @Override
    public int getBaseline() {
        return getHeight();
    }

}