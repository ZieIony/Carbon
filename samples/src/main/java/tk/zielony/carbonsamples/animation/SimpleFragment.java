package tk.zielony.carbonsamples.animation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import carbon.widget.TextView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-02-08.
 */
public class SimpleFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText("Hey, I'm a fragment!");
        int padding = (int) getResources().getDimension(R.dimen.carbon_padding);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }
}
