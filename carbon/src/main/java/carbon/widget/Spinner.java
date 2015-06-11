package carbon.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import carbon.R;

/**
 * Created by Marcin on 2015-06-11.
 */
public class Spinner extends TextView {
    PopupMenu popupMenu;

    public Spinner(Context context) {
        this(context, null);
    }

    public Spinner(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public Spinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        popupMenu = new PopupMenu(context);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.setWidth((int) (getWidth() + getResources().getDimension(R.dimen.carbon_padding) * 2));
                popupMenu.show(Spinner.this);
            }
        });
    }

    public void setAdapter(final RecyclerView.Adapter adapter) {
        popupMenu.setAdapter(adapter);
        adapter.setOnItemClickedListener(new RecyclerView.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position) {
                setText(adapter.getItem(position).toString());
                popupMenu.dismiss();
            }
        });
    }

    public RecyclerView.Adapter getAdapter() {
        return popupMenu.getAdapter();
    }
}
