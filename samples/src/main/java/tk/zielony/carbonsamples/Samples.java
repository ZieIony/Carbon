package tk.zielony.carbonsamples;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import carbon.Carbon;
import carbon.internal.DebugOverlay;
import carbon.widget.CheckBox;
import carbon.widget.ImageView;
import carbon.widget.Toolbar;

/**
 * Created by Marcin on 2016-03-23.
 */
public class Samples {
    private Samples() {
    }

    public static void initToolbar(final Activity activity, String title) {
        final DebugOverlay overlay = new DebugOverlay(activity);

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setTitle(title);

        final ImageView debug = (ImageView) activity.findViewById(R.id.debug);
        if (debug != null) {
            debug.setTint(Carbon.getThemeColor(activity, R.attr.carbon_colorDisabled));
            debug.setOnClickListener(new View.OnClickListener() {
                boolean debugEnabled = false;

                @Override
                public void onClick(View view) {
                    if (!debugEnabled) {
                        debug.setTint(Carbon.getThemeColor(activity, R.attr.carbon_iconColor));
                        overlay.show();
                        debugEnabled = true;
                    } else {
                        debug.setTint(Carbon.getThemeColor(activity, R.attr.carbon_colorDisabled));
                        overlay.dismiss();
                        debugEnabled = false;
                    }
                }
            });
        }

        CheckBox checkBox = (CheckBox) activity.findViewById(R.id.enabled);
        if (checkBox != null)
            checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
                List<View> views = findViewsWithTag((ViewGroup) activity.getWindow().getDecorView().getRootView(), "enable");
                for (View v : views)
                    v.setEnabled(checked);
            });
    }

    private static List<View> findViewsWithTag(ViewGroup start, Object tag) {
        List<View> result = new ArrayList<>();
        List<ViewGroup> groups = new ArrayList<>();
        groups.add(start);
        while (!groups.isEmpty()) {
            ViewGroup group = groups.remove(0);
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                if (tag.equals(child.getTag()))
                    result.add(child);
                if (child instanceof ViewGroup)
                    groups.add((ViewGroup) child);
            }
        }
        return result;
    }

    public static void setTheme(Context context, int theme, int primaryColor, int accentColor) {
        SharedPreferences preferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        preferences.edit().putInt("theme", theme).putInt("primary", primaryColor).putInt("accent", accentColor).commit();
    }

    public static void applyTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("theme", Context.MODE_PRIVATE);
        context.setTheme(preferences.getInt("theme", R.style.ThemeLight));
        context.getTheme().applyStyle(preferences.getInt("primary", R.style.PrimaryRed), true);
        context.getTheme().applyStyle(preferences.getInt("accent", R.style.AccentBlue), true);
    }
}
