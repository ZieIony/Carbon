package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.SoundEffectConstants;
import android.view.View;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import java.util.List;

import carbon.widget.FrameLayout;
import carbon.widget.ImageView;
import carbon.widget.LinearLayout;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-04-19.
 */
public class PowerMenuActivity extends Activity {
    boolean vibration = false, volume = true, airplaneMode = false;
    View powerMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powermenu);

        powerMenu = findViewById(R.id.powerMenu);
        final FrameLayout transitionLayout = (FrameLayout) findViewById(R.id.transition);

        findViewById(R.id.button).setOnClickListener(view -> {
            if (powerMenu.getVisibility() == View.VISIBLE)
                return;
            for (int i = 0; i < transitionLayout.getChildCount(); i++)
                transitionLayout.getChildAt(i).setVisibility(i == 0 ? View.VISIBLE : View.GONE);
            final List<View> viewsWithTag = ((LinearLayout) transitionLayout.getChildAt(0)).findViewsWithTag("animate");
            Stream.of(viewsWithTag).forEach(v -> v.setVisibility(View.INVISIBLE));
            powerMenu.setVisibility(View.VISIBLE);
            view.getHandler().postDelayed(() -> {
                Stream.of(viewsWithTag).forEach(v -> {
                    v.getHandler().postDelayed(() -> {
                        v.setVisibility(View.VISIBLE);
                    }, viewsWithTag.indexOf(v) * 40);
                });
            }, 200);
        });

        findViewById(R.id.powerOff).setOnClickListener(view -> {
            final List<View> viewsWithTag = ((FrameLayout) transitionLayout.getChildAt(1)).findViewsWithTag("animate");
            for (int i = 0; i < viewsWithTag.size(); i++)
                viewsWithTag.get(i).setVisibility(View.INVISIBLE);
            view.getHandler().postDelayed(() -> {
                for (int i = 0; i < viewsWithTag.size(); i++) {
                    final int finalI = i;
                    view.getHandler().postDelayed(() -> viewsWithTag.get(finalI).setVisibility(View.VISIBLE), i * 20);
                }
            }, 400);
            //transitionLayout.setHotspot(view.findViewById(R.id.powerOffIcon));
            //transitionLayout.startTransition(1, TransitionLayout.TransitionType.Radial);
            view.getHandler().postDelayed(() -> powerMenu.setVisibility(View.INVISIBLE), 3000);
        });


        findViewById(R.id.reboot).setOnClickListener(view -> {
            final List<View> viewsWithTag = ((FrameLayout) transitionLayout.getChildAt(2)).findViewsWithTag("animate");
            for (int i = 0; i < viewsWithTag.size(); i++)
                viewsWithTag.get(i).setVisibility(View.INVISIBLE);
            view.getHandler().postDelayed(() -> {
                for (int i = 0; i < viewsWithTag.size(); i++) {
                    final int finalI = i;
                    view.getHandler().postDelayed(() -> viewsWithTag.get(finalI).setVisibility(View.VISIBLE), i * 20);
                }
            }, 400);
            //transitionLayout.setHotspot(view.findViewById(R.id.rebootIcon));
            //transitionLayout.startTransition(2, TransitionLayout.TransitionType.Radial);
            view.getHandler().postDelayed(() -> powerMenu.setVisibility(View.INVISIBLE), 3000);
        });

        findViewById(R.id.airplaneMode).setOnClickListener(view -> {
            final List<View> viewsWithTag = ((FrameLayout) transitionLayout.getChildAt(3)).findViewsWithTag("animate");
            for (int i = 0; i < viewsWithTag.size(); i++)
                viewsWithTag.get(i).setVisibility(View.INVISIBLE);
            view.getHandler().postDelayed(() -> {
                for (int i = 0; i < viewsWithTag.size(); i++) {
                    final int finalI = i;
                    view.getHandler().postDelayed(() -> viewsWithTag.get(finalI).setVisibility(View.VISIBLE), i * 20);
                }
            }, 400);
            //transitionLayout.setHotspot(view.findViewById(R.id.airplaneModeIcon));
            //transitionLayout.startTransition(3, TransitionLayout.TransitionType.Radial);
            view.getHandler().postDelayed(() -> {
                //transitionLayout.startTransition(0, TransitionLayout.TransitionType.Radial, TransitionLayout.DEFAULT_DURATION, false);
                airplaneMode = !airplaneMode;
                TextView amStatus = (TextView) findViewById(R.id.airplaneModeStatus);
                amStatus.setText("Airplane Mode is " + (airplaneMode ? "on" : "off"));
                ImageView airplaneModeIcon = (ImageView) view.findViewById(R.id.airplaneModeIcon);
                airplaneModeIcon.setImageResource(airplaneMode ? R.raw.ic_airplanemode_on_24px : R.raw.ic_airplanemode_off_24px);
            }, 3000);
        });

        findViewById(R.id.vibration).setOnClickListener(view -> {
            if (vibration) {
                vibration = false;
                view.setBackgroundColor(0xffffffff);
                ((ImageView) view).setTint(getResources().getColor(R.color.carbon_black_54));
            } else {
                vibration = true;
                view.setBackgroundColor(0xff00695D);
                ((ImageView) view).setTint(0xffffffff);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
            }
            powerMenu.postInvalidate();
        });

        findViewById(R.id.volume).setOnClickListener(view -> {
            if (volume) {
                volume = false;
                view.setBackgroundColor(0xffffffff);
                ((ImageView) view).setTint(getResources().getColor(R.color.carbon_black_54));
            } else {
                volume = true;
                view.setBackgroundColor(0xff00695D);
                ((ImageView) view).setTint(0xffffffff);
                view.playSoundEffect(SoundEffectConstants.CLICK);
            }
            powerMenu.postInvalidate();
        });

    }

    @Override
    public void onBackPressed() {
        if (powerMenu.getVisibility() == View.VISIBLE) {
            powerMenu.setVisibility(View.INVISIBLE);
            return;
        }
        super.onBackPressed();
    }
}
