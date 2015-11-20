package io.github.craftedcart.MFF.tileentiry;

import io.github.craftedcart.MFF.init.ModBlocks;
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

    private int power = 1000000000; //1 Billion

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

            //Warn the player of incoming lag!
            BlockPos pos = this.getPos();
            List playerList = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(pos.getX() - 256, pos.getY() - 256, pos.getZ() - 256,
                    pos.getX() + 256, pos.getY() + 256, pos.getZ() + 256));
            for (Object objPlayer : playerList) {
                EntityPlayer player = (EntityPlayer) objPlayer;
                if (!player.worldObj.isRemote) {
                    player.addChatMessage(new ChatComponentText("§eMFF WARNING: A nearby forcefield is being generated in §c5s"));
                    player.addChatMessage(new ChatComponentText("§eMFF WARNING: The forcefield size is §c" + blockList.size()));
                    player.addChatMessage(new ChatComponentText("§eMFF WARNING: Expect some lag for a few moments"));
                    player.addChatMessage(new ChatComponentText("§eMFF WARNING: It is normal for Minecraft to freeze"));
                }
            }

            doSetup = false;
        }

        updateTime--;

        if (power >= 10 * blockList.size()) {
            power -= 10 * blockList.size();
        }

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

        //Tell nearby players of the current power level
        //Debugging!
        BlockPos pos = this.getPos();
        List playerList = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(pos.getX() - 256, pos.getY() - 256, pos.getZ() - 256,
                pos.getX() + 256, pos.getY() + 256, pos.getZ() + 256));
        for (Object objPlayer : playerList) {
            EntityPlayer player = (EntityPlayer) objPlayer;
            //if (!player.worldObj.isRemote) {
                player.addChatMessage(new ChatComponentText("§ePower: §b" + power));
            //}
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
