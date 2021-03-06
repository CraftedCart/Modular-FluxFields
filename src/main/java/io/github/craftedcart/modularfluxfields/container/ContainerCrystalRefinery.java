package io.github.craftedcart.modularfluxfields.container;

import io.github.craftedcart.modularfluxfields.tileentity.TECrystalRefinery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 28/11/2015 (DD/MM/YYYY)
 */
public class ContainerCrystalRefinery extends Container {

    private TECrystalRefinery te;

    public ContainerCrystalRefinery(IInventory playerInv, TECrystalRefinery te) {
        this.te = te;

        /*
         * SLOTS:
         *
         * Tile Entity 0-1 ........  0 -  4
         * Player Inventory 9-35 ..  5 - 31
         * Player Inventory 0-8 ... 32 - 41
         */

        //Tile Entity, Slot 0-1, Slot IDs 0-2
        this.addSlotToContainer(new Slot(te, 0, 46, 35)); //Input
        this.addSlotToContainer(new Slot(te, 1, 114, 35)); //Output
        this.addSlotToContainer(new Slot(te, 2, 181, 8)); //Upgrade 1
        this.addSlotToContainer(new Slot(te, 3, 181, 26)); //Upgrade 2
        this.addSlotToContainer(new Slot(te, 4, 181, 44)); //Upgrade 3

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
            if (fromSlot <= 4) {
                // From TE Inventory to Player Inventory
                if (!this.mergeItemStack(current, 5, 41, true))
                    return null;
            } else {
                // From Player Inventory to TE Inventory
                if (!this.mergeItemStack(current, 0, 1, false))
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
