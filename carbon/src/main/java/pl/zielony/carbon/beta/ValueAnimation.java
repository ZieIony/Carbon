package pl.zielony.carbon.beta;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Marcin on 2014-11-15.
 */
public class ValueAnimation extends Animation {
    private final float from;
    private final float to;
    private final Object obj;
    private Field field;
    private Method setter;

    public ValueAnimation(float from, float to, Object obj, String field) {
        this.from = from;
        this.to = to;
        this.obj = obj;
        setFillEnabled(true);
        setFillAfter(true);
        setFillBefore(true);
        String setterName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
        try {
            setter = obj.getClass().getMethod(setterName, float.class);
        } catch (NoSuchMethodException e) {
            try {
                setter = obj.getClass().getDeclaredMethod(setterName, float.class);
                setter.setAccessible(true);
            } catch (NoSuchMethodException e1) {
                try {
                    this.field = obj.getClass().getField(field);
                } catch (NoSuchFieldException e2) {
                    try {
                        this.field = obj.getClass().getDeclaredField(field);
                        this.field.setAccessible(true);
                    } catch (NoSuchFieldException e3) {
                        Log.e("", "Value Animation was unable to find " + field + " in " + obj.getClass().getSimpleName());
                        //e1.printStackTrace();
                    }
                    //e.printStackTrace();
                }
                //e1.printStackTrace();
            }
            //e.printStackTrace();
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float value = from * (1 - interpolatedTime) + to * interpolatedTime;
        try {
            if (setter != null) {
                setter.invoke(obj, value);
            } else {
                field.set(obj, value);
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //super.applyTransformation(interpolatedTime, t);
    }
}
