package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import carbon.widget.PagerTabStrip;
import carbon.widget.RelativeLayout;
import carbon.widget.ViewPager;
import carbon.widget.ViewPagerIndicator;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class TabsActivity extends SamplesActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Samples.initToolbar(this, getString(R.string.tabsActivity_title));

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
        PagerTabStrip tabs = (PagerTabStrip) findViewById(R.id.tabStrip);
        tabs.setViewPager(pager);
        ViewPagerIndicator indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }
}
