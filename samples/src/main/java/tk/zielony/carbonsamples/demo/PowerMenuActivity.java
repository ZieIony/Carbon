package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import carbon.widget.FrameLayout;
import carbon.widget.LinearLayout;
import carbon.widget.TransitionLayout;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-19.
 */
public class PowerMenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powermenu);

        final View powerMenu = findViewById(R.id.powerMenu);
        final TransitionLayout transitionLayout = (TransitionLayout) findViewById(R.id.transition);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                transitionLayout.setCurrentChild(0);
                final List<View> viewsWithTag = ((LinearLayout) transitionLayout.getChildAt(0)).findViewsWithTag("animate");
                for (int i = 0; i < viewsWithTag.size(); i++)
                    viewsWithTag.get(i).setVisibility(View.INVISIBLE);
                powerMenu.setVisibility(View.VISIBLE);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final List<View> viewsWithTag = ((LinearLayout) transitionLayout.getChildAt(0)).findViewsWithTag("animate");
                        for (int i = 0; i < viewsWithTag.size(); i++) {
                            final int finalI = i;
                            v.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewsWithTag.get(finalI).setVisibility(View.VISIBLE);
                                }
                            }, i * 40);
                        }
                    }
                }, 200);
            }
        });

        findViewById(R.id.powerOff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final List<View> viewsWithTag = ((FrameLayout) transitionLayout.getChildAt(1)).findViewsWithTag("animate");
                for (int i = 0; i < viewsWithTag.size(); i++)
                    viewsWithTag.get(i).setVisibility(View.INVISIBLE);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final List<View> viewsWithTag = ((FrameLayout) transitionLayout.getChildAt(1)).findViewsWithTag("animate");
                        for (int i = 0; i < viewsWithTag.size(); i++) {
                            final int finalI = i;
                            v.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    viewsWithTag.get(finalI).setVisibility(View.VISIBLE);
                                }
                            }, i * 20);
                        }
                    }
                }, 400);
                transitionLayout.startTransition(TransitionLayout.TransitionType.Radial);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        powerMenu.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }
        });
    }
}
