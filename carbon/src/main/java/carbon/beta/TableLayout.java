package carbon.beta;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;
import carbon.widget.DropDown;
import carbon.widget.LinearLayout;
import carbon.widget.TableView;
import carbon.widget.TextView;

public class TableLayout extends LinearLayout {
    private TableView table;
    LinearLayout header;
    View footer;
    DropDown rowNumber;
    private TextView pageNumbers;

    public TableLayout(Context context) {
        super(context);
        initTableLayout();
    }

    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTableLayout();
    }

    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTableLayout();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initTableLayout();
    }

    private void initTableLayout() {
        View.inflate(getContext(), R.layout.carbon_tablelayout, this);
        setOrientation(VERTICAL);

        header = findViewById(R.id.carbon_tableHeader);
        table = findViewById(R.id.carbon_table);
        footer = findViewById(R.id.carbon_tableFooter);
        rowNumber = findViewById(R.id.carbon_tableRowNumber);
        rowNumber.setItems(new String[]{"10", "20", "50"});
        pageNumbers = findViewById(R.id.carbon_tablePageNumbers);
    }

    public TableView getTableView() {
        return table;
    }

    public View getHeader() {
        return header;
    }

    public View getFooter() {
        return footer;
    }

    public void setAdapter(TableView.Adapter adapter) {
        table.setAdapter(adapter);
        header.removeAllViews();
        for (int i = 0; i < adapter.getColumnCount(); i++) {
            View headerCell = View.inflate(getContext(), R.layout.carbon_tablelayout_header, null);
            TextView tv = headerCell.findViewById(R.id.carbon_tableHeaderText);
            tv.setText(adapter.getColumnName(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, adapter.getColumnWeight(i));
            header.addView(headerCell, params);
        }
        rowNumber.setText("10");
        pageNumbers.setText("1-" + adapter.getItemCount() + " of " + adapter.getItemCount());
    }
}
