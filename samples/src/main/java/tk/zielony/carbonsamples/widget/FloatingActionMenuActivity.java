package tk.zielony.carbonsamples.widget;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import carbon.Carbon;
import carbon.component.MenuItem;
import carbon.widget.FloatingActionButton;
import carbon.widget.FloatingActionMenu;
import tk.zielony.carbonsamples.ColorsActivity;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class FloatingActionMenuActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floatingactionmenu);

        Samples.initToolbar(this, getString(R.string.floatingActionMenuActivity_title));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setMenu(R.menu.menu_fab);
        FloatingActionMenu floatingActionMenu = fab.getFloatingActionMenu();
        floatingActionMenu.setFocusable(false);
        Menu menu = floatingActionMenu.getMenu();
        fab.setOnMenuItemClickListener(item -> {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = (MenuItem) menu.getItem(i);
            item.setBackgroundDrawable(new ColorDrawable(getResources().getColor(ColorsActivity.primary[i].color)));
        }

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        Menu menu2 = fab2.getFloatingActionMenu().getMenu();
        for (int i = 0; i < menu2.size(); i++) {
            MenuItem item = (MenuItem) menu2.getItem(i);
            item.setEnabled(i % 2 == 0);
        }

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        Menu menu3 = fab3.getFloatingActionMenu().getMenu();
        for (int i = 0; i < menu3.size(); i++) {
            MenuItem item = (MenuItem) menu3.getItem(i);
            item.setVisible(i % 2 == 0);
        }

        FloatingActionButton fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        Menu menu4 = fab4.getFloatingActionMenu().getMenu();
        for (int i = 0; i < menu4.size(); i++) {
            MenuItem item = (MenuItem) menu4.getItem(i);
            item.setIconTint(ColorStateList.valueOf(Carbon.getThemeColor(this, R.attr.carbon_iconColorInverse)));
        }
    }
}
