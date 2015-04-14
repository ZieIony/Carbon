package tk.zielony.carbonsamples.applibrary;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import carbon.drawable.RippleDrawable;
import carbon.drawable.RippleDrawableCompat;
import carbon.widget.CardView;
import carbon.widget.LinearLayout;
import carbon.widget.StateAnimatorView;
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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewModel item = items.get(position);
        holder.text.setText(item.getText());
        holder.image.setImageBitmap(null);
        Picasso.with(holder.image.getContext()).cancelRequest(holder.image);
        holder.image.setVisibility(View.INVISIBLE);
        //Picasso.with(holder.image.getContext()).load(item.getImage()).into((Target)holder.image);
        Picasso.with(holder.image.getContext()).load(R.drawable.mazda).into((Target)holder.image);
        holder.itemView.setTag(item);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.card.getLayoutParams();
        layoutParams.bottomMargin = position == items.size() - 1 ? 0 : (int) -holder.card.getContext().getResources().getDimension(R.dimen.carbon_paddingHalf);
        holder.card.setLayoutParams(layoutParams);
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
            LinearLayout content = card.getContent();
            content.setClickable(true);
            content.setFocusable(true);
            content.setFocusableInTouchMode(true);
            card.setCardBackgroundColor(Color.WHITE);

            RippleDrawable rippleDrawable = new RippleDrawableCompat(0x42ff0000,null,itemView.getContext(),RippleDrawable.Style.Over);
            rippleDrawable.setCallback(content);
            rippleDrawable.setHotspotEnabled(true);
            content.setRippleDrawable(rippleDrawable);
        }
    }
}
