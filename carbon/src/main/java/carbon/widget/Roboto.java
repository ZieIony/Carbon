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

    static Map<Style, String> typefacePaths = new HashMap<>();

    static {
        typefacePaths.put(Style.Black, "carbon/Roboto-Black.ttf");
        typefacePaths.put(Style.BlackItalic, "carbon/Roboto-BlackItalic.ttf");
        typefacePaths.put(Style.Bold, "carbon/Roboto-Bold.ttf");
        typefacePaths.put(Style.BoldItalic, "carbon/Roboto-BoldItalic.ttf");
        typefacePaths.put(Style.Italic, "carbon/Roboto-Italic.ttf");
        typefacePaths.put(Style.Light, "carbon/Roboto-Light.ttf");
        typefacePaths.put(Style.LightItalic, "carbon/Roboto-LightItalic.ttf");
        typefacePaths.put(Style.Medium, "carbon/Roboto-Medium.ttf");
        typefacePaths.put(Style.MediumItalic, "carbon/Roboto-MediumItalic.ttf");
        typefacePaths.put(Style.Regular, "carbon/Roboto-Regular.ttf");
        typefacePaths.put(Style.Thin, "carbon/Roboto-Thin.ttf");
        typefacePaths.put(Style.ThinItalic, "carbon/Roboto-ThinItalic.ttf");

        typefacePaths.put(Style.CondensedBold, "carbon/RobotoCondensed-Bold.ttf");
        typefacePaths.put(Style.CondensedBoldItalic, "carbon/RobotoCondensed-BoldItalic.ttf");
        typefacePaths.put(Style.CondensedItalic, "carbon/RobotoCondensed-Italic.ttf");
        typefacePaths.put(Style.CondensedLight, "carbon/RobotoCondensed-Light.ttf");
        typefacePaths.put(Style.CondensedLightItalic, "carbon/RobotoCondensed-LightItalic.ttf");
        typefacePaths.put(Style.CondensedRegular, "carbon/RobotoCondensed-Regular.ttf");
    }

    public static android.graphics.Typeface getTypeface(Context context, Style style) {
        return android.graphics.Typeface.createFromAsset(context.getAssets(), typefacePaths.get(style));
    }
}
