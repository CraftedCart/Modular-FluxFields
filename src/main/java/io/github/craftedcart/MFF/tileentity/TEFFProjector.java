package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.init.Blocks;
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
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEFFProjector extends TileEntity implements IUpdatePlayerListBox {

    //Config-y stuff
    public int minX = -5;
    public int maxX = 5;
    public int minY = -5;
    public int maxY = 5;
    public int minZ = -5;
    public int maxZ = 5;

    public double power = 0;

    //Not so config-y stuff
    private int updateTime = 100;
    List<BlockPos> blockList = new ArrayList<BlockPos>();

    public void getBlocks() {

        BlockPos pos = this.getPos();
        blockList.clear();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                blockList.add(pos.add(x, y, minZ));
                blockList.add(pos.add(x, y, maxZ));
            }
        }

        for (int z = minZ + 1; z <= maxZ - 1; z++) {
            for (int y = minY; y <= maxY; y++) {
                blockList.add(pos.add(minX, y, z));
                blockList.add(pos.add(maxX, y, z));
            }
        }

        for (int x = minX + 1; x <= maxX - 1; x++) {
            for (int z = minZ + 1; z <= maxZ - 1; z++) {
                blockList.add(pos.add(x, maxY, z));
                blockList.add(pos.add(x, minY, z));
            }
        }

    }

    @Override
    public void update() {

        updateTime--;

        //Use 2 power/block/t
        if (power >= PowerConf.ffProjectorUsagePerBlock * blockList.size()) {
            power -= PowerConf.ffProjectorUsagePerBlock * blockList.size();
        }

        if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) != null) {
            if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) instanceof TEPowerCube) {
                drawPower((TEPowerCube) worldObj.getTileEntity(this.getPos().add(0, 1, 0)));
            }
        }

        //Executed every 4.95s (99t)
        if (updateTime <= 0) {
            updateTime = 99; //4.95s (99t)

            //Send info to client
            worldObj.markBlockForUpdate(this.getPos());
            markDirty();

            getBlocks();

            for (BlockPos ffPos : blockList) {

                if (worldObj.getBlockState(ffPos) == Blocks.air.getDefaultState()) {
                    if (power >= PowerConf.ffProjectorUsagePerBlockToGenerate) {
                        worldObj.setBlockState(ffPos, ModBlocks.forcefield.getDefaultState());
                        power -= PowerConf.ffProjectorUsagePerBlockToGenerate;
                    }
                } else if (worldObj.getBlockState(ffPos) == ModBlocks.forcefield.getDefaultState()) {
                    if (power >= PowerConf.ffProjectorUsagePerBlock * blockList.size()) {
                        refreshFFTimer(ffPos);
                    }
                }

            }

        }

    }

    //Refresh the forcefield decay timer
    private void refreshFFTimer(BlockPos ffPos) {

        TileEntity te = worldObj.getTileEntity(ffPos);
        try {
            Field f = te.getClass().getField("decayTimer");
            f.setInt(te, 100); //5s
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            LogHelper.warn("Gah! Error when trying to refresh the decayTimer of the Forcefield at " + ffPos.toString() + " - Make sure that the chunk is loaded!");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //Draw power from a Power Sphere
    private void drawPower(TEPowerCube powerCube) {

        double powerDrawRate = PowerConf.ffProjectorDrawRate;
        double powerMax = PowerConf.ffProjectorMaxPower;

        if (power < powerMax) {

            double psPower; //Power Sphere Power

            try {

                Field f = powerCube.getClass().getField("power");
                psPower = f.getDouble(powerCube);

                if (psPower > 0) {
                    if (psPower < powerDrawRate) {
                        if (power + psPower <= powerMax) {
                            power += psPower;
                            f.setDouble(powerCube, 0);
                        } else {
                            f.setDouble(powerCube, powerMax - power);
                            power = powerMax;
                        }
                    } else {
                        if (power + powerDrawRate <= powerMax) {
                            power += powerDrawRate;
                            f.setDouble(powerCube, psPower - powerDrawRate);
                        } else {
                            f.setDouble(powerCube, psPower - (powerMax - power));
                            power = powerMax;
                        }
                    }
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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

        tagCompound.setInteger("x1", minX);
        tagCompound.setInteger("y1", minY);
        tagCompound.setInteger("z1", minZ);
        tagCompound.setInteger("x2", maxX);
        tagCompound.setInteger("y2", maxY);
        tagCompound.setInteger("z2", maxZ);
    }

    void readSyncableDataFromNBT(NBTTagCompound tagCompound) {
        power = tagCompound.getDouble("power");

        minX = tagCompound.getInteger("x1");
        minY = tagCompound.getInteger("y1");
        minZ = tagCompound.getInteger("z1");
        maxX = tagCompound.getInteger("x2");
        maxY = tagCompound.getInteger("y2");
        maxZ = tagCompound.getInteger("z2");

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
