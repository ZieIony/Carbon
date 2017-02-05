package carbon.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.text.Format;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import carbon.Carbon;
import carbon.R;
import carbon.recycler.ListAdapter;

/**
 * Created by Marcin on 2015-12-18.
 */
public class TableView extends RecyclerView {
    Map<Class, CellRenderer> cellRenderers = new HashMap<>();

    public TableView(Context context) {
        super(context);
        initTableView(context);
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTableView(context);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTableView(context);
    }

    private void initTableView(Context context) {
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        putCellRenderer(String.class, new StringRenderer());
        putCellRenderer(Integer.class, new IntegerRenderer());
        putCellRenderer(Float.class, new FloatRenderer());
        putCellRenderer(Boolean.class, new BooleanRenderer());
    }

    public static abstract class Adapter extends ListAdapter<Adapter.ViewHolder, List<?>> {
        private TableView tableView;

        public Adapter(TableView tableView) {
            this.tableView = tableView;
        }

        public abstract String getColumnName(int column);

        public abstract Class getColumnClass(int column);

        public abstract int getColumnCount();

        public int getColumnWeight(int column) {
            return 1;
        }

        public Format getColumnFormat(int column) {
            return null;
        }

        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout linearLayout = (LinearLayout) View.inflate(parent.getContext(), R.layout.carbon_tablelayout_row, null);
            linearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setMinimumHeight((int) parent.getContext().getResources().getDimension(R.dimen.carbon_tableRowHeight));
            float weightSum = 0;
            for (int i = 0; i < getColumnCount(); i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, getColumnWeight(i));
                linearLayout.addView(tableView.getCellRenderer(getColumnClass(i)).getView(parent.getContext()), params);
                weightSum += getColumnWeight(i);
            }
            linearLayout.setWeightSum(weightSum);
            return new ViewHolder(linearLayout);
        }

        @Override
        public void onBindViewHolder(Adapter.ViewHolder holder, final int position) {
            for (int i = 0; i < holder.row.getChildCount(); i++) {
                View cell = holder.row.getChildAt(i);
                CellRenderer cellRenderer = tableView.getCellRenderer(getColumnClass(i));
                cellRenderer.bindView(cell, getItem(position).get(i), getColumnFormat(i));
            }
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout row;

            public ViewHolder(View itemView) {
                super(itemView);

                row = (LinearLayout) itemView;
            }
        }
    }

    private CellRenderer getCellRenderer(Class c) {
        return cellRenderers.get(c);
    }

    public void putCellRenderer(Class c, CellRenderer cellRenderer) {
        cellRenderers.put(c, cellRenderer);
    }

    public interface CellRenderer<T, V extends View> {
        V getView(Context context);

        void bindView(V view, T value, Format format);
    }

    public static class StringRenderer implements CellRenderer<String, TextView> {

        @Override
        public TextView getView(Context context) {
            TextView textView = new TextView(context);
            textView.setPadding((int) context.getResources().getDimension(R.dimen.carbon_padding), 0, (int) context.getResources().getDimension(R.dimen.carbon_padding), 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextColor(Carbon.getThemeColor(context, android.R.attr.textColorPrimary));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            return textView;
        }

        @Override
        public void bindView(TextView view, String value, Format format) {
            view.setText(value);
        }
    }

    public static class IntegerRenderer implements CellRenderer<Integer, TextView> {

        @Override
        public TextView getView(Context context) {
            TextView textView = new TextView(context);
            textView.setPadding((int) context.getResources().getDimension(R.dimen.carbon_padding), 0, (int) context.getResources().getDimension(R.dimen.carbon_padding), 0);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            textView.setTextColor(Carbon.getThemeColor(context, android.R.attr.textColorPrimary));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            return textView;
        }

        @Override
        public void bindView(TextView view, Integer value, Format format) {
            if (format != null) {
                view.setText(format.format(value));
            } else {
                view.setText(value);
            }
        }
    }

    public static class FloatRenderer implements CellRenderer<Float, TextView> {

        @Override
        public TextView getView(Context context) {
            TextView textView = new TextView(context);
            textView.setPadding((int) context.getResources().getDimension(R.dimen.carbon_padding), 0, (int) context.getResources().getDimension(R.dimen.carbon_padding), 0);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            textView.setTextColor(Carbon.getThemeColor(context, android.R.attr.textColorPrimary));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            return textView;
        }

        @Override
        public void bindView(TextView view, Float value, Format format) {
            if (format != null) {
                view.setText(format.format(value));
            } else {
                view.setText(String.valueOf(value));
            }
        }
    }

    public static class BooleanRenderer implements CellRenderer<Boolean, FrameLayout> {

        @Override
        public FrameLayout getView(Context context) {
            FrameLayout layout = new FrameLayout(context);
            CheckBox checkBox = new CheckBox(context);
            checkBox.setPadding(0, 0, 0, 0);
            checkBox.setEnabled(false);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            layout.addView(checkBox, params);
            return layout;
        }

        @Override
        public void bindView(FrameLayout view, Boolean value, Format format) {
            ((CheckBox) view.getChildAt(0)).setCheckedImmediate(value);
        }
    }
}
