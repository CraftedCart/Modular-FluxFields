package io.github.craftedcart.modularfluxfields.utility;

import io.github.craftedcart.modularfluxfields.client.gui.guiutils.UIColor;

import java.util.Random;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */
public class MathUtils {

//    public static int randInt(int min, int max) {
//        Random rand = new Random();
//        return rand.nextInt(max - min + 1) + min;
//    }

    public static double randDouble(double min, double max) {
        Random rand = new Random();
        return min + (max - min) * rand.nextDouble();
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static double lerp(double a, double b, double f) {
        return a + f * (b - a);
    }

    public static UIColor lerpUIColor(UIColor a, UIColor b, float f) {
        return new UIColor(a.r * 255 + f * (b.r * 255 - a.r * 255), a.g * 255 + f * (b.g * 255 - a.g * 255), a.b * 255 + f * (b.b * 255 - a.b * 255), a.a * 255 + f * (b.a * 255 - a.a * 255));
    }

}
