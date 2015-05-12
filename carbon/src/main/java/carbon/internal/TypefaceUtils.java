package carbon.internal;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

public class TypefaceUtils {
    public static HashMap<String, Typeface> typefaceCache = new HashMap<>();

    public static Typeface getTypeface(Context context, String path) {
        if (typefaceCache.containsKey(path)) {
            return typefaceCache.get(path);
        } else {
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), path);
            typefaceCache.put(path, typeface);
            return typeface;
        }
    }
}
