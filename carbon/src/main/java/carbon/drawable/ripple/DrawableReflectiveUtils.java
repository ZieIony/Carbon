package carbon.drawable.ripple;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.v4.util.LruCache;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import java.lang.reflect.Method;

class DrawableReflectiveUtils {
    private final static String TAG = "DrawableReflectiveUtils";

    private static SimpleArrayMap<String, Method> sCachedMethods = new SimpleArrayMap<>();
    private static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);

    private final static Class[] INT_ARG = {int.class};

    @SuppressWarnings("unchecked")
    public static <T> T tryInvoke(Object target, String methodName, Class<?>[] argTypes, Object... args) {

        try {
            Method method = sCachedMethods.get(methodName);
            if (method != null) {
                return (T) method.invoke(target, args);
            }

            method = target.getClass().getDeclaredMethod(methodName, argTypes);
            sCachedMethods.put(methodName, method);

            return (T) method.invoke(target, args);
        } catch (Exception pokemon) {
            Log.e(TAG, "Unable to invoke " + methodName + " on " + target, pokemon);
        }

        return null;
    }

    public static PorterDuffColorFilter setColor(PorterDuffColorFilter cf, int color, PorterDuff.Mode mode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // First, lets see if the cache already contains the color filter
            PorterDuffColorFilter filter = COLOR_FILTER_CACHE.get(color, mode);

            if (filter == null) {
                // Cache miss, so create a color filter and add it to the cache
                filter = new PorterDuffColorFilter(color, mode);
                COLOR_FILTER_CACHE.put(color, mode, filter);
            }

            return filter;
        }

        /**
         * Otherwise invoke native one
         */
        tryInvoke(cf, "setColor", INT_ARG, color);
        return cf;
    }

    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {

        public ColorFilterLruCache(int maxSize) {
            super(maxSize);
        }

        PorterDuffColorFilter get(int color, PorterDuff.Mode mode) {
            return get(generateCacheKey(color, mode));
        }

        PorterDuffColorFilter put(int color, PorterDuff.Mode mode, PorterDuffColorFilter filter) {
            return put(generateCacheKey(color, mode), filter);
        }

        private static int generateCacheKey(int color, PorterDuff.Mode mode) {
            int hashCode = 1;
            hashCode = 31 * hashCode + color;
            hashCode = 31 * hashCode + mode.hashCode();
            return hashCode;
        }
    }
}