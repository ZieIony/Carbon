package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

public class ShadowGenerator {
    public static final float SHADOW_SCALE = 2.0f;

    private static RenderScript renderScript;
    private static ScriptIntrinsicBlur blurShader;
    private static Paint paint = new Paint();
    private static LightingColorFilter lightingColorFilter = new LightingColorFilter(0, 0);

    private static void blur(Bitmap bitmap, float radius) {
        Allocation inAllocation = Allocation.createFromBitmap(renderScript, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation outAllocation = Allocation.createTyped(renderScript, inAllocation.getType());

        blurShader.setRadius(radius);
        blurShader.setInput(inAllocation);
        blurShader.forEach(outAllocation);

        outAllocation.copyTo(bitmap);
    }

    public static Shadow generateShadow(View view, float elevation) {
        if (renderScript == null) {
            renderScript = RenderScript.create(view.getContext());
            blurShader = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        }

        boolean isRect = ((ShadowView) view).isRect();

        int e = (int) Math.ceil(elevation);
        Bitmap bitmap;
        if (isRect) {
            bitmap = Bitmap.createBitmap(e * 4 + 1, e * 4 + 1, Bitmap.Config.ARGB_8888);

            Canvas shadowCanvas = new Canvas(bitmap);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xff000000);

            shadowCanvas.drawRect(e, e, e * 3 + 1, e * 3 + 1, paint);

            blur(bitmap, elevation);

            return new NinePatchShadow(bitmap, elevation);
        } else {
            bitmap = Bitmap.createBitmap((int) (view.getWidth() / SHADOW_SCALE + e * 2), (int) (view.getHeight() / SHADOW_SCALE + e * 2), Bitmap.Config.ARGB_8888);

            Canvas shadowCanvas = new Canvas(bitmap);
            paint.setStyle(Paint.Style.FILL);
            paint.setColorFilter(lightingColorFilter);

            if (view.getDrawingCache() != null) {
                shadowCanvas.drawBitmap(view.getDrawingCache(), e, e, paint);
            } else {
                Bitmap cache = Bitmap.createBitmap((int) (view.getWidth() / ShadowGenerator.SHADOW_SCALE), (int) (view.getHeight() / ShadowGenerator.SHADOW_SCALE), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(cache);
                canvas.scale(1 / SHADOW_SCALE, 1 / SHADOW_SCALE);
                view.draw(canvas);
                shadowCanvas.drawBitmap(cache, e, e, paint);
            }

            blur(bitmap, elevation);

            return new Shadow(bitmap, elevation);
        }
    }
}
