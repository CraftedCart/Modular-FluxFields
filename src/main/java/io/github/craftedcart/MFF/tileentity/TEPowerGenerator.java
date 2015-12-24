package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by CraftedCart on 23/12/2015 (DD/MM/YYYY)
 */

public class TEPowerGenerator extends TileEntity implements IUpdatePlayerListBox {

    public double power = 0;
    public double maxPower;

    public void init(double maxPower) {
        this.maxPower = maxPower;
    }

    @Override
    public void update() {

        if (maxPower == -1) { //If it's a creative power generator
            power = 100000000;
        }

        //Send power to below Power Cube
        if (worldObj.getTileEntity(this.getPos().add(0, -1, 0)) != null) {
            if (worldObj.getTileEntity(this.getPos().add(0, -1, 0)) instanceof TEPowerCube) {
                sendPower((TEPowerCube) worldObj.getTileEntity(this.getPos().add(0, -1, 0)));
            }
        }

    }

    private void sendPower(TEPowerCube powerCube) {

        double pcPower = powerCube.power;

        if (pcPower != PowerConf.powerCubeMaxPower && power > 0) {

            if (pcPower + power <= PowerConf.powerCubeMaxPower) {
                powerCube.power += power;
                power = 0;
            } else {
                power -= PowerConf.powerCubeMaxPower - pcPower;
                powerCube.power = PowerConf.powerCubeMaxPower;
            }

        }

    }

    //TODO: Sync Data, Add GUI, Add Container, Add Upgrade Support

}
