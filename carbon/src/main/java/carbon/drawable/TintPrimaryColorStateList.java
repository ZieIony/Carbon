package carbon.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.TypedValue;

import carbon.Carbon;
import carbon.R;

/**
 * Created by Marcin on 2015-03-16.
 */
public class TintPrimaryColorStateList extends ColorStateList {
    public TintPrimaryColorStateList(Context context) {
        super(new int[][]{
                new int[]{}
        }, new int[]{
                Carbon.getThemeColor(context, R.attr.colorPrimary)
        });
    }

    /**
     * @param context context
     * @param attr    attribute to get from the current theme
     * @return color from the current theme
     * @deprecated use {@link carbon.Carbon#getThemeColor(Context, int)} instead. This method was duplicated in all ColorStateList implementations
     */
    @Deprecated
    public static int getThemeColor(Context context, int attr) {
        return Carbon.getThemeColor(context, attr);
    }
}
