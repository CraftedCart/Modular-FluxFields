package io.github.craftedcart.MFF.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CraftedCart on 21/11/2015 (DD/MM/YYYY)
 */

public class TEPowerCube extends TileEntity implements IUpdatePlayerListBox {

    public double power = 100000000;

    private int updateTime = 1;
    public List powerCubeLinks = new ArrayList<BlockPos>();

    @Override
    public void update() {

        updateTime--;

        if (updateTime <= 0) {
            updateTime = 100; //5s

            //Send info to client
            worldObj.markBlockForUpdate(this.getPos());
            markDirty();

            //Fetch nearby Power Spheres
            powerCubeLinks.clear();
            for (int x = -16; x <= 16; x++) {
                for (int y = -16; y <= 16; y++) {
                    for (int z = -16; z <= 16; z++) {
                        if (worldObj.getTileEntity(this.getPos().add(x, y, z)) != null &&
                                new BlockPos(x, y, z).add(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()) != this.getPos()) {
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
                    try {
                        Field f = pc.getClass().getField("power");
                        double pcPower = f.getDouble(pc);
                        if (pcPower < power) {
                            powerCubeLinksTotalPower += pcPower;
                            powerCubeLinksToTransferEnergyTo.add(pos);
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                obj.remove();
            }
        }

        double averagePower = (powerCubeLinksTotalPower + power) / (powerCubeLinksToTransferEnergyTo.size() + 1);

        for (Iterator<Object> obj = powerCubeLinksToTransferEnergyTo.listIterator(); obj.hasNext();) {
            BlockPos pos = (BlockPos) obj.next();
            if (worldObj.getTileEntity(pos) != null) {
                if (worldObj.getTileEntity(pos) instanceof TEPowerCube) {
                    TEPowerCube pc = (TEPowerCube) worldObj.getTileEntity(pos);
                    try {
                        Field f = pc.getClass().getField("power");
                        power = averagePower;
                        f.setDouble(pc, averagePower);

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                obj.remove();
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);

        writeSyncableDataToNBT(tagCompound);

        // ... Continue writing non-syncable data
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);

        readSyncableDataFromNBT(tagCompound);

        // ... Continue reading non-syncable data
    }

    void writeSyncableDataToNBT(NBTTagCompound tagCompound) {
        tagCompound.setDouble("power", power);
    }

    void readSyncableDataFromNBT(NBTTagCompound tagCompound) {
        power = tagCompound.getDouble("power");

    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound syncData = new NBTTagCompound();
        this.writeSyncableDataToNBT(syncData);
        return new S35PacketUpdateTileEntity(this.getPos(), 1, syncData);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readSyncableDataFromNBT(pkt.getNbtCompound());
    }

}
