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

        ActivityAnnotation annotation = getClass().getAnnotation(ActivityAnnotation.class);
        if (annotation != null) {
            if (annotation.layout() != 0)
                setContentView(annotation.layout());
            if (annotation.title() != 0)
                Samples.initToolbar(this, getString(annotation.title()));
        }
    }

    public void applyTheme() {
        SharedPreferences preferences = getSharedPreferences(ColorsActivity.THEME, Context.MODE_PRIVATE);
        setTheme(ColorsActivity.styles[preferences.getInt(ColorsActivity.STYLE, 1)].value);
        getTheme().applyStyle(ColorsActivity.primary[preferences.getInt(ColorsActivity.PRIMARY, 8)].value, true);
        getTheme().applyStyle(ColorsActivity.accents[preferences.getInt(ColorsActivity.ACCENT, 14)].value, true);
    }
}
