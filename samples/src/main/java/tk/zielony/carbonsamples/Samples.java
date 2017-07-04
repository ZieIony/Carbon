package tk.zielony.carbonsamples;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import carbon.internal.DebugOverlay;
import carbon.widget.CheckBox;
import carbon.widget.ImageView;
import carbon.widget.Toolbar;

public class Samples {
    private Samples() {
    }

    public static void initToolbar(final Activity activity, String title) {
        initToolbar(activity, title, true);
    }

    public static void initToolbar(final Activity activity, String title, boolean showBack) {
        final DebugOverlay overlay = new DebugOverlay(activity);

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setIconVisible(showBack);

        final ImageView debug = activity.findViewById(R.id.debug);
        if (debug != null) {
            debug.setOnClickListener(new View.OnClickListener() {
                boolean debugEnabled = false;

                @Override
                public void onClick(View view) {
                    if (!debugEnabled) {
                        overlay.show();
                        debugEnabled = true;
                    } else {
                        overlay.dismiss();
                        debugEnabled = false;
                    }
                }
            });
        }

        CheckBox checkBox = activity.findViewById(R.id.enabled);
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener((compoundButton, checked) -> {
                List<View> views = new ArrayList<>();
                views.add(activity.getWindow().getDecorView().getRootView());
                while (!views.isEmpty()) {
                    View v = views.remove(0);
                    if (v.getId() == R.id.toolbar)
                        continue;
                    v.setEnabled(checked);
                    if (v instanceof ViewGroup) {
                        ViewGroup viewGroup = (ViewGroup) v;
                        for (int i = 0; i < viewGroup.getChildCount(); i++)
                            views.add(viewGroup.getChildAt(i));
                    }
                }
            });
        }
    }

    public static Spannable colorSyntax(String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);

        int quotedTextColor = Color.GREEN;
        int keywordColor = Color.BLUE;

        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            builder.setSpan(new ForegroundColorSpan(quotedTextColor), matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        String[] keyword = new String[]{"wrap_content", "match_parent"};
        Stream.of(keyword).forEach(k -> {
            int index = 0;
            while (index < text.length()) {
                index = text.indexOf(k, index);
                builder.setSpan(new ForegroundColorSpan(keywordColor), index, index + k.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                index += k.length();
            }
        });
        return builder;
    }
}
