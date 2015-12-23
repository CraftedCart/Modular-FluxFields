package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;

/**
 * Created by CraftedCart on 23/12/2015 (DD/MM/YYYY)
 */

public class TEPowerGenerator extends TileEntity implements IUpdatePlayerListBox {

    private double power = 0;
    private double maxPower;
    private double sendRate;

    public void init(double maxPower, double sendRate) {
        this.maxPower = maxPower;
        this.sendRate = sendRate;
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

        if (pcPower < PowerConf.powerCubeMaxPower) {
            if (power >= sendRate) {

                if (pcPower + sendRate <= PowerConf.powerCubeMaxPower) {
                    power -= sendRate;
                    powerCube.power += sendRate;
                } else {
                    power -= PowerConf.powerCubeMaxPower - powerCube.power;
                    powerCube.power = PowerConf.powerCubeMaxPower;
                }

            } else {

                if (pcPower + power <= PowerConf.powerCubeMaxPower) {
                    power = 0;
                    powerCube.power += power;
                } else {
                    power -= PowerConf.powerCubeMaxPower - powerCube.power;
                    powerCube.power = PowerConf.powerCubeMaxPower;
                }

            }
        }
    }
}
