package carbon.widget;

import android.content.Context;

/**
 * Created by Marcin on 2014-11-07.
 */
public class Roboto {
    public enum Style {
        Black {
            @Override
            String typefacePath() {
                return "carbon/Roboto-Black.ttf";
            }
        },
        BlackItalic {
            @Override
            String typefacePath() {
                return "carbon/Roboto-BlackItalic.ttf";
            }
        },
        Bold {
            @Override
            String typefacePath() {
                return "carbon/Roboto-Bold.ttf";
            }
        },
        BoldItalic {
            @Override
            String typefacePath() {
                return "carbon/Roboto-BoldItalic.ttf";
            }
        },
        Italic {
            @Override
            String typefacePath() {
                return "carbon/Roboto-Italic.ttf";
            }
        },
        Light {
            @Override
            String typefacePath() {
                return "carbon/Roboto-Light.ttf";
            }
        },
        LightItalic {
            @Override
            String typefacePath() {
                return "carbon/Roboto-LightItalic.ttf";
            }
        },
        Medium {
            @Override
            String typefacePath() {
                return "carbon/Roboto-Medium.ttf";
            }
        },
        MediumItalic {
            @Override
            String typefacePath() {
                return "carbon/Roboto-MediumItalic.ttf";
            }
        },
        Regular {
            @Override
            String typefacePath() {
                return "carbon/Roboto-Regular.ttf";
            }
        },
        Thin {
            @Override
            String typefacePath() {
                return "carbon/Roboto-Thin.ttf";
            }
        },
        ThinItalic {
            @Override
            String typefacePath() {
                return "carbon/Roboto-ThinItalic.ttf";
            }
        },
        CondensedBold {
            @Override
            String typefacePath() {
                return "carbon/RobotoCondensed-Bold.ttf";
            }
        },
        CondensedBoldItalic {
            @Override
            String typefacePath() {
                return "carbon/RobotoCondensed-BoldItalic.ttf";
            }
        },
        CondensedItalic {
            @Override
            String typefacePath() {
                return "carbon/RobotoCondensed-Italic.ttf";
            }
        },
        CondensedLight {
            @Override
            String typefacePath() {
                return "carbon/RobotoCondensed-Light.ttf";
            }
        },
        CondensedLightItalic {
            @Override
            String typefacePath() {
                return "carbon/RobotoCondensed-LightItalic.ttf";
            }
        },
        CondensedRegular {
            @Override
            String typefacePath() {
                return "carbon/RobotoCondensed-Regular.ttf";
            }
        };

        abstract String typefacePath();
    }

    public static android.graphics.Typeface getTypeface(Context context, Style style) {
        return android.graphics.Typeface.createFromAsset(context.getAssets(), style.typefacePath());
    }
}
