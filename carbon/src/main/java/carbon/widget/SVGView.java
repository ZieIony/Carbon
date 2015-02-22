package carbon.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.util.AttributeSet;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import carbon.R;

/**
 * Created by Marcin on 2014-12-02.
 */
public class SVGView extends ImageView {
    private static final String TAG = SVGView.class.getSimpleName();
    private int svgId;
    private Bitmap bitmap;

    public SVGView(Context context) {
        super(context);
        init(null, R.attr.carbon_svgViewStyle);
    }

    public SVGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, R.attr.carbon_svgViewStyle);
    }

    public SVGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SVGView, defStyleAttr, 0);
        svgId = a.getResourceId(R.styleable.SVGView_carbon_src, 0);
        int filterColor = a.getColor(R.styleable.SVGView_carbon_filterColor, 0);
        if (filterColor != 0)
            setColorFilter(new LightingColorFilter(0, filterColor));
        a.recycle();
    }

    public void setSVGResource(int svgId) {
        if (this.svgId == svgId)
            return;
        this.svgId = svgId;
        render();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (isInEditMode() || svgId == 0)
            return;

        if (bitmap == null || changed && bitmap.getWidth() != right - left && bitmap.getHeight() != bottom - top)
            render();
    }

    private synchronized void render() {
        if (svgId == 0 || getWidth() == 0)
            return;
        try {
            SVG svg = SVG.getFromResource(getContext(), svgId);
            bitmap = Bitmap.createBitmap(getWidth() - getPaddingLeft() - getPaddingRight(),
                    getHeight() - getPaddingTop() - getPaddingBottom(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            svg.setDocumentWidth(bitmap.getWidth());
            svg.setDocumentHeight(bitmap.getHeight());
            svg.renderToCanvas(canvas);
            setImageBitmap(bitmap);
        } catch (SVGParseException e) {
            Log.e(TAG, "problem with the svg", e);
        } catch (NullPointerException e) {
            Log.e(TAG, "problem with the resource", e);
        }
    }
}
