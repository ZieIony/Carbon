package tk.zielony.carbonsamples.demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.SoundEffectConstants;
import android.view.View;

import com.annimon.stream.Stream;

import java.util.List;

import carbon.animation.AnimatedView;
import carbon.widget.Button;
import carbon.widget.FrameLayout;
import carbon.widget.ImageView;
import carbon.widget.LinearLayout;
import carbon.view.RevealView;
import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.SamplesActivity;

public class PowerMenuActivity extends SamplesActivity {
    boolean vibration = false, volume = true, airplaneMode = false;

    Button button;
    LinearLayout powerMenu, screenPowerMenu;
    FrameLayout transition, screenPowerOff, screenReboot, screenAirplaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powermenu);

        button = findViewById(R.id.button);
        powerMenu = findViewById(R.id.powerMenu);
        transition = findViewById(R.id.transition);
        screenPowerMenu = findViewById(R.id.screen_powerMenu);
        screenPowerOff = findViewById(R.id.screen_powerOff);
        screenReboot = findViewById(R.id.screen_reboot);
        screenAirplaneMode = findViewById(R.id.screen_airplaneMode);

        button.setOnClickListener(view -> {
            if (powerMenu.getVisibility() == View.VISIBLE)
                return;
            for (int i = 0; i < transition.getChildCount(); i++)
                transition.getChildAt(i).setVisibility(i == 0 ? View.VISIBLE : View.GONE);
            final List<View> viewsWithTag = screenPowerMenu.findViewsWithTag("animate");
            Stream.of(viewsWithTag).forEach(v -> v.setVisibility(View.INVISIBLE));
            Animator animator = powerMenu.animateVisibility(View.VISIBLE);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Stream.of(viewsWithTag).forEach(v -> {
                        v.getHandler().postDelayed(() -> {
                            if (v instanceof AnimatedView) {
                                ((AnimatedView) v).animateVisibility(View.VISIBLE);
                            } else {
                                v.setVisibility(View.VISIBLE);
                            }
                        }, viewsWithTag.indexOf(v) * 40);
                    });
                }
            });
        });

        findViewById(R.id.powerOff).setOnClickListener(view -> {
            final List<View> viewsWithTag = screenPowerOff.findViewsWithTag("animate");
            Stream.of(viewsWithTag).forEach(v -> v.setVisibility(View.INVISIBLE));
            screenPowerOff.setVisibility(View.VISIBLE);
            Animator circularReveal = screenPowerOff.createCircularReveal(view.findViewById(R.id.powerOffIcon), 0, RevealView.MAX_RADIUS);
            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Stream.of(viewsWithTag).forEach(v -> {
                        view.getHandler().postDelayed(() -> {
                            if (v instanceof AnimatedView) {
                                ((AnimatedView) v).animateVisibility(View.VISIBLE);
                            } else {
                                v.setVisibility(View.VISIBLE);
                            }
                        }, viewsWithTag.indexOf(v) * 20);
                    });
                }
            });
            circularReveal.start();
            view.getHandler().postDelayed(() -> powerMenu.animateVisibility(View.INVISIBLE), 3000);
        });


        findViewById(R.id.reboot).setOnClickListener(view -> {
            final List<View> viewsWithTag = screenReboot.findViewsWithTag("animate");
            Stream.of(viewsWithTag).forEach(v -> v.setVisibility(View.INVISIBLE));
            screenReboot.setVisibility(View.VISIBLE);
            Animator circularReveal = screenReboot.createCircularReveal(view.findViewById(R.id.rebootIcon), 0, RevealView.MAX_RADIUS);
            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Stream.of(viewsWithTag).forEach(v -> {
                        view.getHandler().postDelayed(() -> {
                            if (v instanceof AnimatedView) {
                                ((AnimatedView) v).animateVisibility(View.VISIBLE);
                            } else {
                                v.setVisibility(View.VISIBLE);
                            }
                        }, viewsWithTag.indexOf(v) * 20);
                    });
                }
            });
            circularReveal.start();
            view.getHandler().postDelayed(() -> powerMenu.animateVisibility(View.INVISIBLE), 3000);
        });

        findViewById(R.id.airplaneMode).setOnClickListener(view -> {
            final List<View> viewsWithTag = screenAirplaneMode.findViewsWithTag("animate");
            Stream.of(viewsWithTag).forEach(v -> v.setVisibility(View.INVISIBLE));
            screenAirplaneMode.setVisibility(View.VISIBLE);
            Animator circularReveal = screenAirplaneMode.createCircularReveal(view.findViewById(R.id.airplaneModeIcon), 0, RevealView.MAX_RADIUS);
            circularReveal.setDuration(400);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Stream.of(viewsWithTag).forEach(v -> {
                        view.getHandler().postDelayed(() -> {
                            if (v instanceof AnimatedView) {
                                ((AnimatedView) v).animateVisibility(View.VISIBLE);
                            } else {
                                v.setVisibility(View.VISIBLE);
                            }
                        }, viewsWithTag.indexOf(v) * 20);
                    });
                }
            });
            circularReveal.start();
            view.getHandler().postDelayed(() -> {
                Animator circularReveal2 = screenAirplaneMode.createCircularReveal(view.findViewById(R.id.airplaneModeIcon), RevealView.MAX_RADIUS, 0);
                circularReveal2.setDuration(400);
                circularReveal2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        screenAirplaneMode.setVisibility(View.INVISIBLE);
                    }
                });
                circularReveal2.start();
                airplaneMode = !airplaneMode;
                TextView amStatus = findViewById(R.id.airplaneModeStatus);
                amStatus.setText("Airplane Mode is " + (airplaneMode ? "on" : "off"));
                ImageView airplaneModeIcon = view.findViewById(R.id.airplaneModeIcon);
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
        if (powerMenu.isVisible()) {
            powerMenu.animateVisibility(View.INVISIBLE);
            return;
        }
        super.onBackPressed();
    }
}
