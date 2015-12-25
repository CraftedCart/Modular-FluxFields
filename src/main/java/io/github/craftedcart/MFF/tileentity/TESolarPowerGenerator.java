package io.github.craftedcart.MFF.tileentity;

import net.minecraft.world.EnumSkyBlock;

/**
 * Created by CraftedCart on 24/12/2015 (DD/MM/YYYY)
 */

public class TESolarPowerGenerator extends TEPowerGenerator {

    private double genRate;

    public void initSolar(double genRate) {
        this.genRate = genRate;
    }

    @Override
    public void update() {

        if (worldObj.isDaytime() && worldObj.getLightFor(EnumSkyBlock.SKY, this.pos) > 7) {
            power += genRate;
        }

        super.update();

    }
}
