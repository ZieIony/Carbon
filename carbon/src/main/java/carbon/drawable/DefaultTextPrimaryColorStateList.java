package carbon.drawable;

import android.content.Context;
import android.content.res.ColorStateList;

import carbon.Carbon;
import carbon.R;

public class DefaultTextPrimaryColorStateList extends ColorStateList {
    public DefaultTextPrimaryColorStateList(Context context) {
        super(new int[][]{
                new int[]{R.attr.carbon_state_invalid},
                new int[]{-android.R.attr.state_enabled},
                new int[]{}
        }, new int[]{
                Carbon.getThemeColor(context, R.attr.carbon_colorError),
                Carbon.getThemeColor(context, android.R.attr.textColorTertiary),
                Carbon.getThemeColor(context, android.R.attr.textColorPrimary)
        });
    }
}
