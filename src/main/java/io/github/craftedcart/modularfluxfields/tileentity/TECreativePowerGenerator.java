package io.github.craftedcart.modularfluxfields.tileentity;

/**
 * Created by CraftedCart on 24/12/2015 (DD/MM/YYYY)
 */
public class TECreativePowerGenerator extends TEPowerGenerator {

    @Override
    public void update() {

        super.update();
        this.power = 100000000;

    }

}
