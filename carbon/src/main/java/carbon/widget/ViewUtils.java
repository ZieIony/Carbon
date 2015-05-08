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

}
