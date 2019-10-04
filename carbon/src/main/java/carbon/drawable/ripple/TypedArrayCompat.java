package carbon.drawable.ripple;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;

import java.lang.reflect.Field;
import java.util.Arrays;

import carbon.Carbon;

class TypedArrayCompat {
    private static final int[] TEMP_ARRAY = new int[1];

    private final static ITypedArray IMPL;

    static {
        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            IMPL = new TypedArrayLollipop();
        } else {
            IMPL = new BaseTypedArray();
        }
    }

    /**
     * Retrieve the ColorStateList for the attribute at <var>index</var>. The value may be either a
     * single solid color or a reference to a color or complex {@link ColorStateList} description.
     *
     * @param index Index of attribute to retrieve.
     * @return ColorStateList for the attribute, or null if not defined.
     */
    public static ColorStateList getColorStateList(Resources.Theme theme, TypedArray a,
                                                   TypedValue[] values, int index) {
        if (values != null && theme != null) {
            TypedValue v = values[index];

            if (v.type == TypedValue.TYPE_ATTRIBUTE) {
                TEMP_ARRAY[0] = v.data;
                TypedArray tmp = theme.obtainStyledAttributes(null, TEMP_ARRAY, 0, 0);
                try {
                    return tmp.getColorStateList(0);
                } finally {
                    tmp.recycle();
                }
            }
        }

        if (a != null) {
            return a.getColorStateList(index);
        }

        return null;
    }

    /**
     * Return a mask of the configuration parameters for which the values in this typed array may
     * change.
     *
     * @return Returns a mask of the changing configuration parameters, as defined by {@link
     * android.content.pm.ActivityInfo}.
     * @see android.content.pm.ActivityInfo
     */
    public static int getChangingConfigurations(TypedArray array) {
        return IMPL.getChangingConfigurations(array);
    }

    /**
     * Retrieve the Drawable for the attribute at <var>index</var>.
     *
     * @param index Index of attribute to retrieve.
     * @return Drawable for the attribute, or null if not defined.
     */
    public static Drawable getDrawable(Resources.Theme theme, TypedArray a, TypedValue[] values, int index) {

        if (values != null && theme != null) {
            TypedValue v = values[index];

            if (v.type == TypedValue.TYPE_ATTRIBUTE) {
                TEMP_ARRAY[0] = v.data;
                TypedArray tmp = theme.obtainStyledAttributes(null, TEMP_ARRAY, 0, 0);
                try {
                    return tmp.getDrawable(0);
                } finally {
                    tmp.recycle();
                }
            }
        }

        if (a != null) {
            return LollipopDrawablesCompat.getDrawable(a, index, theme);
        }

        return null;
    }

    /**
     * Retrieve the resource identifier for the attribute at
     * <var>index</var>.  Note that attribute resource as resolved when
     * the overall {@link TypedArray} object is retrieved.  As a result, this function will return
     * the resource identifier of the final resource value that was found, <em>not</em> necessarily
     * the original resource that was specified by the attribute.
     *
     * @param index Index of attribute to retrieve.
     * @param def   Value to return if the attribute is not defined or not a resource.
     * @return Attribute resource identifier, or defValue if not defined.
     */
    public static int getResourceId(Resources.Theme theme, TypedArray a, TypedValue[] values, int index, int def) {
        if (values != null && theme != null) {
            TypedValue v = values[index];
            if (v.type == TypedValue.TYPE_ATTRIBUTE) {
                TEMP_ARRAY[0] = v.data;
                TypedArray tmp = theme.obtainStyledAttributes(null, TEMP_ARRAY, 0, 0);
                try {
                    return tmp.getResourceId(0, def);
                } finally {
                    tmp.recycle();
                }
            }
        }

        if (a != null) {
            return a.getResourceId(index, def);
        }

        return def;
    }

    /**
     * Retrieve a dimensional unit attribute at <var>index</var> for use as an offset in raw pixels.
     * This is the same as {@link TypedArray#getDimension}, except the returned value is converted
     * to integer pixels for you.  An offset conversion involves simply truncating the base value to
     * an integer.
     * <p/>
     * Retrieve from extracted first if no value than tries from {@link TypedArray}
     *
     * @param index Index of attribute to retrieve.
     * @param def   Value to return if the attribute is not defined or not a resource.
     * @return Attribute dimension value multiplied by the appropriate metric and truncated to
     * integer pixels, or defValue if not defined.
     * @see TypedArray#getDimension
     * @see TypedArray#getDimensionPixelSize
     */
    public static int getDimensionPixelOffset(Resources.Theme theme, TypedArray a, TypedValue[] values,
                                              int index, int def) {
        if (values != null && theme != null) {
            TypedValue v = values[index];

            if (v.type == TypedValue.TYPE_ATTRIBUTE) {
                TEMP_ARRAY[0] = v.data;
                TypedArray tmp = theme.obtainStyledAttributes(null, TEMP_ARRAY, 0, 0);
                try {
                    return tmp.getDimensionPixelOffset(0, def);
                } finally {
                    tmp.recycle();
                }
            }
        }

        if (a != null) {
            return a.getDimensionPixelOffset(index, def);
        }

        return def;
    }

    /*package*/ static final int STYLE_NUM_ENTRIES = 6;
    /*package*/ static final int STYLE_TYPE = 0;
    /*package*/ static final int STYLE_DATA = 1;
    /*package*/ static final int STYLE_ASSET_COOKIE = 2;
    /*package*/ static final int STYLE_RESOURCE_ID = 3;
    /*package*/ static final int STYLE_CHANGING_CONFIGURATIONS = 4;
    /*package*/ static final int STYLE_DENSITY = 5;

    /**
     * Extracts theme attributes from a typed array for later resolution using {@link
     * Resources.Theme#resolveAttributes(int[], int[])}. Removes the entries from the typed array so
     * that subsequent calls to typed getters will return the default value without crashing.
     *
     * @return an array of length {@link TypedArray#getIndexCount()} populated with theme
     * attributes, or null if there are no theme attributes in the typed array
     */
    public static int[] extractThemeAttrs(TypedArray array) {
        int[] scrap = null;
        /*if (mRecycled) {
            throw new RuntimeException("Cannot make calls to a recycled instance!");
        }*/

        int[] attrs = null;
        int[] mData = null;

        try {
            Field mDataField = array.getClass().getDeclaredField("mData");
            mDataField.setAccessible(true);
            mData = (int[]) mDataField.get(array);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        final int[] data = mData;
        final int N = array.length();
        for (int i = 0; i < N; i++) {
            final int index = i * STYLE_NUM_ENTRIES;
            if (data[index + STYLE_TYPE] != TypedValue.TYPE_ATTRIBUTE) {
                // Not an attribute, ignore.
                continue;
            }

            // Null the entry so that we can safely call getZzz().
            data[index + STYLE_TYPE] = TypedValue.TYPE_NULL;

            final int attr = data[index + STYLE_DATA];
            if (attr == 0) {
                // Useless data, ignore.
                continue;
            }

            // Ensure we have a usable attribute array.
            if (attrs == null) {
                if (scrap != null && scrap.length == N) {
                    attrs = scrap;
                    Arrays.fill(attrs, 0);
                } else {
                    attrs = new int[N];
                }
            }

            attrs[i] = attr;
        }

        return attrs;
    }


    interface ITypedArray {
        int getChangingConfigurations(TypedArray array);
    }

    static class BaseTypedArray implements ITypedArray {

        @Override
        public int getChangingConfigurations(TypedArray array) {
            return 0;
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static class TypedArrayLollipop extends BaseTypedArray {

        @Override
        public int getChangingConfigurations(TypedArray array) {
            return array.getChangingConfigurations();
        }
    }
}
