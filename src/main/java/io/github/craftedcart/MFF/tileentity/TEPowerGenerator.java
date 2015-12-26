package io.github.craftedcart.MFF.tileentity;

import net.minecraft.server.gui.IUpdatePlayerListBox;

/**
 * Created by CraftedCart on 24/12/2015 (DD/MM/YYYY)
 */

public class TEPowerGenerator extends TEPoweredBlock implements IUpdatePlayerListBox {

    @Override
    public void update() {

        super.update();

        //Send power to below Power Cube or below Power Generator
        if (worldObj.getTileEntity(this.getPos().add(0, -1, 0)) != null) {
            if (worldObj.getTileEntity(this.getPos().add(0, -1, 0)) instanceof TEPowerCube ||
                    worldObj.getTileEntity(this.getPos().add(0, -1, 0)) instanceof TEPowerGenerator) {
                sendPower((TEPoweredBlock) worldObj.getTileEntity(this.getPos().add(0, -1, 0)));
            }
        }

    }

}
