package tk.zielony.carbonsamples.widget;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import carbon.widget.RelativeLayout;
import carbon.widget.TabLayout;
import carbon.widget.ViewPager;
import carbon.widget.ViewPagerIndicator;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(layout = R.layout.activity_tabs, title = R.string.tabsActivity_title)
public class TabsActivity extends ThemedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter() {
            @Override
            public CharSequence getPageTitle(int position) {
                return "Page " + position;
            }

            View getView(int position, ViewPager pager) {
                return new RelativeLayout(pager.getContext(), null, R.attr.carbon_cardViewStyle);
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public int getCount() {
                return 7;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ViewPager pager = (ViewPager) container;
                View view = getView(position, pager);

                pager.addView(view);

                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
                container.removeView((View) view);
            }

        });
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setViewPager(pager);
        ViewPagerIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(pager);
    }
}
