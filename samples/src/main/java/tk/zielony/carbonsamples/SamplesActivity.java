package tk.zielony.carbonsamples;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class SamplesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        applyTheme();
        super.onCreate(savedInstanceState);
    }

    public void applyTheme() {
        SharedPreferences preferences = getSharedPreferences(ColorsActivity.THEME, Context.MODE_PRIVATE);
        setTheme(ColorsActivity.styles[preferences.getInt(ColorsActivity.STYLE, 1)].value);
        getTheme().applyStyle(ColorsActivity.primary[preferences.getInt(ColorsActivity.PRIMARY, 8)].value, true);
        getTheme().applyStyle(ColorsActivity.accents[preferences.getInt(ColorsActivity.ACCENT, 14)].value, true);
    }
}
