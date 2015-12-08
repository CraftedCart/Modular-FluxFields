package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.eventhandler.PreventFFBlockBreak;
import io.github.craftedcart.MFF.init.ModBlocks;
import io.github.craftedcart.MFF.reference.PowerConf;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 18/11/2015 (DD/MM/YYYY)
 */

public class TEFFProjector extends TileEntity implements IUpdatePlayerListBox, IInventory {

    //Config-y stuff
    public int minX = -5;
    public int maxX = 5;
    public int minY = -5;
    public int maxY = 5;
    public int minZ = -5;
    public int maxZ = 5;

    //Upgrades
    public boolean hasSecurityUpgrade = true;

    //Not so config-y stuff
    private int updateTime = 100;
    public ArrayList<BlockPos> blockList = new ArrayList<BlockPos>(); //The list of blockposes of the forcefield walls
    private boolean doWorldLoadSetup = false; //Used to make sure a block of code only runs once on chunk load
    public boolean isPowered = false; //Does the FF Projector has enough power to keep running
    private ItemStack[] inventory; //The inventory of the FF Projector
    private String customName;
    public double power = 0;
    public int uptime = 0;
    public String owner = ""; //The owner UUID
    public String ownerName = ""; //The owner username
    public List<List<String>> permittedPlayers = new ArrayList<List<String>>(); //The list of permitted players defined by the security tab on the gui

