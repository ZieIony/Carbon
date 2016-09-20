package tk.zielony.carbonsamples.widget;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import carbon.widget.PagerTabStrip;
import carbon.widget.RelativeLayout;
import carbon.widget.ViewPager;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class TabsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter() {
            @Override
            public CharSequence getPageTitle(int position) {
                return "Page " + position;
            }

            public View getView(int position, ViewPager pager) {
                return new RelativeLayout(pager.getContext(), null, R.attr.carbon_cardViewStyle);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return 7;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ViewPager pager = (ViewPager) container;
                View view = getView(position, pager);

                pager.addView(view);

                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object view) {
                ((ViewPager) container).removeView((View) view);
            }

        });
        PagerTabStrip tabs = (PagerTabStrip) findViewById(R.id.pager_title_strip);
        tabs.setViewPager(pager);
    }
}
