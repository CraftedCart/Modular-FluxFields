package io.github.craftedcart.MFF.tileentity;

/**
 * Created by CraftedCart on 24/12/2015 (DD/MM/YYYY)
 */

public class TECreativePowerGenerator extends TEPowerGenerator {

    @Override
    public void update() {

        this.power = 100000000;
        super.update();

    }

}
