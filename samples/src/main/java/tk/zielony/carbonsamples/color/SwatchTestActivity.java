package tk.zielony.carbonsamples.color;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2017-02-13.
 */
public class SwatchTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getIntent().getIntExtra("theme", R.style.ThemeLight));
        getTheme().applyStyle(getIntent().getIntExtra("primary", R.style.PrimaryRed), true);
        getTheme().applyStyle(getIntent().getIntExtra("accent", R.style.AccentBlue), true);
        setContentView(R.layout.activity_swatchtest);
    }
}
