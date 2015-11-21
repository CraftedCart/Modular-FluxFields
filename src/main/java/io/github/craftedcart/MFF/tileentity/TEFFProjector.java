package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEFFProjector extends TileEntity implements IUpdatePlayerListBox {

    //Config-y stuff
    private int minX = -5;
    private int maxX = 5;
    private int minY = -5;
    private int maxY = 5;
    private int minZ = -5;
    private int maxZ = 5;

    private int power = 0;

    //Not so config-y stuff
    private int updateTime = 100;
    private boolean doSetup = true;
    List<BlockPos> blockList = new ArrayList<BlockPos>();

    private void getBlocks() {

        BlockPos pos = this.getPos();

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

        for (int x = minZ + 1; x <= maxZ - 1; x++) {
            for (int z = minY; z <= maxY; z++) {
                blockList.add(pos.add(x, maxY, z));
                blockList.add(pos.add(x, minY, z));
            }
        }

    }

    @Override
    public void update() {

        if (doSetup) {
            getBlocks();

            doSetup = false;
        }

        updateTime--;

        //Use 2 power/block/t
        if (power >= 2 * blockList.size()) {
            power -= 2 * blockList.size();
        }

        if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) != null) {
            if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) instanceof TEPowerSphere) {
                drawPower((TEPowerSphere) worldObj.getTileEntity(this.getPos().add(0, 1, 0)));
            }
        }

        //Executed every 4.5s (99t)
        if (updateTime <= 0) {
            updateTime = 99; //4.95s (99t)

            //Send info to client
            worldObj.markBlockForUpdate(this.getPos());
            markDirty();


            for (BlockPos ffPos : blockList) {

                if (worldObj.getBlockState(ffPos) == Blocks.air.getDefaultState()) {
                    if (power >= 500) {
                        worldObj.setBlockState(ffPos, ModBlocks.forcefield.getDefaultState());
                        power -= 500;
                    }
                } else if (worldObj.getBlockState(ffPos) == ModBlocks.forcefield.getDefaultState()) {
                    if (power >= 50) {
                        refreshFFTimer(ffPos);
                        power -= 50;
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
    private void drawPower(TEPowerSphere powerSphere) {

        int powerDrawRate = PowerConf.ffProjectorDrawRate;
        int powerMax = PowerConf.ffProjectorMaxPower;

        if (power < powerMax) {

            int psPower; //Power Sphere Power

            try {

                Field f = powerSphere.getClass().getField("power");
                psPower = f.getInt(powerSphere);

                if (psPower > 0) {
                    if (psPower < powerDrawRate) {
                        if (power + psPower <= powerMax) {
                            power += psPower;
                            f.setInt(powerSphere, 0);
                        } else {
                            f.setInt(powerSphere, powerMax - power);
                            power = powerMax;
                        }
                    } else {
                        if (power + powerDrawRate <= powerMax) {
                            power += powerDrawRate;
                            f.setInt(powerSphere, psPower - powerDrawRate);
                        } else {
                            f.setInt(powerSphere, psPower - (powerMax - power));
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
