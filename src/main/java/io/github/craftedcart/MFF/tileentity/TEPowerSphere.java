package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 21/11/2015 (DD/MM/YYYY)
 */

public class TEPowerSphere extends TileEntity implements IUpdatePlayerListBox {

    public int power = 100000000;

    private int updateTime = 1;
    public List powerSphereLinks = new ArrayList<BlockPos>();

    @Override
    public void update() {

        updateTime--;

        if (updateTime <= 0) {
            updateTime = 100; //5s

            //Send info to client
            worldObj.markBlockForUpdate(this.getPos());
            markDirty();

            //Fetch nearby Power Spheres
            powerSphereLinks.clear();
            for (int x = -16; x <= 16; x++) {
                for (int y = -16; y <= 16; y++) {
                    for (int z = -16; z <= 16; z++) {
                        if (worldObj.getTileEntity(this.getPos().add(x, y, z)) != null &&
                                new BlockPos(x, y, z).add(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()) != this.getPos()) {
                            if (worldObj.getTileEntity(this.getPos().add(x, y, z)) instanceof TEPowerSphere) {
                                //We found another Power Sphere in a radius of 32 blocks
                                powerSphereLinks.add(this.getPos().add(x, y, z));
                            }
                        }
                    }
                }
            }

        }

        for (Object obj : powerSphereLinks) {

            BlockPos pos = (BlockPos) obj;

            if (worldObj.getTileEntity(pos) != null) {
                if (worldObj.getTileEntity(pos) instanceof TEPowerSphere) {

                    TEPowerSphere ps = (TEPowerSphere) worldObj.getTileEntity(pos);

                    try {

                        Field f = ps.getClass().getField("power");
                        int psPower = f.getInt(ps);
                        int powerDiff = power - psPower;

                        if (powerDiff > 0) {
                            //Send some power

                            if (psPower + power / 10 > PowerConf.powerSphereMaxPower) {
                                power -= psPower - power;
                                f.setInt(ps, PowerConf.powerSphereMaxPower);
                            } else {
                                f.setInt(ps, psPower + power / 10);
                                power -= power / 10;
                            }

                        } //TODO: Better power balancing system

                        if (power < 0) {
                            power = 0;
                        } //TODO: This is a workaround for a bug D:

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }
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
        tagCompound.setInteger("power", power);
    }

    void readSyncableDataFromNBT(NBTTagCompound tagCompound) {
        power = tagCompound.getInteger("power");

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
