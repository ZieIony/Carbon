package tk.zielony.carbonsamples.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.SoundEffectConstants;
import android.view.View;

import java.util.List;

import carbon.beta.TransitionLayout;
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
        final TransitionLayout transitionLayout = (TransitionLayout) findViewById(R.id.transition);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (powerMenu.getVisibility() == View.VISIBLE)
                    return;
                transitionLayout.setCurrentChild(0);
                final List<View> viewsWithTag = ((LinearLayout) transitionLayout.getChildAt(0)).findViewsWithTag("animate");
                for (int i = 0; i < viewsWithTag.size(); i++)
                    viewsWithTag.get(i).setVisibility(View.INVISIBLE);
                powerMenu.setVisibility(View.VISIBLE);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                transitionLayout.setHotspot(v.findViewById(R.id.powerOffIcon));
                transitionLayout.startTransition(1, TransitionLayout.TransitionType.Radial);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        powerMenu.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }
        });


        findViewById(R.id.reboot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final List<View> viewsWithTag = ((FrameLayout) transitionLayout.getChildAt(2)).findViewsWithTag("animate");
                for (int i = 0; i < viewsWithTag.size(); i++)
                    viewsWithTag.get(i).setVisibility(View.INVISIBLE);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                transitionLayout.setHotspot(v.findViewById(R.id.rebootIcon));
                transitionLayout.startTransition(2, TransitionLayout.TransitionType.Radial);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        powerMenu.setVisibility(View.INVISIBLE);
                    }
                }, 3000);
            }
        });

        findViewById(R.id.airplaneMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final List<View> viewsWithTag = ((FrameLayout) transitionLayout.getChildAt(3)).findViewsWithTag("animate");
                for (int i = 0; i < viewsWithTag.size(); i++)
                    viewsWithTag.get(i).setVisibility(View.INVISIBLE);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                transitionLayout.setHotspot(v.findViewById(R.id.airplaneModeIcon));
                transitionLayout.startTransition(3, TransitionLayout.TransitionType.Radial);
                v.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        transitionLayout.startTransition(0, TransitionLayout.TransitionType.Radial, TransitionLayout.DEFAULT_DURATION, false);
                        airplaneMode = !airplaneMode;
                        TextView amStatus = (TextView) findViewById(R.id.airplaneModeStatus);
                        amStatus.setText("Airplane Mode is " + (airplaneMode ? "on" : "off"));
                        ImageView airplaneModeIcon = (ImageView) v.findViewById(R.id.airplaneModeIcon);
                        airplaneModeIcon.setImageResource(airplaneMode ? R.raw.ic_airplanemode_on_24px : R.raw.ic_airplanemode_off_24px);
                    }
                }, 3000);
            }
        });

        findViewById(R.id.vibration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vibration) {
                    vibration = false;
                    v.setBackgroundColor(0xffffffff);
                    ((ImageView) v).setTint(getResources().getColor(R.color.carbon_black_54));
                } else {
                    vibration = true;
                    v.setBackgroundColor(0xff00695D);
                    ((ImageView) v).setTint(0xffffffff);
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                }
                powerMenu.postInvalidate();
            }
        });

        findViewById(R.id.volume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume) {
                    volume = false;
                    v.setBackgroundColor(0xffffffff);
                    ((ImageView) v).setTint(getResources().getColor(R.color.carbon_black_54));
                } else {
                    volume = true;
                    v.setBackgroundColor(0xff00695D);
                    ((ImageView) v).setTint(0xffffffff);
                    v.playSoundEffect(SoundEffectConstants.CLICK);
                }
                powerMenu.postInvalidate();
            }
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
