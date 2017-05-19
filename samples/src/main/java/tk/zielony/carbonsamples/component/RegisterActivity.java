package tk.zielony.carbonsamples.component;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Arrays;

import carbon.component.DataBindingComponent;
import carbon.component.DefaultIconDropDownItem;
import carbon.component.DefaultIconEditTextItem;
import carbon.component.DefaultIconPasswordItem;
import carbon.component.DividerItem;
import carbon.component.DividerRow;
import carbon.component.IconDropDownRow;
import carbon.component.IconEditTextRow;
import carbon.component.IconPasswordRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.drawable.VectorDrawable;
import carbon.recycler.RowListAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.Samples;
import tk.zielony.carbonsamples.SamplesActivity;

public class RegisterActivity extends SamplesActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listcomponent);

        Samples.initToolbar(this, getString(R.string.registerActivity_title));

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        RowListAdapter<Serializable> adapter = new RowListAdapter<>(DefaultIconEditTextItem.class, IconEditTextRow.FACTORY);
        adapter.addFactory(PaddingItem.class, PaddingRow.FACTORY);
        adapter.addFactory(String.class, parent -> new DataBindingComponent(parent, R.layout.row_description));
        adapter.addFactory(DividerItem.class, DividerRow.FACTORY);
        adapter.addFactory(DefaultIconPasswordItem.class, IconPasswordRow.FACTORY);
        adapter.addFactory(DefaultIconDropDownItem.class, IconDropDownRow.FACTORY);
        recycler.setAdapter(adapter);
        adapter.setItems(Arrays.asList(
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                "Forms such as registration screen can be easily created with reusable components and a recycler",
                new DefaultIconEditTextItem(new VectorDrawable(getResources(), R.raw.profile), "login", ""),
                new DefaultIconEditTextItem(new VectorDrawable(getResources(), R.raw.email), "email", ""),
                new DefaultIconPasswordItem(new VectorDrawable(getResources(), R.raw.lock), "password", ""),
                new DefaultIconPasswordItem(null, "retype password", ""),
                new DefaultIconDropDownItem<>(new VectorDrawable(getResources(), R.raw.gender), "sex", new String[]{"Male", "Female"}, "Male"),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))));
    }
}
