package io.github.craftedcart.MFF.tileentity;

import net.minecraft.util.EnumFacing;

/**
 * Created by CraftedCart on 25/02/2016 (DD/MM/YYYY)
 */

public class TEPowerRelay extends TEPoweredBlock {

    public EnumFacing inputSide = EnumFacing.NORTH;

    @Override
    public void init() {
        super.init();

        setup(1, 1);
    }

}
