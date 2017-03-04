package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import carbon.internal.MathUtils;
import carbon.internal.WeakHashSet;
import carbon.widget.CornerView;

public class ShadowGenerator {
    private static Object renderScript;
    private static Object blurShader;
    private static Paint paint = new Paint();
    private static boolean software = false;
    private static RectF roundRect = new RectF();

    private static WeakHashSet shadowSet = new WeakHashSet();

    private static void blur(Bitmap bitmap, float radius) {
        radius = MathUtils.constrain(radius, 0, 25);
        if (software) {
            blurSoftware(bitmap, radius);
        } else {
            blurRenderScript(bitmap, radius);
        }
    }

    private static void blurSoftware(Bitmap bitmap, float radius) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] halfResult = new int[width * height];
        int rad = (int) Math.ceil(radius);
        int rad2plus1 = rad * 2 + 1;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sumBlack = 0, sumAlpha = 0;
                for (int i = -rad; i <= rad; i++) {
                    int pixel = pixels[y * width + MathUtils.constrain(x + i, 0, width - 1)];
                    sumBlack += pixel & 0xff;
                    sumAlpha += (pixel >> 24) & 0xff;
                }
                int blurredBlack = sumBlack / rad2plus1;
                int blurredAlpha = sumAlpha / rad2plus1;
                halfResult[y * width + x] = Color.argb(blurredAlpha, blurredBlack, blurredBlack, blurredBlack);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int sumBlack = 0, sumAlpha = 0;
                for (int i = -rad; i <= rad; i++) {
                    int pixel = halfResult[MathUtils.constrain(y + i, 0, height - 1) * height + x];
                    sumBlack += pixel & 0xff;
                    sumAlpha += (pixel >> 24) & 0xff;
                }
                int blurredBlack = sumBlack / rad2plus1;
                int blurredAlpha = sumAlpha / rad2plus1;
                pixels[y * width + x] = Color.argb(blurredAlpha, blurredBlack, blurredBlack, blurredBlack);
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    private static void blurRenderScript(Bitmap bitmap, float radius) {
        Allocation inAllocation = Allocation.createFromBitmap((RenderScript) renderScript, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        Allocation outAllocation = Allocation.createTyped((RenderScript) renderScript, inAllocation.getType());

        ((ScriptIntrinsicBlur) blurShader).setRadius(radius);
        ((ScriptIntrinsicBlur) blurShader).setInput(inAllocation);
        ((ScriptIntrinsicBlur) blurShader).forEach(outAllocation);

        outAllocation.copyTo(bitmap);
    }

    public static Shadow generateShadow(View view, float elevation) {
        if (!software && renderScript == null) {
            try {
                renderScript = RenderScript.create(view.getContext());
                blurShader = ScriptIntrinsicBlur.create((RenderScript) renderScript, Element.U8_4((RenderScript) renderScript));
            } catch (Error ignore) {
                software = true;
            }
        }

        CornerView cornerView = (CornerView) view;

        int e = (int) Math.ceil(elevation);
        int c = (int) Math.max(e, cornerView.getCornerRadius());

        for (Object o : shadowSet) {
            Shadow s = (Shadow) o;
            if (s != null && s.elevation == elevation && s.c == c)
                return s;
        }

        Bitmap bitmap;
        int bitmapSize = e * 2 + 2 * c + 1;
        bitmap = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888);

        Canvas shadowCanvas = new Canvas(bitmap);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xffffffff);

        roundRect.set(e, e, bitmapSize - e, bitmapSize - e);
        shadowCanvas.drawRoundRect(roundRect, c, c, paint);

        blur(bitmap, elevation);

        Shadow shadow = new Shadow(bitmap, elevation, c);
        shadowSet.add(shadow);
        return shadow;
    }
}
