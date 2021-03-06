package io.github.craftedcart.modularfluxfields.tileentity;

import io.github.craftedcart.modularfluxfields.crafting.CraftOverTimeResult;
import io.github.craftedcart.modularfluxfields.crafting.CrystalConstructorRecipeHandler;
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
 * Created by CraftedCart on 14/12/2015 (DD/MM/YYYY)
 */
public class TECrystalConstructor extends TEPoweredBlock implements IInventory, ISidedInventory, ITickable {

    private ItemStack[] inventory;
    private String customName;

    public int progress = 0;
    public int maxProgress = 0;

    public double speedMultiplier = 1;
    public double powerMultiplier = 1;
    private double powerTimser = 1;
    private double powerDivider = 1;

    private boolean doneWorldSetup = false;

    public TECrystalConstructor() {
        this.inventory = new ItemStack[this.getSizeInventory()];
    }

    //<editor-fold desc="Inventory Stuff"> //Used by IntelliJ for code folding
    @Override
    public int getSizeInventory() {
        return 13;
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
        return this.hasCustomName() ? this.customName : "container.modularfluxfields:crystalConstructor";
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

        writeSyncableDataToNBT(nbt);

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

        readSyncableDataFromNBT(nbt);

    }

    protected void writeSyncableDataToNBT(NBTTagCompound nbt) {
        super.writeSyncableDataToNBT(nbt);
        nbt.setInteger("progress", progress);
    }

    protected void readSyncableDataFromNBT(NBTTagCompound nbt) {
        super.readSyncableDataFromNBT(nbt);
        progress = nbt.getInteger("progress");
    }

    //<editor-fold desc="ISidedInventory Stuff">
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] {9};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return index == 9;
    }
    //</editor-fold>

    private int updateTime = 100;

    @Override
    public void update() {

        if (!doneWorldSetup) {
            setup(PowerConf.crystalConstructorMaxPower, PowerConf.crystalConstructorDrawRate);
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

        ItemStack input1 = getStackInSlot(0);
        ItemStack input2 = getStackInSlot(1);
        ItemStack input3 = getStackInSlot(2);
        ItemStack input4 = getStackInSlot(3);
        ItemStack input5 = getStackInSlot(4);
        ItemStack input6 = getStackInSlot(5);
        ItemStack input7 = getStackInSlot(6);
        ItemStack input8 = getStackInSlot(7);
        ItemStack input9 = getStackInSlot(8);
        ItemStack output = getStackInSlot(9);
        ItemStack upgrade1 = getStackInSlot(10);
        ItemStack upgrade2 = getStackInSlot(11);
        ItemStack upgrade3 = getStackInSlot(12);

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

        CraftOverTimeResult desiredOutput = CrystalConstructorRecipeHandler.checkRecipe(new ItemStack[]{input1, input2, input3, input4, input5, input6, input7, input8, input9});
        if (desiredOutput != null) {
            int currentCraftingTime = desiredOutput.ticksToCraft;
            maxProgress = (int) (currentCraftingTime / speedMultiplier);
            craftTick(output, desiredOutput.result.getItem(), desiredOutput.result.stackSize);
        } else {
            progress = 0;
            maxProgress = 0;
        }

    }
    

    private void craftTick(ItemStack outputSlot, Item outputItem, int amountProduced) {
        if (progress <= maxProgress && power > PowerConf.crystalConstructorUsage * powerMultiplier) {
            progress++;
            power -= PowerConf.crystalConstructorUsage * powerMultiplier;
        } else if (progress >= maxProgress) {
            //Done Crafting
            if (outputSlot != null) {
                if ((outputSlot.getItem() == outputItem && outputSlot.stackSize + amountProduced <= outputSlot.getMaxStackSize())) {
                    progress = 0;
                    decrStackSize(0, 1);
                    decrStackSize(1, 1);
                    decrStackSize(2, 1);
                    decrStackSize(3, 1);
                    decrStackSize(4, 1);
                    decrStackSize(5, 1);
                    decrStackSize(6, 1);
                    decrStackSize(7, 1);
                    decrStackSize(8, 1);
                    setInventorySlotContents(9, new ItemStack(outputItem, outputSlot.stackSize + amountProduced));
                }
            } else {
                progress = 0;
                decrStackSize(0, 1);
                decrStackSize(1, 1);
                decrStackSize(2, 1);
                decrStackSize(3, 1);
                decrStackSize(4, 1);
                decrStackSize(5, 1);
                decrStackSize(6, 1);
                decrStackSize(7, 1);
                decrStackSize(8, 1);
                setInventorySlotContents(9, new ItemStack(outputItem, amountProduced));
            }
        }
    }

    private void checkForUpgrade(ItemStack stack) {

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
