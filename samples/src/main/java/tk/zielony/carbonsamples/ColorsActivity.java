package tk.zielony.carbonsamples;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import tk.zielony.carbonsamples.color.SwatchTestActivity;
import tk.zielony.carbonsamples.databinding.ActivityColordemoBinding;


public class ColorsActivity extends SamplesActivity {

    private Bundle extras = new Bundle();

    private static class Item {
        String name;
        int value;
        int color;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityColordemoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_colordemo);

        Samples.initToolbar(this, getString(R.string.colorsActivity_title));

        binding.test.setOnClickListener(v -> {
            Intent intent = new Intent(ColorsActivity.this, SwatchTestActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        });

        binding.apply.setOnClickListener(v -> {
            Samples.setTheme(ColorsActivity.this, extras.getInt("theme"), extras.getInt("primary"), extras.getInt("accent"));
            finish();
        });

        binding.theme.setItems(new Object[]{
                new Item("Dark", R.style.ThemeDark, R.color.carbon_colorBackground_dark),
                new Item("Light", R.style.ThemeLight, R.color.carbon_colorBackground_light)
        });
        binding.theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) binding.theme.getSelectedItem();
                extras.putInt("theme", item.value);
                binding.themebg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        {
            Item item = (Item) binding.theme.getAdapter().getItem(0);
            extras.putInt("theme", item.value);
            binding.themebg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        }

        binding.primary.setItems(new Object[]{
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
                new Item("Blue grey", R.style.PrimaryBlueGrey, R.color.carbon_blueGrey_400)
        });
        binding.primary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) binding.primary.getSelectedItem();
                extras.putInt("primary", item.value);
                binding.primarybg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        {
            Item item = (Item) binding.primary.getAdapter().getItem(0);
            extras.putInt("primary", item.value);
            binding.primarybg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        }

        binding.accent.setItems(new Object[]{
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
        });
        binding.accent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Item item = (Item) binding.accent.getSelectedItem();
                extras.putInt("accent", item.value);
                binding.accentbg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        {
            Item item = (Item) binding.accent.getAdapter().getItem(0);
            extras.putInt("accent", item.value);
            binding.accentbg.setImageDrawable(new ColorDrawable(getResources().getColor(item.color)));
        }
    }

}
