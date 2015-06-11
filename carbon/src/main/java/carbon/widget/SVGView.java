package carbon.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import carbon.Carbon;
import carbon.R;

/**
 * Created by Marcin on 2014-12-02.
 */
public class SVGView extends ImageView {
    private static final String TAG = SVGView.class.getSimpleName();
    private Bitmap bitmap;
    private ColorStateList filterColor;
    private int svgId;
    private SVG svg;

    public SVGView(Context context) {
        this(context, null);
    }

    public SVGView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.carbon_svgViewStyle);
    }

    public SVGView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SVGView, defStyleAttr, 0);
        setSVGResource(a.getResourceId(R.styleable.SVGView_carbon_src, 0));
        a.recycle();
    }

    public void setSVGResource(int svgId) {
        if (this.svgId == svgId)
            return;
        this.svgId = svgId;
        if (svgId == 0) {
            setImageBitmap(null);
            return;
        }
        try {
            svg = SVG.getFromResource(getContext(), svgId);
        } catch (SVGParseException e) {
            Log.e(TAG, "problem with the svg", e);
        }

        render();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY && svg != null) {
            width = (int) (svg.getDocumentWidth() * getResources().getDimension(R.dimen.carbon_1dip));
            width += getPaddingLeft() + getPaddingRight();
        }
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY && svg != null) {
            height = (int) (svg.getDocumentHeight() * getResources().getDimension(R.dimen.carbon_1dip));
            height += getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (isInEditMode())
            return;

        if (svgId != 0 && bitmap == null || bitmap != null && changed &&
                bitmap.getWidth() != right - left - getPaddingLeft() - getPaddingRight() &&
                bitmap.getHeight() != bottom - top - getPaddingTop() - getPaddingBottom())
            render();
    }

    private synchronized void render() {
        if (svgId == 0)
            return;
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        if (width <= 0 || height <= 0)
            return;
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        svg.setDocumentWidth(width);
        svg.setDocumentHeight(height);
        svg.renderToCanvas(canvas);
        setImageBitmap(bitmap);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        bitmap = null;
        svgId = 0;
        svg = null;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        bitmap = null;
        svgId = 0;
        svg = null;
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        bitmap = null;
        svgId = 0;
        svg = null;
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        bitmap = null;
        svgId = 0;
        svg = null;
    }
}
