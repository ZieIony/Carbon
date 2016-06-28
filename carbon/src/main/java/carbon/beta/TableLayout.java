package carbon.beta;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import carbon.R;
import carbon.widget.FrameLayout;
import carbon.widget.LinearLayout;
import carbon.widget.Spinner;
import carbon.widget.TableView;
import carbon.widget.TextView;
import carbon.widget.Toolbar;

/**
 * Created by Marcin on 2015-12-18.
 */
public class TableLayout extends LinearLayout {
    private TableView table;
    Toolbar toolbar;
    LinearLayout header;
    View footer;
    Spinner rowNumber;
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

    private void initTableLayout() {
        View.inflate(getContext(), R.layout.carbon_tablelayout, this);
        setOrientation(VERTICAL);

        toolbar = (Toolbar) findViewById(R.id.carbon_tableToolbar);
        header = (LinearLayout) findViewById(R.id.carbon_tableHeader);
        table = (TableView) findViewById(R.id.carbon_table);
        footer = findViewById(R.id.carbon_tableFooter);
        rowNumber = (Spinner) findViewById(R.id.carbon_tableRowNumber);
        rowNumber.setItems(new String[]{"10", "20", "50"});
        pageNumbers = (TextView) findViewById(R.id.carbon_tablePageNumbers);
    }

    public TableView getTableView() {
        return table;
    }

    public Toolbar getToolbar() {
        return toolbar;
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
            TextView tv = (TextView) headerCell.findViewById(R.id.carbon_tableHeaderText);
            tv.setText(adapter.getColumnName(i));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, adapter.getColumnWeight(i));
            header.addView(headerCell, params);
        }
        rowNumber.setText("10");
        pageNumbers.setText("1-" + adapter.getItemCount() + " of " + adapter.getItemCount());
    }
}
