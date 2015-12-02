package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.crafting.CraftOverTimeResult;
import io.github.craftedcart.MFF.crafting.CrystalRefineryRecipeHandler;
import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */

public class TECrystalRefinery extends TileEntity implements IInventory, ISidedInventory, IUpdatePlayerListBox {

    private ItemStack[] inventory;
    private String customName;

    public double power = 0;
    public int progress = 0;
    public int maxProgress;

    public double speedMultiplier = 1;
    public double powerMultiplier = 1;
    public double powerTimser = 1;
    public double powerDivider = 1;

    public TECrystalRefinery() {
        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    //<editor-fold desc="Inventory Stuff"> //Used by IntelliJ for code folding
    @Override
    public int getSizeInventory() {
        return 5;
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
        return this.hasCustomName() ? this.customName : "container.mff:crystalRefinery";
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
    //</editor-fold>

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

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

        //Not inventory stuff
        nbt.setInteger("progress", progress);
        nbt.setDouble("power", power);
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

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

        //Not inventory stuff
        progress = nbt.getInteger("progress");
        power = nbt.getInteger("power");
    }

    private void writeSyncableDataToNBT(NBTTagCompound nbt) {
        nbt.setInteger("progress", progress);
        nbt.setDouble("power", power);
    }

    private void readSyncableDataFromNBT(NBTTagCompound nbt) {
        progress = nbt.getInteger("progress");
        power = nbt.getInteger("power");
    }

    //<editor-fold desc="ISidedInventory Stuff">
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if (index == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (index == 1) {
            return true;
        } else {
            return false;
        }
    }
    //</editor-fold>

    private int updateTime = 100;

    @Override
    public void update() {

        updateTime--;

        //Executed every 5 seconds
        if (updateTime <= 0) {
            updateTime = 100; //5s (100t)

            //Send info to client
            worldObj.markBlockForUpdate(this.getPos());
            markDirty();
        }

        //Draw power from above
        if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) != null) {
            if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) instanceof TEPowerCube) {
                drawPower((TEPowerCube) worldObj.getTileEntity(this.getPos().add(0, 1, 0)));
            }
        }

        ItemStack input = getStackInSlot(0);
        ItemStack output = getStackInSlot(1);
        ItemStack upgrade1 = getStackInSlot(2);
        ItemStack upgrade2 = getStackInSlot(3);
        ItemStack upgrade3 = getStackInSlot(4);
        final int baseTicksToCraft;

        CraftOverTimeResult result = CrystalRefineryRecipeHandler.checkRecipe(input);
        if (result != null) {
            baseTicksToCraft = result.ticksToCraft;
            craftTick(output, result.result.getItem());
        } else {
            progress = 0;
            baseTicksToCraft = 0;
        }

        //Check for upgrades
        speedMultiplier = 1;
        powerMultiplier = 1;
        powerTimser = 1;
        powerDivider = 1;
        checkForUpgrade(upgrade1);
        checkForUpgrade(upgrade2);
        checkForUpgrade(upgrade3);
        maxProgress = (int) (baseTicksToCraft / speedMultiplier);
        powerMultiplier *= powerTimser;
        powerMultiplier /= powerDivider;

    }

    private void craftTick(ItemStack outputSlot, Item outputItem) {
        if (progress <= maxProgress && power > PowerConf.crystalRefineryUsage * powerMultiplier) {
            progress++;
            power -= PowerConf.crystalRefineryUsage * powerMultiplier;
        } else if (progress >= maxProgress) {
            //Done Refining
            if (outputSlot != null) {
                if ((outputSlot.getItem() == outputItem && outputSlot.stackSize < outputSlot.getMaxStackSize())) {
                    progress = 0;
                    decrStackSize(0, 1);
                    setInventorySlotContents(1, new ItemStack(outputItem, outputSlot.stackSize + 1));
                }
            } else {
                progress = 0;
                decrStackSize(0, 1);
                setInventorySlotContents(1, new ItemStack(outputItem, 1));
            }
        }
    }

    private void checkForUpgrade (ItemStack stack) {

        if (stack != null) {
            if (stack.getItem() == ModItems.speedUpgrade) {
                //Speed Upgrade
                speedMultiplier += stack.stackSize;
                powerTimser += 0.25 * stack.stackSize;
            } else if (stack.getItem() == ModItems.efficiencyUpgrade) {
                //Efficiency Upgrade
                powerDivider += 0.5 * stack.stackSize;
            }
        }

    }

    //Draw power from a Power Cube
    private void drawPower(TEPowerCube powerCube) {

        double powerDrawRate = PowerConf.crystalRefineryDrawRate;
        double powerMax = PowerConf.crystalRefineryMaxPower;

        if (power < powerMax) {

            double pcPower = powerCube.power;

            if (pcPower > 0) {
                if (pcPower < powerDrawRate) {
                    if (power + pcPower <= powerMax) {
                        power += pcPower;
                        powerCube.power = 0;
                    } else {
                        powerCube.power = powerMax - power;
                        power = powerMax;
                    }
                } else {
                    if (power + powerDrawRate <= powerMax) {
                        power += powerDrawRate;
                        powerCube.power = pcPower - powerDrawRate;
                    } else {
                        powerCube.power = pcPower - (powerMax - power);
                        power = powerMax;
                    }
                }
            }

        }

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