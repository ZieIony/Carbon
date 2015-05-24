package carbon.widget;

import android.graphics.drawable.ColorDrawable;

import java.lang.reflect.Field;

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
