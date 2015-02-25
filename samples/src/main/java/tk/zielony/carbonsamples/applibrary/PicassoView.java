package tk.zielony.carbonsamples.applibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import carbon.widget.ImageView;

/**
 * Created by Marcin on 2015-02-08.
 */
public class PicassoView extends ImageView implements Target {
    public PicassoView(Context context) {
        super(context);
    }

    public PicassoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PicassoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setImageBitmap(bitmap);
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
}
