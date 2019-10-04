package carbon;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.collection.LongSparseArray;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import carbon.drawable.VectorDrawable;
import carbon.drawable.ripple.LollipopDrawable;
import carbon.drawable.ripple.RippleDrawableICS;

public class CarbonResources extends Resources {
    private final Object mAccessLock = new Object();

    private final Map<String, Class<? extends Drawable>> CLASS_MAP = new HashMap<>();
    private final LongSparseArray<WeakReference<Drawable.ConstantState>> sDrawableCache = new LongSparseArray<>();

    private final LongSparseArray<WeakReference<Drawable.ConstantState>> sColorDrawableCache = new LongSparseArray<>();

    private final IDrawable IMPL;
    private final Context context;

    /**
     * Create a new Resources object on top of an existing set of assets in an AssetManager.
     *
     * @param assets  Previously created AssetManager.
     * @param metrics Current display metrics to consider when selecting/computing resource values.
     * @param config  Desired device configuration to consider when
     */
    public CarbonResources(Context context, AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
        this.context = context;
        registerDrawable(RippleDrawableICS.class, "ripple");

        if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
            IMPL = new LollipopDrawableImpl();
        } else {
            IMPL = new BaseDrawableImpl();
        }
    }

    public void registerDrawable(Class<? extends Drawable> clazz, String name) {
        if (name == null || clazz == null) {
            throw new NullPointerException("Class: " + clazz + ". Name: " + name);
        }
        CLASS_MAP.put(name, clazz);
    }

    public void unregisterDrawable(String name) {
        CLASS_MAP.remove(name);
    }

    /**
     * Applies the specified theme to this Drawable and its children.
     */
    public void applyTheme(Drawable d, Resources.Theme t) {
        IMPL.applyTheme(d, t);
    }

    public boolean canApplyTheme(Drawable d) {
        return IMPL.canApplyTheme(d);
    }

    /**
     * Create a drawable from an inputstream
     */
    public Drawable createFromStream(InputStream is, String srcName) {
        return createFromResourceStream(null, is, srcName);
    }

    /**
     * Create a drawable from an inputstream, using the given resources and value to determine
     * density information.
     */
    public Drawable createFromResourceStream(TypedValue value, InputStream is, String srcName) {
        return createFromResourceStream(value, is, srcName, null);
    }

    /**
     * Create a drawable from an inputstream, using the given resources and value to determine
     * density information.
     */
    public Drawable createFromResourceStream(TypedValue value, InputStream is, String srcName, BitmapFactory.Options opts) {
        return Drawable.createFromResourceStream(this, value, is, srcName, opts);
    }


    /**
     * Create a drawable from an XML document. For more information on how to create resources in
     * XML, see
     * <a href="{@docRoot}guide/topics/resources/drawable-resource.html">Drawable Resources</a>.
     */
    public Drawable createFromXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        return createFromXml(parser, null);
    }

    /**
     * Create a drawable from an XML document using an optional {@link Resources.Theme}. For more
     * information on how to create resources in XML, see
     * <a href="{@docRoot}guide/topics/resources/drawable-resource.html">Drawable Resources</a>.
     */
    public Drawable createFromXml(XmlPullParser parser, Resources.Theme theme) throws XmlPullParserException, IOException {
        AttributeSet attrs = Xml.asAttributeSet(parser);

        int type;
        while ((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
            // Empty loop
        }

        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }

        Drawable drawable = createFromXmlInner(parser, attrs, theme);

        if (drawable == null) {
            throw new RuntimeException("Unknown initial tag: " + parser.getName());
        }

        return drawable;
    }


    /**
     * Create a drawable from inside an XML document using an optional {@link Resources.Theme}.
     * Called on a parser positioned at a tag in an XML document, tries to create a Drawable from
     * that tag. Returns {@code null} if the tag is not a valid drawable.
     */
    public Drawable createFromXmlInner(XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        Drawable drawable = null;
        final String name = parser.getName();
        try {
            Class<? extends Drawable> clazz = CLASS_MAP.get(name);
            if (clazz != null) {
                drawable = clazz.newInstance();
            } else if (name.indexOf('.') > 0) {
                drawable = (Drawable) Class.forName(name).newInstance();
            }
        } catch (Exception e) {
            throw new XmlPullParserException("Error while inflating drawable resource", parser, e);
        }
        if (drawable == null) {
            if (Carbon.IS_LOLLIPOP_OR_HIGHER) {
                return Drawable.createFromXmlInner(this, parser, attrs, theme);
            } else {
                return Drawable.createFromXmlInner(this, parser, attrs);
            }
        }
        IMPL.inflate(drawable, this, parser, attrs, theme);
        return drawable;
    }

    private Drawable getCachedDrawable(LongSparseArray<WeakReference<Drawable.ConstantState>> cache, long key) {
        synchronized (mAccessLock) {
            WeakReference<Drawable.ConstantState> wr = cache.get(key);
            if (wr != null) {
                Drawable.ConstantState entry = wr.get();
                if (entry != null) {
                    return entry.newDrawable(this);
                } else {
                    cache.delete(key);
                }
            }
        }
        return null;
    }

    public Drawable getDrawable(int resId, Resources.Theme theme) {
        if (resId != 0 && getResourceTypeName(resId).equals("raw")) {
            return new VectorDrawable(this, resId);
        } else {
            TypedValue value = new TypedValue();
            getValue(resId, value, true);
            return loadDrawable(value, theme);
        }
    }

    public Drawable getDrawable(int resId) {
        if (resId != 0 && getResourceTypeName(resId).equals("raw")) {
            return new VectorDrawable(this, resId);
        } else {
            TypedValue value = new TypedValue();
            getValue(resId, value, true);
            return loadDrawable(value, null);
        }
    }


    public Drawable getDrawable(TypedArray array, int index, Resources.Theme theme) {
        TypedValue value = new TypedValue();
        array.getValue(index, value);
        return loadDrawable(value, theme);
    }

    public Drawable getDrawable(TypedArray array, int index) {
        return getDrawable(array, index, null);
    }

    public Drawable loadDrawable(TypedValue value, Resources.Theme theme) throws Resources.NotFoundException {
        if (value == null || value.resourceId == 0) {
            return null;
        }

        final boolean isColorDrawable;
        final LongSparseArray<WeakReference<Drawable.ConstantState>> cache;
        final long key;

        if (value.type >= TypedValue.TYPE_FIRST_COLOR_INT && value.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            isColorDrawable = true;
            cache = sColorDrawableCache;
            key = value.data;
        } else {
            isColorDrawable = false;
            cache = sDrawableCache;
            key = (long) value.assetCookie << 32 | value.data;
        }

        Drawable dr = getCachedDrawable(cache, key);
        if (dr != null) {
            return dr;
        }

        Drawable.ConstantState cs = null;

        //TODO hm do i need implement preloading of drawables?

        if (cs != null) {
            final Drawable cloneDr = cs.newDrawable(this);
            if (theme != null) {
                dr = cloneDr.mutate();
                applyTheme(dr, theme);
            } else {
                dr = cloneDr;
            }
        } else if (isColorDrawable) {
            dr = new ColorDrawable(value.data);
        } else {
            dr = loadDrawableForCookie(value, value.resourceId, theme);
        }

        if (dr != null) {
            dr.setChangingConfigurations(value.changingConfigurations);
            cacheDrawable(value, theme, isColorDrawable, key, dr, cache);
        }
        return dr;
    }

    private void cacheDrawable(TypedValue value, Resources.Theme theme, boolean isColorDrawable, long key, Drawable drawable, LongSparseArray<WeakReference<Drawable.ConstantState>> caches) {

        Drawable.ConstantState cs = drawable.getConstantState();
        if (cs == null) {
            return;
        }

        synchronized (mAccessLock) {
            caches.put(key, new WeakReference<>(cs));
        }
    }

    private Drawable loadDrawableForCookie(TypedValue value, int id, Resources.Theme theme) {
        if (value.string == null)
            throw new Resources.NotFoundException("Resource \"" + getResourceName(id) + "\" (" + Integer.toHexString(id) + ")  is not a Drawable (color or path): " + value);

        String file = value.string.toString();

        Drawable dr = null;

        if (file.endsWith(".xml")) {
            try {
                XmlResourceParser rp = getAssets().openXmlResourceParser(value.assetCookie, file);
                dr = createFromXml(rp, theme);
                rp.close();
            } catch (Exception e) {
                try {
                    dr = AppCompatResources.getDrawable(context.getApplicationContext(), id);
                } catch (Exception e2) {
                    Log.w(CarbonResources.class.getSimpleName(), "Failed to load drawable resource", e);
                }
            }

        } else {
            try {
                InputStream is = getAssets().openNonAssetFd(value.assetCookie, file).createInputStream();
                dr = createFromResourceStream(value, is, file, null);
                is.close();
            } catch (Exception e) {
                try {
                    dr = AppCompatResources.getDrawable(context.getApplicationContext(), id);
                } catch (Exception e2) {
                    Log.w(CarbonResources.class.getSimpleName(), "Failed to load drawable resource", e);
                }
            }
        }

        return dr;
    }

    /**
     * Create a drawable from file path name.
     */
    public Drawable createFromPath(String pathName) {
        return Drawable.createFromPath(pathName);
    }

    private interface IDrawable {
        void applyTheme(Drawable drawable, Resources.Theme t);

        boolean canApplyTheme(Drawable drawable);

        void inflate(Drawable drawable, Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException;
    }

    static class BaseDrawableImpl implements IDrawable {

        @Override
        public void applyTheme(Drawable drawable, Resources.Theme t) {
            if (drawable instanceof LollipopDrawable) {
                ((LollipopDrawable) drawable).applyTheme(t);
            }
        }

        @Override
        public boolean canApplyTheme(Drawable drawable) {
            return drawable instanceof LollipopDrawable && ((LollipopDrawable) drawable).canApplyTheme();
        }

        @Override
        public void inflate(Drawable drawable, Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {

            if (drawable instanceof LollipopDrawable) {
                ((LollipopDrawable) drawable).inflate(r, parser, attrs, theme);
                return;
            }

            drawable.inflate(r, parser, attrs);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static class LollipopDrawableImpl extends BaseDrawableImpl {

        @Override
        public void applyTheme(Drawable drawable, Resources.Theme t) {
            drawable.applyTheme(t);
        }

        @Override
        public boolean canApplyTheme(Drawable drawable) {
            return drawable.canApplyTheme();
        }

        @Override
        public void inflate(Drawable drawable, Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
            drawable.inflate(r, parser, attrs, theme);
        }
    }

    @Nullable
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Drawable getDrawableForDensity(int resId, int density, Theme theme) {
        if (resId != 0 && getResourceTypeName(resId).equals("raw")) {
            return new VectorDrawable(this, resId);
        } else {
            return super.getDrawableForDensity(resId, density, theme);
        }
    }

    @Nullable
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Drawable getDrawableForDensity(int resId, int density) throws NotFoundException {
        if (resId != 0 && getResourceTypeName(resId).equals("raw")) {
            return new VectorDrawable(this, resId);
        } else {
            return super.getDrawableForDensity(resId, density);
        }
    }

}
