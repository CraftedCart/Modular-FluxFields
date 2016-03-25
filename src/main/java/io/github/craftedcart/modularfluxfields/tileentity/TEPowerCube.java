package io.github.craftedcart.modularfluxfields.tileentity;

import io.github.craftedcart.modularfluxfields.reference.PowerConf;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CraftedCart on 21/11/2015 (DD/MM/YYYY)
 */
public class TEPowerCube extends TEPoweredBlock implements IUpdatePlayerListBox {

    private boolean doneWorldSetup = false;
    private int updateTime = 1;
    public List powerCubeLinks = new ArrayList<BlockPos>();

    @SuppressWarnings("unchecked")
    @Override
    public void update() {

        super.update();

        if (!doneWorldSetup) {
            setup(PowerConf.powerCubeMaxPower);
            doneWorldSetup = true;
        }

        updateTime--;

        if (updateTime <= 0) {
            updateTime = 100; //5s

            //Fetch nearby Power Spheres
            powerCubeLinks.clear();
            for (int x = -16; x <= 16; x++) {
                for (int y = -16; y <= 16; y++) {
                    for (int z = -16; z <= 16; z++) {
                        if (worldObj.getTileEntity(this.getPos().add(x, y, z)) != null &&
                                !new BlockPos(x, y, z).add(this.getPos()).equals(this.getPos())) {
                            if (worldObj.getTileEntity(this.getPos().add(x, y, z)) instanceof TEPowerCube) {
                                //We found another Power Sphere in a radius of 32 blocks
                                powerCubeLinks.add(this.getPos().add(x, y, z));
                            }
                        }
                    }
                }
            }

        }

        //Get total power of connected Power Cubes
        double powerCubeLinksTotalPower = 0;
        List powerCubeLinksToTransferEnergyTo = new ArrayList<BlockPos>();
        for (Iterator<Object> obj = powerCubeLinks.listIterator(); obj.hasNext();) {
            BlockPos pos = (BlockPos) obj.next();
            if (worldObj.getTileEntity(pos) != null) {
                if (worldObj.getTileEntity(pos) instanceof TEPowerCube) {
                    TEPowerCube pc = (TEPowerCube) worldObj.getTileEntity(pos);
                    double pcPower = pc.power;
                    if (pcPower < power) {
                        powerCubeLinksTotalPower += pcPower;
                        powerCubeLinksToTransferEnergyTo.add(pos);
                    }
                }
            } else {
                obj.remove();
            }
        }

        double averagePower = (powerCubeLinksTotalPower + power) / (powerCubeLinksToTransferEnergyTo.size() + 1);

        power = averagePower;

        for (Iterator<Object> obj = powerCubeLinksToTransferEnergyTo.listIterator(); obj.hasNext();) {
            BlockPos pos = (BlockPos) obj.next();
            if (worldObj.getTileEntity(pos) != null) {
                if (worldObj.getTileEntity(pos) instanceof TEPowerCube) {
                    TEPowerCube pc = (TEPowerCube) worldObj.getTileEntity(pos);
                    pc.power = averagePower;
                }
            } else {
                obj.remove();
            }
        }

    }

}
