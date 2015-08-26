package com.pyr0g3ist.saxumcore.util;

import java.awt.Color;

public class ColorUtil {
    
    public static Color getFractionColor(Color startColor, Color endColor, double fraction) {
        if (fraction > 1) {
            fraction = 1;
        } else if (fraction < 0) {
            fraction = 0;
        }
        int red = (int) (fraction * endColor.getRed() + (1 - fraction) * startColor.getRed());
        int green = (int) (fraction * endColor.getGreen() + (1 - fraction) * startColor.getGreen());
        int blue = (int) (fraction * endColor.getBlue() + (1 - fraction) * startColor.getBlue());
        return new Color(red, green, blue);
    }
    
}
