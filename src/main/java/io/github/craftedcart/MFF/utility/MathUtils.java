package io.github.craftedcart.MFF.utility;

import java.util.Random;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class MathUtils {

    public static int randInt(int min, int max) {

        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

}
