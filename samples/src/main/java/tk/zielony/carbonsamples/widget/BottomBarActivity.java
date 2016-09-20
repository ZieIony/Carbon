package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;

import carbon.widget.BottomBar;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2016-03-17.
 */
public class BottomBarActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setMenu(R.menu.menu_navigation);
    }
}
