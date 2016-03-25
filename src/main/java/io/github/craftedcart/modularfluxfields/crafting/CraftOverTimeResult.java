package io.github.craftedcart.modularfluxfields.crafting;

import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 21/12/2015 (DD/MM/YYYY)
 */
public class CraftOverTimeResult {

    public ItemStack result;
    public int ticksToCraft;

    CraftOverTimeResult(ItemStack result, int ticksToCraft) {
        this.result = result;
        this.ticksToCraft = ticksToCraft;
    }

}
