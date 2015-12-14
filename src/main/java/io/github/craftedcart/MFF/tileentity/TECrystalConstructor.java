package io.github.craftedcart.MFF.tileentity;

import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.item.ModItem;
import io.github.craftedcart.MFF.reference.CrystalConstructorTimings;
import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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

import java.lang.reflect.Field;

/**
 * Created by CraftedCart on 14/12/2015 (DD/MM/YYYY)
 */

public class TECrystalConstructor extends TileEntity implements IInventory, ISidedInventory, IUpdatePlayerListBox {

    private ItemStack[] inventory;
    private String customName;

    public double power = 0;
    public int progress = 0;
    public int maxProgress = 0;
    private int currentCraftingTime = 0;

    public double speedMultiplier = 1;
    public double powerMultiplier = 1;
    public double powerTimser = 1;
    public double powerDivider = 1;

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
        return this.hasCustomName() ? this.customName : "container.mff:crystalConstructor";
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
        return false;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (index == 9) {
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

        if (checkRecipe(
                input1, input2, input3, input4, input5, input6, input7, input8, input9,
                ModItems.aluriteIngot, null, null, null, Items.redstone, null, null, null, null
        ) || checkRecipe(
                input1, input2, input3, input4, input5, input6, input7, input8, input9,
                null, ModItems.aluriteIngot, null, null, null, Items.redstone, null, null, null
        ) || checkRecipe(
                input1, input2, input3, input4, input5, input6, input7, input8, input9,
                null, null, null, ModItems.aluriteIngot, null, null, null, Items.redstone, null
        ) || checkRecipe(
                input1, input2, input3, input4, input5, input6, input7, input8, input9,
                null, null, null, null, ModItems.aluriteIngot, null, null, null, Items.redstone
        )) { //Craft Alurite Base

            currentCraftingTime = CrystalConstructorTimings.aluriteBase;
            craftTick(output, ModItems.aluriteBase);

        } else {
            progress = 0;
            currentCraftingTime = 0;
        }

        //Check for upgrades
        speedMultiplier = 1;
        powerMultiplier = 1;
        powerTimser = 1;
        powerDivider = 1;
        checkForUpgrade(upgrade1);
        checkForUpgrade(upgrade2);
        checkForUpgrade(upgrade3);
        maxProgress = (int) (currentCraftingTime / speedMultiplier);
        powerMultiplier *= powerTimser;
        powerMultiplier /= powerDivider;

    }

    private boolean checkRecipe(ItemStack s1, ItemStack s2, ItemStack s3, ItemStack s4, ItemStack s5, ItemStack s6, ItemStack s7, ItemStack s8, ItemStack s9,
                             Item i1, Item i2, Item i3, Item i4, Item i5, Item i6, Item i7, Item i8, Item i9) { //S stands for slot, I stands for item

        //Check if the slot and expected item are both empty
        if (i1 == null && s1 != null || i1 != null && s1 == null) {
            return false;
        }
        if (i2 == null && s2 != null || i2 != null && s2 == null) {
            return false;
        }
        if (i3 == null && s3 != null || i3 != null && s3 == null) {
            return false;
        }
        if (i4 == null && s4 != null || i4 != null && s4 == null) {
            return false;
        }
        if (i5 == null && s5 != null || i5 != null && s5 == null) {
            return false;
        }
        if (i6 == null && s6 != null || i6 != null && s6 == null) {
            return false;
        }
        if (i7 == null && s7 != null || i7 != null && s7 == null) {
            return false;
        }
        if (i8 == null && s8 != null || i8 != null && s8 == null) {
            return false;
        }
        if (i9 == null && s9 != null || i9 != null && s9 == null) {
            return false;
        }
        
        //Check if the slot's item and the expected item are the same
        if (s1 != null && i1 != null) {
            if (s1.getItem() != i1) {
                return false;
            }
        }
        if (s2 != null && i2 != null) {
            if (s2.getItem() != i2) {
                return false;
            }
        }
        if (s3 != null && i3 != null) {
            if (s3.getItem() != i3) {
                return false;
            }
        }
        if (s4 != null && i4 != null) {
            if (s4.getItem() != i4) {
                return false;
            }
        }
        if (s5 != null && i5 != null) {
            if (s5.getItem() != i5) {
                return false;
            }
        }
        if (s6 != null && i6 != null) {
            if (s6.getItem() != i6) {
                return false;
            }
        }
        if (s7 != null && i7 != null) {
            if (s7.getItem() != i7) {
                return false;
            }
        }
        if (s8 != null && i8 != null) {
            if (s8.getItem() != i8) {
                return false;
            }
        }
        if (s9 != null && i9 != null) {
            if (s9.getItem() != i9) {
                return false;
            }
        }

        return true;

    }
    

    private void craftTick(ItemStack outputSlot, ModItem outputItem) {
        if (progress <= maxProgress && power > PowerConf.crystalConstructorUsage * powerMultiplier) {
            progress++;
            power -= PowerConf.crystalConstructorUsage * powerMultiplier;
        } else if (progress >= maxProgress) {
            //Done Refining
            if (outputSlot != null) {
                if ((outputSlot.getItem() == outputItem && outputSlot.stackSize < outputSlot.getMaxStackSize())) {
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
                    setInventorySlotContents(9, new ItemStack(outputItem, outputSlot.stackSize + 1));
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
                setInventorySlotContents(9, new ItemStack(outputItem, 1));
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

    //Draw power from a Power Cube
    private void drawPower(TEPowerCube powerCube) {

        double powerDrawRate = PowerConf.crystalConstructorDrawRate;
        double powerMax = PowerConf.crystalConstructorMaxPower;

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
