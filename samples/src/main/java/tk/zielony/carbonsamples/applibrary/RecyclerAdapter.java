package tk.zielony.carbonsamples.applibrary;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleDrawableFroyo;
import carbon.widget.CardView;
import tk.zielony.carbonsamples.R;

/**
 * Created by Marcin on 2015-02-12.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<ViewModel> items;
    private int itemLayout;

    public RecyclerAdapter(List<ViewModel> items, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewModel item = items.get(position);
        holder.text.setText(item.getText());
        holder.image.setImageBitmap(null);
        Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
        holder.image.setVisibility(View.INVISIBLE);
        Picasso.with(holder.image.getContext()).load(item.getImage()).into((Target) holder.image);
        //Picasso.with(holder.image.getContext()).load(R.drawable.mazda).into((Target)holder.image);
        holder.itemView.setTag(item);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.card.getLayoutParams();
        if (position != items.size() - 1) {
            layoutParams.bottomMargin = 0;
            holder.card.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView text;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            card = (CardView) itemView;
            card.setClickable(true);
            card.setBackgroundColor(Color.WHITE);

            RippleDrawable rippleDrawable = new RippleDrawableFroyo(0x42ff0000, null, itemView.getContext(), RippleDrawable.Style.Over);
            rippleDrawable.setCallback(card);
            rippleDrawable.setHotspotEnabled(true);
            card.setRippleDrawable(rippleDrawable);
        }
    }
}
