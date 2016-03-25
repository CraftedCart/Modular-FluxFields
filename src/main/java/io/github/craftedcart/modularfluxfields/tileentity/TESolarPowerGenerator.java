package io.github.craftedcart.modularfluxfields.tileentity;

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

        super.update();

        if (((worldObj.getWorldTime() % 24000 >= 0 && worldObj.getWorldTime() % 24000 <= 13000) || worldObj.getWorldTime() % 24000 >= 23500) && worldObj.getLightFor(EnumSkyBlock.SKY, this.pos) > 7) {
            if (power + genRate <= maxPower) {
                power += genRate;
            } else {
                power = maxPower;
            }
        }

    }
}
