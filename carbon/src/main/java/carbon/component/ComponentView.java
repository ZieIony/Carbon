package carbon.component;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.StyleRes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import carbon.R;

public class ComponentView extends FrameLayout {
    Component component;

    public ComponentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ComponentView(Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ComponentView(Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ComponentView, 0, 0);

        int id = a.getResourceId(R.styleable.ComponentView_carbon_id, 0);
        String type = a.getString(R.styleable.ComponentView_carbon_type);
        try {
            if (type != null) {
                Constructor<?> constructor = Class.forName(type).getConstructor(ViewGroup.class);
                component = (Component) constructor.newInstance(this);
                View view = component.getView();
                view.setTag(component);
                view.setId(id);
                addView(view);
            } else {
                throw new IllegalStateException("ComponentView needs a component type");
            }
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

    public <Type extends Component> Type getComponent() {
        return (Type) component;
    }
}