    public TEFFProjector() {
        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index < 0 || index >= this.getSizeInventory())
            return null;
        return this.inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.getStackInSlot(index) != null) {
            ItemStack itemstack;

            if (this.getStackInSlot(index).stackSize <= count) {
                itemstack = this.getStackInSlot(index);
                this.setInventorySlotContents(index, null);
                this.markDirty();
                return itemstack;
            } else {
                itemstack = this.getStackInSlot(index).splitStack(count);

                if (this.getStackInSlot(index).stackSize <= 0) {
                    this.setInventorySlotContents(index, null);
                } else {
                    //Just to show that changes happened
                    this.setInventorySlotContents(index, this.getStackInSlot(index));
                }

                this.markDirty();
                return itemstack;
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        ItemStack stack = this.getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= this.getSizeInventory())
            return;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();

        if (stack != null && stack.stackSize == 0)
            stack = null;

        this.inventory[index] = stack;
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        //NO-OP
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        //NO-OP
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
        //NO-OP
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSizeInventory(); i++)
            this.setInventorySlotContents(i, null);
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.mff:ffProjector";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        writeSyncableDataToNBT(nbt);

        //Inventory stuff
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (this.getStackInSlot(i) != null) {
                NBTTagCompound stackTag = new NBTTagCompound();
                stackTag.setByte("Slot", (byte) i);
                this.getStackInSlot(i).writeToNBT(stackTag);
                list.appendTag(stackTag);
            }
        }
        nbt.setTag("Items", list);

        if (this.hasCustomName()) {
            nbt.setString("CustomName", this.getCustomName());
        }

    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        readSyncableDataFromNBT(nbt);

        //Inventory Stuff
        NBTTagList list = nbt.getTagList("Items", 10);
        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound stackTag = list.getCompoundTagAt(i);
            int slot = stackTag.getByte("Slot") & 255;
            this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
        }

        if (nbt.hasKey("CustomName", 8)) {
            this.setCustomName(nbt.getString("CustomName"));
        }

    }

    public void getBlocks() {

        BlockPos pos = this.getPos(); //Get this projector's position
        blockList.clear(); //Clear the list of blockposes of the forcefield walls

        for (int x = minX; x <= maxX; x++) { //Calculate the +/- Z walls
            for (int y = minY; y <= maxY; y++) {
                blockList.add(pos.add(x, y, minZ));
                blockList.add(pos.add(x, y, maxZ));
            }
        }

        for (int z = minZ + 1; z <= maxZ - 1; z++) { //Calculate the +/- X walls
            for (int y = minY; y <= maxY; y++) {
                blockList.add(pos.add(minX, y, z));
                blockList.add(pos.add(maxX, y, z));
            }
        }

        for (int x = minX + 1; x <= maxX - 1; x++) { //Calculate the +/- Y walls
            for (int z = minZ + 1; z <= maxZ - 1; z++) {
                blockList.add(pos.add(x, maxY, z));
                blockList.add(pos.add(x, minY, z));
            }
        }

    }

    @Override
    public void update() { //Runs every game tick (20 times a second)

        if (!doWorldLoadSetup) { //This only runs when the chunk loads
            ArrayList<Object> al = new ArrayList<Object>();
            al.add(this.getWorld()); //Add this world to the list
            al.add(this.getPos()); //Add this blockpos to the list
            PreventFFBlockBreak.ffProjectors.add(al); //Register itself with the event handler which prevents FF block breaking
            doWorldLoadSetup = true;
        }

        updateTime--;

        //Draw power from above
        if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) != null) { //If a tile entity exists above
            if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) instanceof TEPowerCube) { //If the tile entity is a power cube
                drawPower((TEPowerCube) worldObj.getTileEntity(this.getPos().add(0, 1, 0))); //Draw some power
            }
        }

        //Use 2 power/block/t
        if (power >= PowerConf.ffProjectorUsagePerBlock * blockList.size()) {
            power -= PowerConf.ffProjectorUsagePerBlock * blockList.size();
            isPowered = true;
            uptime++;
        } else {
            isPowered = false;
            uptime = 0;
        }

        //Executed every 4.95s (99t)
        if (updateTime <= 0) {
            updateTime = 99; //4.95s (99t)

            //Send info to client
            worldObj.markBlockForUpdate(this.getPos());
            markDirty();

            getBlocks(); //Calculate the blockposes of the walls

            for (BlockPos ffPos : blockList) { //Loop through every blockpos

                if (worldObj.getBlockState(ffPos) == Blocks.air.getDefaultState()) { //If the block found is air
                    if (power >= PowerConf.ffProjectorUsagePerBlockToGenerate) { //If we have enough power to place an FF block
                        worldObj.setBlockState(ffPos, ModBlocks.forcefield.getDefaultState()); //Place an FF block
                        power -= PowerConf.ffProjectorUsagePerBlockToGenerate; //Minus the power used
                    } else {
                        uptime = 0; //Reset the uptime
                    }
                } else if (worldObj.getBlockState(ffPos) == ModBlocks.forcefield.getDefaultState()) { //IF the block found id an FF
                    if (power >= PowerConf.ffProjectorUsagePerBlock * blockList.size()) { //If we have enough power to sustain the FF
                        refreshFFTimer(ffPos); //Refresh the decay timer of the FF
                    }
                }

            }

        }

    }

    //Refresh the forcefield decay timer
    private void refreshFFTimer(BlockPos ffPos) {

        TileEntity te = worldObj.getTileEntity(ffPos); //Get the tileentity

        if (te != null) { //Prevent it from trying to update forcefields that are not loaded (in an unloaded chunk)
            try {
                Field f = te.getClass().getField("decayTimer"); //Get its decayTimer
                f.setInt(te, 100); //Set it to 5s
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                LogHelper.warn("Gah! Error when trying to refresh the decayTimer of the Forcefield at " + ffPos.toString()); //This shouldn't happen ever
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //Draw power from a Power Cube
    private void drawPower(TEPowerCube powerCube) {

        double powerDrawRate = PowerConf.ffProjectorDrawRate;
        double powerMax = PowerConf.ffProjectorMaxPower;

        if (power < powerMax) { //If we have space for more power

            double pcPower; //Power Cube Power

            try {

                Field f = powerCube.getClass().getField("power");
                pcPower = f.getDouble(powerCube); //Get the power cube's power level

                if (pcPower > 0) { //If the power cube has more than 0 power
                    if (pcPower < powerDrawRate) { //If the power cube has less power than what the FF Projector draws in 1 tick
                        if (power + pcPower <= powerMax) { //If the projector's power + the power cube's power is mess than the projector's max power value
                            power += pcPower; //Draw all power from the power cube
                            f.setDouble(powerCube, 0); //Set the power cube's power level to 0
                        } else {
                            f.setDouble(powerCube, powerMax - power); //Set the power cube's power level to the difference between the projector's power and max power
                            power = powerMax; //Set the projector's power level to the max
                        }
                    } else {
                        if (power + powerDrawRate <= powerMax) { //If the projector's power + the power cube's power is mess than the projector's max power value
                            power += powerDrawRate; //Draw some power from the power cube
                            f.setDouble(powerCube, pcPower - powerDrawRate); //Minus the power drawn from the power cube
                        } else {
                            f.setDouble(powerCube, pcPower - (powerMax - power)); //Draw the power difference between the projector's power and max power
                            power = powerMax; //Set the projector's power to the max
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

    void writeSyncableDataToNBT(NBTTagCompound tagCompound) {
        tagCompound.setDouble("power", power);

        tagCompound.setInteger("x1", minX);
        tagCompound.setInteger("y1", minY);
        tagCompound.setInteger("z1", minZ);
        tagCompound.setInteger("x2", maxX);
        tagCompound.setInteger("y2", maxY);
        tagCompound.setInteger("z2", maxZ);

        tagCompound.setInteger("uptime", uptime);
        tagCompound.setString("owner", owner);
        tagCompound.setString("ownerName", ownerName);
    }

    void readSyncableDataFromNBT(NBTTagCompound tagCompound) {
        power = tagCompound.getDouble("power");

        minX = tagCompound.getInteger("x1");
        minY = tagCompound.getInteger("y1");
        minZ = tagCompound.getInteger("z1");
        maxX = tagCompound.getInteger("x2");
        maxY = tagCompound.getInteger("y2");
        maxZ = tagCompound.getInteger("z2");

        uptime = tagCompound.getInteger("uptime");
        owner = tagCompound.getString("owner");
        ownerName = tagCompound.getString("ownerName");

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
