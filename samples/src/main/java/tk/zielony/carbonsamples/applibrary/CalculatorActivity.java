package tk.zielony.carbonsamples.applibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;

import carbon.beta.TransitionLayout;
import carbon.widget.Button;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-02-24.
 */
public class CalculatorActivity extends Activity {
    int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
    }

    public void onClick(View v) {
        TextView display = (TextView) findViewById(R.id.display);
        Button button = (Button) v;
        switch (v.getId()) {
            case R.id.digit_0:
                if (number == 0)
                    break;
            case R.id.digit_1:
            case R.id.digit_2:
            case R.id.digit_3:
            case R.id.digit_4:
            case R.id.digit_5:
            case R.id.digit_6:
            case R.id.digit_7:
            case R.id.digit_8:
            case R.id.digit_9:
                number = number * 10 + Integer.parseInt(button.getText().toString());
                display.setText("" + number);
                break;
            case R.id.del:
                if (number == 0) {
                    final TransitionLayout transitionLayout = (TransitionLayout) findViewById(R.id.transition);
                    transitionLayout.setHotspot(transitionLayout.getWidth(), transitionLayout.getHeight());
                    transitionLayout.setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            transitionLayout.setListener(null);
                            transitionLayout.startTransition(1, TransitionLayout.TransitionType.Radial, TransitionLayout.DEFAULT_DURATION, false);
                        }
                    });
                    transitionLayout.startTransition(0, TransitionLayout.TransitionType.Fade, TransitionLayout.DEFAULT_DURATION, true);
                }
                number = number / 10;
                display.setText("" + number);
                break;
        }
    }
}
