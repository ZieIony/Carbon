package carbon.drawable;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;

import java.lang.reflect.Field;

/**
 * Created by Marcin on 2015-08-21.
 */
public class ColorStateListDrawable extends StateListDrawable {
    public ColorStateListDrawable(ColorStateList list) {
        try {
            Field mStateSpecsField = ColorStateList.class.getDeclaredField("mStateSpecs");
            mStateSpecsField.setAccessible(true);
            int[][] mStateSpecs = (int[][]) mStateSpecsField.get(list);
            Field mColorsField = ColorStateList.class.getDeclaredField("mColors");
            mColorsField.setAccessible(true);
            int[] mColors = (int[]) mColorsField.get(list);

            for (int i = 0; i < mColors.length; i++) {
                addState(mStateSpecs[i], new ColorDrawable(mColors[i]));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
