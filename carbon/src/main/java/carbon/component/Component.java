package carbon.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import carbon.R;
import carbon.recycler.Row;

/**
 * Created by Marcin on 2017-02-19.
 */

public class Component extends FrameLayout{
    public Component(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Component(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Component(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Component, 0, 0);
        if (a != null) {
            int id = a.getResourceId(R.styleable.Component_carbon_id, 0);
            int layout = a.getResourceId(R.styleable.Component_carbon_layout, 0);
            String type = a.getString(R.styleable.Component_carbon_type);
            try {
                Row row;
                if (layout != 0 && type == null) {
                    row = new DataBindingRow(this, layout);
                } else {
                    Constructor<?> constructor = Class.forName(type).getConstructor(ViewGroup.class);
                    row = (Row) constructor.newInstance(this);
                }
                View view = row.getView();
                view.setTag(row);
                view.setId(id);
                addView(view);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            a.recycle();
        }
    }
}
