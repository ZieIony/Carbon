package carbon.drawable;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.TypedValue;

import carbon.R;

/**
 * Created by Marcin on 2015-03-16.
 */
public class TintPrimaryColorStateList extends ColorStateList {
    public TintPrimaryColorStateList(Context context) {
        super(new int[][]{
                new int[]{}
        }, new int[]{
                getThemeColor(context, R.attr.colorPrimary)
        });
    }

    public static int getThemeColor(Context context, int attr) {
        Resources.Theme theme = context.getTheme();
        TypedValue typedvalueattr = new TypedValue();
        theme.resolveAttribute(attr, typedvalueattr, true);
        return typedvalueattr.resourceId != 0 ? context.getResources().getColor(typedvalueattr.resourceId) : typedvalueattr.data;
    }
}
