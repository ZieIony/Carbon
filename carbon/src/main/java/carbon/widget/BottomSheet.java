package carbon.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import carbon.R;

/**
 * Created by Marcin on 2014-11-29.
 */
public class BottomSheet extends PopupWindow {
    private float dim;
    ViewGroup content;

    public BottomSheet(Context context) {
        setContentView(content = (ViewGroup) View.inflate(context, R.layout.carbon_bottom_sheet, null));
    }
}
