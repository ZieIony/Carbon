package carbon.widget;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcin on 2014-11-07.
 */
public class Roboto {
    public enum Style {
        Black,
        BlackItalic,
        Bold,
        BoldItalic,
        Italic,
        Light,
        LightItalic,
        Medium,
        MediumItalic,
        Regular,
        Thin,
        ThinItalic,
        CondensedBold,
        CondensedBoldItalic,
        CondensedItalic,
        CondensedLight,
        CondensedLightItalic,
        CondensedRegular,
    }

    static Map<Style, String> typefacePaths = new HashMap<Style, String>();

    static {
        typefacePaths.put(Style.Black, "Roboto-Black.ttf");
        typefacePaths.put(Style.BlackItalic, "Roboto-BlackItalic.ttf");
        typefacePaths.put(Style.Bold, "Roboto-Bold.ttf");
        typefacePaths.put(Style.BoldItalic, "Roboto-BoldItalic.ttf");
        typefacePaths.put(Style.Italic, "Roboto-Italic.ttf");
        typefacePaths.put(Style.Light, "Roboto-Light.ttf");
        typefacePaths.put(Style.LightItalic, "Roboto-LightItalic.ttf");
        typefacePaths.put(Style.Medium, "Roboto-Medium.ttf");
        typefacePaths.put(Style.MediumItalic, "Roboto-MediumItalic.ttf");
        typefacePaths.put(Style.Regular, "Roboto-Regular.ttf");
        typefacePaths.put(Style.Thin, "Roboto-Thin.ttf");
        typefacePaths.put(Style.ThinItalic, "Roboto-ThinItalic.ttf");

        typefacePaths.put(Style.CondensedBold, "RobotoCondensed-Bold.ttf");
        typefacePaths.put(Style.CondensedBoldItalic, "RobotoCondensed-BoldItalic.ttf");
        typefacePaths.put(Style.CondensedItalic, "RobotoCondensed-Italic.ttf");
        typefacePaths.put(Style.CondensedLight, "RobotoCondensed-Light.ttf");
        typefacePaths.put(Style.CondensedLightItalic, "RobotoCondensed-LightItalic.ttf");
        typefacePaths.put(Style.CondensedRegular, "RobotoCondensed-Regular.ttf");
    }

    public static android.graphics.Typeface getTypeface(Context context, Style style) {
        return android.graphics.Typeface.createFromAsset(context.getAssets(), typefacePaths.get(style));
    }
}
