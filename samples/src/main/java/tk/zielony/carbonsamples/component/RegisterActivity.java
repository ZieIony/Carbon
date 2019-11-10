package tk.zielony.carbonsamples.component;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.Arrays;

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
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;

@ActivityAnnotation(layout = R.layout.activity_register, title = R.string.registerActivity_title)
public class RegisterActivity extends ThemedActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        RowListAdapter<Serializable> adapter = new RowListAdapter<>(DefaultIconEditTextItem.class, IconEditTextRow::new);
        adapter.putFactory(PaddingItem.class, PaddingRow::new);
        adapter.putFactory(DividerItem.class, DividerRow::new);
        adapter.putFactory(DefaultIconPasswordItem.class, IconPasswordRow::new);
        adapter.putFactory(DefaultIconDropDownItem.class, IconDropDownRow::new);
        recycler.setAdapter(adapter);
        adapter.setItems(Arrays.asList(
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                new DefaultIconEditTextItem(new VectorDrawable(getResources(), R.raw.profile), "login", ""),
                new DefaultIconEditTextItem(new VectorDrawable(getResources(), R.raw.email), "email", ""),
                new DefaultIconPasswordItem(new VectorDrawable(getResources(), R.raw.lock), "password", ""),
                new DefaultIconPasswordItem(null, "retype password", ""),
                new DefaultIconDropDownItem<>(new VectorDrawable(getResources(), R.raw.gender), "sex", new String[]{"Male", "Female"}, "Male"),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))));
    }
}
