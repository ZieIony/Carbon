package pl.zielony.carbon.beta;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import pl.zielony.carbon.R;
import pl.zielony.carbon.widget.FrameLayout;
import pl.zielony.carbon.widget.TextView;

/**
 * Created by Marcin on 2015-01-20.
 */
public class Window {
    private final Context context;
    private final View windowLayout;
    private View contentView;

    public Window(Context context) {
        this.context = context;
        windowLayout = View.inflate(context, R.layout.carbon_dialog, null);
    }

    public void setContentView(int id) {
        contentView = View.inflate(context, id, null);
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void show() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        layoutParams.format = PixelFormat.RGBA_8888;

        windowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        FrameLayout content = (FrameLayout) windowLayout.findViewById(R.id.carbon_windowContent);
        content.addView(contentView);
        wm.addView(windowLayout, layoutParams);
    }

    private void hide() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(windowLayout);
    }

    public void setTitle(String text) {
        TextView title = (TextView) windowLayout.findViewById(R.id.carbon_windowTitle);
        title.setText(text);
    }
}
