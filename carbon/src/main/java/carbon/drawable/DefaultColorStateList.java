package carbon.drawable;

import android.content.Context;
import android.content.res.ColorStateList;

import carbon.Carbon;
import carbon.R;

/**
 * Created by Marcin on 2015-03-16.
 */
public class DefaultColorStateList extends ColorStateList {
    public DefaultColorStateList(Context context) {
        super(new int[][]{
                new int[]{R.attr.carbon_state_invalid},
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_checked},
                new int[]{android.R.attr.state_activated},
                new int[]{android.R.attr.state_selected},
                new int[]{android.R.attr.state_focused},
                new int[]{}
        }, new int[]{
                Carbon.getThemeColor(context, R.attr.carbon_colorError),
                Carbon.getThemeColor(context, R.attr.carbon_colorDisabled),
                Carbon.getThemeColor(context, R.attr.colorAccent),
                Carbon.getThemeColor(context, R.attr.colorAccent),
                Carbon.getThemeColor(context, R.attr.colorAccent),
                Carbon.getThemeColor(context, R.attr.colorAccent),
                Carbon.getThemeColor(context, R.attr.colorControlNormal)
        });
    }
}
