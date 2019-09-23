package tk.zielony.carbonsamples;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import java.io.Serializable;

import carbon.widget.DropDown;
import tk.zielony.carbonsamples.databinding.ActivityColordemoBinding;

@ActivityAnnotation(title = R.string.colorsActivity_title)
public class ColorsActivity extends ThemedActivity {

    public static final String ACCENT = "accent";
    public static final String PRIMARY = "primary";
    public static final String STYLE = "style";
    public static final String THEME = "theme";

    public static class Item implements Serializable {
        String name;
        int value;
        public int color;

        public Item(String name, int value, int color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static Item[] styles = new Item[]{
            new Item("Dark", R.style.ThemeDark, R.color.carbon_colorBackground_dark),
            new Item("Dark.DarkPrimaryColor", R.style.ThemeDark_Inverse, R.color.carbon_colorBackground_dark),
            new Item("Light", R.style.ThemeLight, R.color.carbon_colorBackground_light),
            new Item("Light.DarkPrimaryColor", R.style.ThemeLight_Inverse, R.color.carbon_colorBackground_light),
            new Item("DayNight", R.style.ThemeDayNight, R.color.carbon_black_38),
            new Item("DayNight.DarkPrimaryColor", R.style.ThemeDayNight_Inverse, R.color.carbon_black_38)
    };

    public static Item[] primary = new Item[]{
            new Item("Red", R.style.PrimaryRed, R.color.carbon_red_400),
            new Item("Pink", R.style.PrimaryPink, R.color.carbon_pink_400),
            new Item("Purple", R.style.PrimaryPurple, R.color.carbon_purple_400),
            new Item("Deep purple", R.style.PrimaryDeepPurple, R.color.carbon_deepPurple_400),
            new Item("Indigo", R.style.PrimaryIndigo, R.color.carbon_indigo_400),
            new Item("Blue", R.style.PrimaryBlue, R.color.carbon_blue_400),
            new Item("Light blue", R.style.PrimaryLightBlue, R.color.carbon_lightBlue_400),
            new Item("Cyan", R.style.PrimaryCyan, R.color.carbon_cyan_400),
            new Item("Teal", R.style.PrimaryTeal, R.color.carbon_teal_400),
            new Item("Green", R.style.PrimaryGreen, R.color.carbon_green_400),
            new Item("Light green", R.style.PrimaryLightGreen, R.color.carbon_lightGreen_400),
            new Item("Lime", R.style.PrimaryLime, R.color.carbon_lime_400),
            new Item("Yellow", R.style.PrimaryYellow, R.color.carbon_yellow_400),
            new Item("Amber", R.style.PrimaryAmber, R.color.carbon_amber_400),
            new Item("Orange", R.style.PrimaryOrange, R.color.carbon_orange_400),
            new Item("Deep orange", R.style.PrimaryDeepOrange, R.color.carbon_deepOrange_400),
            new Item("Brown", R.style.PrimaryBrown, R.color.carbon_brown_400),
            new Item("Grey", R.style.PrimaryGrey, R.color.carbon_grey_400),
            new Item("Blue grey", R.style.PrimaryBlueGrey, R.color.carbon_blueGrey_400),
            new Item("White", R.style.PrimaryWhite, R.color.carbon_white),
            new Item("Black", R.style.PrimaryBlack, R.color.carbon_black)
    };

    static Item[] accents = new Item[]{
            new Item("Red", R.style.AccentRed, R.color.carbon_red_a200),
            new Item("Pink", R.style.AccentPink, R.color.carbon_pink_a200),
            new Item("Purple", R.style.AccentPurple, R.color.carbon_purple_a200),
            new Item("Deep purple", R.style.AccentDeepPurple, R.color.carbon_deepPurple_a200),
            new Item("Indigo", R.style.AccentIndigo, R.color.carbon_indigo_a200),
            new Item("Blue", R.style.AccentBlue, R.color.carbon_blue_a200),
            new Item("Light blue", R.style.AccentLightBlue, R.color.carbon_lightBlue_a200),
            new Item("Cyan", R.style.AccentCyan, R.color.carbon_cyan_a200),
            new Item("Teal", R.style.AccentTeal, R.color.carbon_teal_a200),
            new Item("Green", R.style.AccentGreen, R.color.carbon_green_a200),
            new Item("Light green", R.style.AccentLightGreen, R.color.carbon_lightGreen_a200),
            new Item("Lime", R.style.AccentLime, R.color.carbon_lime_a200),
            new Item("Yellow", R.style.AccentYellow, R.color.carbon_yellow_a200),
            new Item("Amber", R.style.AccentAmber, R.color.carbon_amber_a200),
            new Item("Orange", R.style.AccentOrange, R.color.carbon_orange_a200),
            new Item("Deep orange", R.style.AccentDeepOrange, R.color.carbon_deepOrange_a200),
            new Item("White", R.style.AccentWhite, R.color.carbon_white),
            new Item("Black", R.style.AccentBlack, R.color.carbon_black)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityColordemoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_colordemo);

        Samples.initToolbar(this);

        ((DropDown<Item>) binding.style).setItems(styles);
        ((DropDown<Item>) binding.style).setOnSelectionChangedListener((item, position) -> {
            SharedPreferences preferences = ColorsActivity.this.getSharedPreferences(THEME, Context.MODE_PRIVATE);
            preferences.edit().putInt(STYLE, binding.style.getSelectedIndex()).commit();
            binding.themebg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        });
        {
            SharedPreferences preferences = ColorsActivity.this.getSharedPreferences(THEME, Context.MODE_PRIVATE);
            binding.style.setSelectedIndex(preferences.getInt(STYLE, 2));
            Item item = styles[preferences.getInt(STYLE, 2)];
            binding.themebg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        }

        ((DropDown<Item>) binding.primary).setItems(primary);
        ((DropDown<Item>) binding.primary).setOnSelectionChangedListener((item, position) -> {
            SharedPreferences preferences = ColorsActivity.this.getSharedPreferences(THEME, Context.MODE_PRIVATE);
            preferences.edit().putInt(PRIMARY, binding.primary.getSelectedIndex()).commit();
            binding.primarybg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        });
        {
            SharedPreferences preferences = ColorsActivity.this.getSharedPreferences(THEME, Context.MODE_PRIVATE);
            binding.primary.setSelectedIndex(preferences.getInt(PRIMARY, 8));
            Item item = primary[preferences.getInt(PRIMARY, 8)];
            binding.primarybg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        }

        ((DropDown<Item>) binding.accent).setItems(accents);
        ((DropDown<Item>) binding.accent).setOnSelectionChangedListener((item, position) -> {
            SharedPreferences preferences = ColorsActivity.this.getSharedPreferences(THEME, Context.MODE_PRIVATE);
            preferences.edit().putInt(ACCENT, binding.accent.getSelectedIndex()).commit();
            binding.accentbg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        });
        {
            SharedPreferences preferences = ColorsActivity.this.getSharedPreferences(THEME, Context.MODE_PRIVATE);
            binding.accent.setSelectedIndex(preferences.getInt(ACCENT, 14));
            Item item = accents[preferences.getInt(ACCENT, 14)];
            binding.accentbg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
