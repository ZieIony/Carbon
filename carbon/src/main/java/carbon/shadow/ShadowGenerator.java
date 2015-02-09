package carbon.shadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ShadowGenerator {
    private static RenderScript renderScript;
    private static ScriptIntrinsicBlur blurShader;
    private static Paint paint = new Paint();
    private static LightingColorFilter lightingColorFilter = new LightingColorFilter(0, 0);

    private static Bitmap generateShadow(Context context, Bitmap bitmap, float radius) {
        if (renderScript == null) {
            renderScript = RenderScript.create(context);
            blurShader = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        }

        Allocation inAllocation = Allocation.createFromBitmap(renderScript, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation outAllocation = Allocation.createTyped(renderScript, inAllocation.getType());

        blurShader.setRadius(radius);
        blurShader.setInput(inAllocation);
        blurShader.forEach(outAllocation);

        Bitmap blurredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        outAllocation.copyTo(blurredBitmap);
        return blurredBitmap;
    }

    public static Shadow generateShadow(View view, float elevation) {
        boolean isRect = ((ShadowView) view).isRect();

        int e = (int) Math.ceil(elevation);
        Bitmap blackShape;
        if (isRect) {
            blackShape = Bitmap.createBitmap(e * 4+1, e * 4+1, Bitmap.Config.ARGB_8888);

            Canvas shadowCanvas = new Canvas(blackShape);
            paint.setStyle(Paint.Style.FILL);
            //paint.setXfermode(null);
            paint.setColor(0xff000000);

            shadowCanvas.drawRect(e, e, e * 3+1, e * 3+1, paint);

            Bitmap shadowBitmap = generateShadow(view.getContext(), blackShape, elevation);

            /*shadowCanvas.setBitmap(shadowBitmap);
            paint.setFilterColor(null);
            paint.setXfermode(porterDuffMode);
            shadowCanvas.drawBitmap(blackShape, 0, -elevation / 3, paint);*/

            blackShape.recycle();

            return new NinePatchShadow(shadowBitmap, elevation);
        } else {
            blackShape = Bitmap.createBitmap(view.getWidth() + e * 2, view.getHeight() + e * 2, Bitmap.Config.ARGB_8888);

            Canvas shadowCanvas = new Canvas(blackShape);
            paint.setStyle(Paint.Style.FILL);
            paint.setColorFilter(lightingColorFilter);
            //paint.setXfermode(null);

            if (view.getDrawingCache() != null) {
                shadowCanvas.drawBitmap(view.getDrawingCache(), e, e, paint);
            } else {
                Bitmap cache = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(cache);
                view.draw(canvas);
                shadowCanvas.drawBitmap(cache, e, e, paint);
            }

            Bitmap shadowBitmap = generateShadow(view.getContext(), blackShape, elevation);

            /*shadowCanvas.setBitmap(shadowBitmap);
            paint.setFilterColor(null);
            paint.setXfermode(porterDuffMode);
            shadowCanvas.drawBitmap(blackShape, 0, -elevation / 3, paint);*/

            blackShape.recycle();

            return new Shadow(shadowBitmap, elevation);
        }
    }
}
