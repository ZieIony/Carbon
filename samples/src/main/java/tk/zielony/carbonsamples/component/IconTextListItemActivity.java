package tk.zielony.carbonsamples.component;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.Arrays;

import carbon.component.DefaultIconSearchItem;
import carbon.component.DefaultIconTextItem;
import carbon.component.IconSearchRow;
import carbon.component.IconTextRow;
import carbon.component.PaddingItem;
import carbon.component.PaddingRow;
import carbon.drawable.ColorStateListDrawable;
import carbon.drawable.DefaultColorControlStateList;
import carbon.drawable.VectorDrawable;
import carbon.recycler.DividerItemDecoration;
import carbon.recycler.RowListAdapter;
import carbon.widget.ArraySearchAdapter;
import carbon.widget.RecyclerView;
import tk.zielony.carbonsamples.ActivityAnnotation;
import tk.zielony.carbonsamples.R;
import tk.zielony.carbonsamples.ThemedActivity;
import tk.zielony.randomdata.person.StringNameGenerator;

@ActivityAnnotation(layout = R.layout.activity_listcomponent, title = R.string.iconTextListItemActivity_title)
public class IconTextListItemActivity extends ThemedActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        RowListAdapter<Serializable> adapter = new RowListAdapter<>(DefaultIconTextItem.class, IconTextRow::new);
        adapter.addFactory(PaddingItem.class, PaddingRow::new);
        adapter.addFactory(DefaultIconSearchItem.class, parent -> new IconSearchRow<>(parent, new ArraySearchAdapter<>(new String[]{}), filterResults -> {
        }));
        recycler.setAdapter(adapter);

        Drawable dividerDrawable = new ColorStateListDrawable(new DefaultColorControlStateList(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable, getResources().getDimensionPixelSize(R.dimen.carbon_dividerHeight));
        dividerItemDecoration.setDrawAfter(position -> adapter.getItem(position) instanceof DefaultIconSearchItem);
        recycler.addItemDecoration(dividerItemDecoration);

        VectorDrawable drawable = new VectorDrawable(getResources(), R.raw.ic_face_24px);
        StringNameGenerator generator = new StringNameGenerator();
        adapter.setItems(Arrays.asList(
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf)),
                new DefaultIconSearchItem(this),
                new DefaultIconTextItem(drawable, generator.next()),
                new DefaultIconTextItem(drawable, generator.next()),
                new DefaultIconTextItem(drawable, generator.next()),
                new DefaultIconTextItem(drawable, generator.next()),
                new DefaultIconTextItem(drawable, generator.next()),
                new DefaultIconTextItem(drawable, generator.next()),
                new DefaultIconTextItem(drawable, generator.next()),
                new DefaultIconTextItem(drawable, generator.next()),
                new PaddingItem(getResources().getDimensionPixelSize(R.dimen.carbon_paddingHalf))));
    }
}
