package io.github.craftedcart.MFF.container;

import io.github.craftedcart.MFF.tileentity.TECrystalRefinery;
import io.github.craftedcart.MFF.tileentity.TEFFProjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 30/11/2015 (DD/MM/YYYY)
 */

public class ContainerFFProjectorInfo extends Container {

    private TEFFProjector te;

    public ContainerFFProjectorInfo(IInventory playerInv, TEFFProjector te) {
        this.te = te;

        /*
         * SLOTS:
         *
         * Tile Entity --- ........  - -  -
         * Player Inventory 9-35 ..  0 - 26
         * Player Inventory 0-8 ... 27 - 36
         */

        //Tile Entity, Slot ---, Slot IDs ---

        // Player Inventory, Slot 9-35, Slot IDs 2-28
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 96 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs 29-37
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 154));
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
            if (fromSlot <= 26) {
                // From Player Inventory to Player Hotbar
                if (!this.mergeItemStack(current, 27, 36, true))
                    return null;
            } else {
                // From Player Hotbar to Player Inventory
                if (!this.mergeItemStack(current, 0, 26, false))
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
