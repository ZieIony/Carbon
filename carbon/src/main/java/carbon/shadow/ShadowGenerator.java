package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RSRuntimeException;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

public class ShadowGenerator {
    public static final float SHADOW_SCALE = 2.0f;

    private static RenderScript renderScript;
    private static ScriptIntrinsicBlur blurShader;
    private static Paint paint = new Paint();
    private static LightingColorFilter lightingColorFilter = new LightingColorFilter(0, 0);
    private static boolean software = false;

    private static void blur(Bitmap bitmap, float radius) {
        radius = Math.max(0,Math.min(radius,25));
        if (software) {
            blurSoftware(bitmap, radius);
        } else {
            blurRenderScript(bitmap, radius);
        }
    }

    private static void blurSoftware(Bitmap bitmap, float radius) {
        int[] pixels = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int[] halfResult = new int[bitmap.getWidth() * bitmap.getHeight()];
        int rad = (int) Math.ceil(radius);
        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int sumBlack = 0, sumAlpha = 0;
                for (int i = -rad; i <= rad; i++) {
                    int pixel = pixels[y * bitmap.getWidth() + Math.max(0, Math.min(x + i, bitmap.getWidth() - 1))];
                    sumBlack += pixel & 0xff;
                    sumAlpha += (pixel >> 24) & 0xff;
                }
                int blurredBlack = sumBlack / (rad * 2 + 1);
                int blurredAlpha = sumAlpha / (rad * 2 + 1);
                halfResult[y * bitmap.getWidth() + x] = Color.argb(blurredAlpha, blurredBlack, blurredBlack, blurredBlack);
            }
        }
        for (int x = 0; x < bitmap.getWidth(); x++) {
            for (int y = 0; y < bitmap.getHeight(); y++) {
                int sumBlack = 0, sumAlpha = 0;
                for (int i = -rad; i <= rad; i++) {
                    int pixel = halfResult[Math.max(0, Math.min(y + i, bitmap.getHeight() - 1)) * bitmap.getHeight() + x];
                    sumBlack += pixel & 0xff;
                    sumAlpha += (pixel >> 24) & 0xff;
                }
                int blurredBlack = sumBlack / (rad * 2 + 1);
                int blurredAlpha = sumAlpha / (rad * 2 + 1);
                pixels[y * bitmap.getWidth() + x] = Color.argb(blurredAlpha, blurredBlack, blurredBlack, blurredBlack);
            }
        }
        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    private static void blurRenderScript(Bitmap bitmap, float radius) {
        Allocation inAllocation = Allocation.createFromBitmap(renderScript, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation outAllocation = Allocation.createTyped(renderScript, inAllocation.getType());

        blurShader.setRadius(radius);
        blurShader.setInput(inAllocation);
        blurShader.forEach(outAllocation);

        outAllocation.copyTo(bitmap);
    }

    public static Shadow generateShadow(View view, float elevation) {
        if (!software && renderScript == null) {
            try {
                renderScript = RenderScript.create(view.getContext());
                blurShader = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
            } catch (RSRuntimeException ignore) {
                software = true;
            }
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
