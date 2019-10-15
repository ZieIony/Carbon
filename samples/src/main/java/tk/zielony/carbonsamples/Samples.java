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

import carbon.dialog.MultiSelectDialog;
import carbon.drawable.CheckedState;
import carbon.internal.DebugOverlay;
import carbon.widget.CheckBox;
import carbon.widget.ImageView;
import carbon.widget.Toolbar;

public class Samples {
    private Samples() {
    }

    public static void initToolbar(final Activity activity) {
        initToolbar(activity, activity.getTitle().toString(), true);
    }

    public static void initToolbar(final Activity activity, String title) {
        initToolbar(activity, title, true);
    }

    public static void initToolbar(final Activity activity, String title, boolean showBack) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setIconVisible(toolbar.getIcon()!=null&&showBack);

        final DebugOverlay overlay = new DebugOverlay(activity);
        overlay.setDrawBoundsEnabled(false);
        overlay.setDrawGridEnabled(false);
        overlay.setDrawHitRectsEnabled(false);
        overlay.setDrawMarginsEnabled(false);
        overlay.setDrawPaddingsEnabled(false);
        overlay.setDrawRulersEnabled(false);
        overlay.setDrawTextSizesEnabled(false);

        final ImageView debug = activity.findViewById(R.id.debug);
        if (debug != null) {
            debug.setOnClickListener(new View.OnClickListener() {
                boolean debugEnabled = false;
                String[] debugOptions = new String[]{"bounds", "grid", "hit rects", "margins", "paddings", "rulers", "text sizes"};

                @Override
                public void onClick(View view) {
                    MultiSelectDialog<String> listDialog = new MultiSelectDialog<>(activity);
                    listDialog.setItems(debugOptions);
                    listDialog.setTitle("Debug options");
                    List<String> initialItems = new ArrayList<>();
                    if (overlay.isDrawBoundsEnabled())
                        initialItems.add("bounds");
                    if (overlay.isDrawGridEnabled())
                        initialItems.add("grid");
                    if (overlay.isDrawHitRectsEnabled())
                        initialItems.add("hit rects");
                    if (overlay.isDrawMarginsEnabled())
                        initialItems.add("margins");
                    if (overlay.isDrawPaddingsEnabled())
                        initialItems.add("paddings");
                    if (overlay.isDrawRulersEnabled())
                        initialItems.add("rulers");
                    if (overlay.isDrawTextSizesEnabled())
                        initialItems.add("text sizes");
                    listDialog.setSelectedItems(initialItems);
                    listDialog.show();
                    listDialog.setOnDismissListener(dialogInterface -> {
                        List<String> selectedItems = listDialog.getSelectedItems();
                        overlay.setDrawBoundsEnabled(selectedItems.contains("bounds"));
                        overlay.setDrawGridEnabled(selectedItems.contains("grid"));
                        overlay.setDrawHitRectsEnabled(selectedItems.contains("hit rects"));
                        overlay.setDrawMarginsEnabled(selectedItems.contains("margins"));
                        overlay.setDrawPaddingsEnabled(selectedItems.contains("paddings"));
                        overlay.setDrawRulersEnabled(selectedItems.contains("rulers"));
                        overlay.setDrawTextSizesEnabled(selectedItems.contains("text sizes"));

                        if (!debugEnabled && !selectedItems.isEmpty()) {
                            overlay.show();
                            debugEnabled = true;
                        } else if (debugEnabled && selectedItems.isEmpty()) {
                            overlay.dismiss();
                            debugEnabled = false;
                        }
                    });
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
                    v.setEnabled(checked == CheckedState.CHECKED);
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
