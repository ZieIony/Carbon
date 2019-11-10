package tk.zielony.carbonsamples.widget;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import carbon.Carbon;
import carbon.widget.FloatingActionButton;
import carbon.widget.FloatingActionMenu;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.ColorsActivity;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(
        layout = R.layout.activity_fab,
        title = R.string.fabActivity_title,
        icon = R.drawable.ic_add_circle_black_24dp
)
public class FABActivity extends ThemedActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setMenu(R.menu.menu_fab);
        FloatingActionMenu floatingActionMenu = fab.getFloatingActionMenu();
        floatingActionMenu.setFocusable(false);
        FloatingActionMenu.Item[] menu = floatingActionMenu.getMenuItems();
        fab.setOnItemClickedListener((View v, FloatingActionMenu.Item item, int position) -> {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        });
        for (int i = 0; i < menu.length; i++) {
            menu[i].setBackgroundDrawable(new ColorDrawable(getResources().getColor(ColorsActivity.primary[i].color)));
        }

        FloatingActionButton fab2 = findViewById(R.id.fab2);
        FloatingActionMenu.Item[] menu2 = fab2.getFloatingActionMenu().getMenuItems();
        for (int i = 0; i < menu2.length; i++) {
            menu2[i].setEnabled(i % 2 == 0);
        }

        FloatingActionButton fab4 = findViewById(R.id.fab4);
        FloatingActionMenu.Item[] menu4 = fab4.getFloatingActionMenu().getMenuItems();
        for (FloatingActionMenu.Item item : menu4) {
            item.setIconTintList(ColorStateList.valueOf(Carbon.getThemeColor(this, R.attr.carbon_iconColorInverse)));
        }
    }
}
