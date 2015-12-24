package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CraftedCart on 24/12/2015 (DD/MM/YYYY)
 */

public class TEPowerGenerator extends TileEntity implements IUpdatePlayerListBox {

    public double power = 0;
    public double maxPower;
    public boolean isSendingPower;

    public void init(double maxPower) {
        this.maxPower = maxPower;
    }

    public void update() {

        //Send power to below Power Cube
        if (worldObj.getTileEntity(this.getPos().add(0, -1, 0)) != null) {
            if (worldObj.getTileEntity(this.getPos().add(0, -1, 0)) instanceof TEPowerCube) {
                sendPower((TEPowerCube) worldObj.getTileEntity(this.getPos().add(0, -1, 0)));
            }
        }

    }

    protected void sendPower(TEPowerCube powerCube) {

        double pcPower = powerCube.power;

        if (pcPower != PowerConf.powerCubeMaxPower && power > 0) {
            isSendingPower = true;

            if (pcPower + power <= PowerConf.powerCubeMaxPower) {
                powerCube.power += power;
                power = 0;
            } else {
                power -= PowerConf.powerCubeMaxPower - pcPower;
                powerCube.power = PowerConf.powerCubeMaxPower;
            }

        } else {
            isSendingPower = false;
        }

    }

}
