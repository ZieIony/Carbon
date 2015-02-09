package carbon.beta;

import android.app.Activity;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marcin on 2015-01-17.
 */
public class SaveStateActivity extends Activity {
    static Map<String, Object> savedObjects = new HashMap<>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
    }

    private void saveState() {
        List<Field> fieldsToSave = new ArrayList<Field>();
        Field[] fields = getClass().getFields();
        for (Field f : fields) {
            if (f.isAnnotationPresent(SaveState.class))
                fieldsToSave.add(f);
        }
        Field[] declaredFields = getClass().getDeclaredFields();
        for (Field f : declaredFields) {
            if (f.isAnnotationPresent(SaveState.class))
                fieldsToSave.add(f);
        }

        List<Method> methodsToSave = new ArrayList<Method>();
        Method[] methods = getClass().getMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(SaveState.class) && m.getName().startsWith("get"))
                methodsToSave.add(m);
        }
        Method[] declaredMethods = getClass().getDeclaredMethods();
        for (Method m : declaredMethods) {
            if (m.isAnnotationPresent(SaveState.class) && m.getName().startsWith("get"))
                methodsToSave.add(m);
        }

        savedObjects.clear();
        for (Field field : fieldsToSave) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                savedObjects.put(field.getName(), value);
            } catch (IllegalAccessException e) {
            }
        }
        for (Method method : methodsToSave) {
            try {
                method.setAccessible(true);
                Object value = method.invoke(this);
                savedObjects.put(method.getName().substring(3), value);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        restoreState();
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        restoreState();
        super.onCreate(savedInstanceState);
    }

    private void restoreState() {
        for (String s : savedObjects.keySet()) {
            Field field = null;
            Method method = null;

            try {
                field = getClass().getField(s);
            } catch (NoSuchFieldException e) {
                try {
                    field = getClass().getDeclaredField(s);
                } catch (NoSuchFieldException e2) {
                    continue;
                }
            }

            try {
                method = getClass().getMethod("set" + Character.toUpperCase(s.charAt(0)) + s.substring(1), savedObjects.get(s).getClass());
            } catch (NoSuchMethodException e) {
                try {
                    method = getClass().getDeclaredMethod("set" + Character.toUpperCase(s.charAt(0)) + s.substring(1), savedObjects.get(s).getClass());
                } catch (NoSuchMethodException e1) {
                }
            }

            try {
                if (method != null) {
                    method.invoke(this, savedObjects.get(s));
                } else {
                    field.setAccessible(true);
                    field.set(this, savedObjects.get(s));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
