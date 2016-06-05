package io.github.craftedcart.modularfluxfields.tileentity;

import io.github.craftedcart.modularfluxfields.crafting.CraftOverTimeResult;
import io.github.craftedcart.modularfluxfields.crafting.CrystalRefineryRecipeHandler;
import io.github.craftedcart.modularfluxfields.init.ModItems;
import io.github.craftedcart.modularfluxfields.reference.PowerConf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.*;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */
public class TECrystalRefinery extends TEPoweredBlock implements IInventory, ISidedInventory, ITickable {

    private ItemStack[] inventory;
    private String customName;

    public int progress = 0;
    public int maxProgress;

    public double speedMultiplier = 1;
    public double powerMultiplier = 1;
    private double powerTimser = 1;
    private double powerDivider = 1;

    private boolean doneWorldSetup = false;

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
    public ItemStack removeStackFromSlot(int index) {
        if (inventory[index] != null) {
            ItemStack itemstack = inventory[index];
            inventory[index] = null;
            return itemstack;
        } else {
            return null;
        }
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
        return index == 0;
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
        return this.hasCustomName() ? this.customName : "container.modularfluxfields:crystalRefinery";
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && !this.customName.equals("");
    }

    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
    }

    private String getCustomName() {
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
    }

    protected void writeSyncableDataToNBT(NBTTagCompound nbt) {
        super.writeSyncableDataToNBT(nbt);
        nbt.setInteger("progress", progress);
    }

    protected void readSyncableDataFromNBT(NBTTagCompound nbt) {
        super.writeSyncableDataToNBT(nbt);
        progress = nbt.getInteger("progress");
    }

    //<editor-fold desc="ISidedInventory Stuff">
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return index == 0;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 1;
    }
    //</editor-fold>

    private int updateTime = 100;

    @Override
    public void update() {

        if (!doneWorldSetup) {
            setup(PowerConf.crystalRefineryMaxPower, PowerConf.crystalRefineryDrawRate);
            doneWorldSetup = true;
        }

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
            if (worldObj.getTileEntity(this.getPos().add(0, 1, 0)) instanceof TEPoweredBlock) {
                drawPower((TEPoweredBlock) worldObj.getTileEntity(this.getPos().add(0, 1, 0)));
            }
        }

        ItemStack input = getStackInSlot(0);
        ItemStack output = getStackInSlot(1);
        ItemStack upgrade1 = getStackInSlot(2);
        ItemStack upgrade2 = getStackInSlot(3);
        ItemStack upgrade3 = getStackInSlot(4);
        final int baseTicksToCraft;

        //Check for upgrades
        speedMultiplier = 1;
        powerMultiplier = 1;
        powerTimser = 1;
        powerDivider = 1;
        checkForUpgrade(upgrade1);
        checkForUpgrade(upgrade2);
        checkForUpgrade(upgrade3);
        powerMultiplier *= powerTimser;
        powerMultiplier /= powerDivider;

        CraftOverTimeResult result = CrystalRefineryRecipeHandler.checkRecipe(input);
        if (result != null) {
            baseTicksToCraft = result.ticksToCraft;
            maxProgress = (int) (baseTicksToCraft / speedMultiplier);
            craftTick(output, result.result.getItem());
        } else {
            progress = 0;
            maxProgress = 0;
        }

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

}
