package carbon.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import carbon.internal.MathUtils;
import carbon.internal.WeakHashSet;
import carbon.view.Corners;
import carbon.view.CornersView;

public class ShadowGenerator {
    private static Object renderScript;
    private static Object blurShader;
    private static Paint paint = new Paint();
    private static boolean software = false;

    private static WeakHashSet shadowSet = new WeakHashSet();

    private static void blur(Bitmap bitmap, float radius) {
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
        elevation = MathUtils.constrain(elevation, 0, 25);

        if (!software && renderScript == null) {
            try {
                renderScript = RenderScript.create(view.getContext());
                blurShader = ScriptIntrinsicBlur.create((RenderScript) renderScript, Element.U8_4((RenderScript) renderScript));
            } catch (Error ignore) {
                software = true;
            }
        }

        CornersView cornersView = (CornersView) view;

        int e = (int) Math.ceil(elevation);
        Corners corners = cornersView.getCorners();

        for (Object o : shadowSet) {
            Shadow s = (Shadow) o;
            if (s != null && s.elevation == elevation && s.corners.equals(corners))
                return s;
        }

        float scale = view.getContext().getResources().getDisplayMetrics().density;

        Bitmap bitmap;
        int minWidth = (int) (Math.max(corners.getTopStart(), corners.getBottomStart()) + Math.max(corners.getTopEnd(), corners.getBottomEnd()) + e);
        int minHeight = (int) (Math.max(corners.getTopStart(), corners.getTopEnd()) + Math.max(corners.getBottomStart(), corners.getBottomEnd()) + e);
        int width = e * 2 + minWidth;
        int height = e * 2 + minHeight;
        bitmap = Bitmap.createBitmap((int) (width / scale), (int) (height / scale), Bitmap.Config.ARGB_8888);

        Canvas shadowCanvas = new Canvas(bitmap);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xffffffff);

        Path path = corners.getPath(minWidth, minHeight);
        path.offset(e, e);
        shadowCanvas.scale(1 / scale, 1 / scale);
        shadowCanvas.drawPath(path, paint);

        blur(bitmap, elevation);

        Shadow shadow = new Shadow(Bitmap.createScaledBitmap(bitmap, width, height, true), elevation, corners);
        shadowSet.add(shadow);
        return shadow;
    }
}
