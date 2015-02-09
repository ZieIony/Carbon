package tk.zielony.carbonsamples;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Marcin on 2014-12-15.
 */
public class AnimationsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations);

        final View fab = findViewById(R.id.fab);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab.getVisibility() != View.VISIBLE) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.INVISIBLE);
                }
            }
        });

        final View button2 = findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button2.getVisibility()!=View.VISIBLE){
                    button2.setVisibility(View.VISIBLE);
                }else {
                    button2.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
