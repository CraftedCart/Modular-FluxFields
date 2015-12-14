package io.github.craftedcart.MFF.container;

import io.github.craftedcart.MFF.tileentity.TECrystalConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */

public class ContainerCrystalConstructor extends Container {

    private TECrystalConstructor te;

    public ContainerCrystalConstructor(IInventory playerInv, TECrystalConstructor te) {
        this.te = te;

        /*
         * SLOTS:
         *
         * Tile Entity 0-12 .......  0 - 12
         * Player Inventory 9-35 .. 13 - 39
         * Player Inventory 0-8 ... 40 - 48
         */

        //Tile Entity, Slot 0-1, Slot IDs 0-2
        this.addSlotToContainer(new Slot(te, 0, 30, 17)); //Input Top Left
        this.addSlotToContainer(new Slot(te, 1, 48, 17)); //Input Top Middle
        this.addSlotToContainer(new Slot(te, 2, 66, 17)); //Input Top Right
        this.addSlotToContainer(new Slot(te, 3, 30, 35)); //Input Middle Left
        this.addSlotToContainer(new Slot(te, 4, 48, 35)); //Input Middle Middle
        this.addSlotToContainer(new Slot(te, 5, 66, 35)); //Input Middle Right
        this.addSlotToContainer(new Slot(te, 6, 30, 53)); //Input Bottom Left
        this.addSlotToContainer(new Slot(te, 7, 48, 53)); //Input Bottom Middle
        this.addSlotToContainer(new Slot(te, 8, 66, 53)); //Input Bottom Right
        this.addSlotToContainer(new Slot(te, 9, 124, 35)); //Output
        this.addSlotToContainer(new Slot(te, 10, 181, 8)); //Upgrade 1
        this.addSlotToContainer(new Slot(te, 11, 181, 26)); //Upgrade 2
        this.addSlotToContainer(new Slot(te, 12, 181, 44)); //Upgrade 3

        // Player Inventory, Slot 9-35, Slot IDs 2-28
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 29-37
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
        ItemStack previous = null;
        Slot slot = (Slot) this.inventorySlots.get(fromSlot);

        if (slot != null && slot.getHasStack()) {
            ItemStack current = slot.getStack();
            previous = current.copy();

            //Custom Behaviour
            if (fromSlot <= 13) {
                // From TE Inventory to Player Inventory
                if (!this.mergeItemStack(current, 13, 49, true))
                    return null;
            } else {
                // From Player Inventory to TE Inventory
                if (!this.mergeItemStack(current, 0, 9, false))
                    return null;
            }
            //Custom Behaviour End

            if (current.stackSize == 0)
                slot.putStack(null);
            else
                slot.onSlotChanged();

            if (current.stackSize == previous.stackSize)
                return null;
            slot.onPickupFromSlot(playerIn, current);
        }
        return previous;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.te.isUseableByPlayer(playerIn);
    }

}
