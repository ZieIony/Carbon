package tk.zielony.carbonsamples.feature;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2014-12-15.
 */
public class RoundedCornersActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rounded_corners);

        Target view = (Target) findViewById(R.id.image);

        Picasso.with(this).load(R.drawable.mazda).into(view);
    }
}
