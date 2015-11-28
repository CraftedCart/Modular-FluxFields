package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

import java.lang.reflect.Field;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */

public class TECrystalRefinery extends TileEntity implements IInventory, ISidedInventory, IUpdatePlayerListBox {

    private ItemStack[] inventory;
    private String customName;

    public double power = 0;
    public int progress = 0;
    public int maxProgress = PowerConf.crystalRefineryBaseTime;

    public double speedMultiplier = 1;
    public double powerMultiplier = 1;
    public double powerTimser = 1;
    public double powerDivider = 1;

    public TECrystalRefinery() {
        this.inventory = new ItemStack[this.getSizeInventory()];
    }


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

    private int updateTime = 100;

    @Override
    public void update() {

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

        if (input != null) {
            if (input.getItem() == ModItems.rawAmethyst) {

                if (progress <= maxProgress && power > PowerConf.crystalRefineryUsage * powerMultiplier) {
                    progress++;
                    power -= PowerConf.crystalRefineryUsage * powerMultiplier;
                } else if (progress >= maxProgress) {
                    //Done Refining
                    if (output != null) {
                        if ((output.getItem() == ModItems.refinedAmethyst && output.stackSize < output.getMaxStackSize())) {
                            progress = 0;
                            decrStackSize(0, 1);
                            setInventorySlotContents(1, new ItemStack(ModItems.refinedAmethyst, output.stackSize + 1));
                        }
                    } else {
                        progress = 0;
                        decrStackSize(0, 1);
                        setInventorySlotContents(1, new ItemStack(ModItems.refinedAmethyst, 1));
                    }
                }

            } else {
                progress = 0;
            }
        } else {
            progress = 0;
        }

        //Check for upgrades
        speedMultiplier = 1;
        powerMultiplier = 1;
        powerTimser = 1;
        powerDivider = 1;
        checkForUpgrade(upgrade1);
        checkForUpgrade(upgrade2);
        checkForUpgrade(upgrade3);
        maxProgress = (int) (PowerConf.crystalRefineryBaseTime / speedMultiplier);
        powerMultiplier *= powerTimser;
        powerMultiplier /= powerDivider;

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

            double pcPower; //Power Cube Power

            try {

                Field f = powerCube.getClass().getField("power");
                pcPower = f.getDouble(powerCube);

                if (pcPower > 0) {
                    if (pcPower < powerDrawRate) {
                        if (power + pcPower <= powerMax) {
                            power += pcPower;
                            f.setDouble(powerCube, 0);
                        } else {
                            f.setDouble(powerCube, powerMax - power);
                            power = powerMax;
                        }
                    } else {
                        if (power + powerDrawRate <= powerMax) {
                            power += powerDrawRate;
                            f.setDouble(powerCube, pcPower - powerDrawRate);
                        } else {
                            f.setDouble(powerCube, pcPower - (powerMax - power));
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

}
