package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import carbon.widget.ImageView;
import carbon.widget.RecentsAdapter;
import carbon.widget.RecentsList;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-14.
 */
public class RecentsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recents);

        RecentsList recents = (RecentsList) findViewById(R.id.recents);
        recents.setAdapter(new RecentsAdapter() {
            @Override
            public String getTitle(int position) {
                return "Item " + position;
            }

            @Override
            public View getView(int position) {
                ImageView iv = new ImageView(RecentsActivity.this);
                iv.setScaleType(android.widget.ImageView.ScaleType.CENTER_CROP);
                iv.setImageResource(R.drawable.mazda);
                iv.setBackgroundColor(0xffffffff);
                return iv;
            }

            @Override
            public Drawable getIcon(int position) {
                return getResources().getDrawable(R.drawable.ic_launcher);
            }

            @Override
            public int getHeaderColor(int position) {
                return 0xffffffff;
            }

            @Override
            public int getCount() {
                return 5;
            }
        });

        recents.setOnItemClickListener(new RecentsList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Toast.makeText(view.getContext(), "Card " + i + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
