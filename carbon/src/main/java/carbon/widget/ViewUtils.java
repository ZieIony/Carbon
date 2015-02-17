package carbon.widget;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;

import java.lang.reflect.Field;

import carbon.R;

/**
 * Created by Marcin on 2014-12-10.
 */
public class ViewUtils {
    public static int getColor(ColorDrawable drawable) {
        int color = 0;
        try {
            Field mStateField = drawable.getClass().getDeclaredField("mState");
            mStateField.setAccessible(true);
            Object colorState = mStateField.get(drawable);
            Field mBaseColorField = colorState.getClass().getDeclaredField("mBaseColor");
            mBaseColorField.setAccessible(true);
            color = mBaseColorField.getInt(colorState);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return color;
    }

    public static int getPrimaryColor(Context context) {
        int color = 0;
        try {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
            int theme = packageInfo.applicationInfo.theme;

            TypedArray a = context.getTheme().obtainStyledAttributes(theme, R.styleable.Carbon);
            color = a.getColor(R.styleable.Carbon_carbon_colorPrimary, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return color;
    }
}
