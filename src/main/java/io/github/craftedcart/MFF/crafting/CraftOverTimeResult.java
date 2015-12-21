package io.github.craftedcart.MFF.crafting;

import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 21/12/2015 (DD/MM/YYYY)
 */

public class CraftOverTimeResult {

    public ItemStack result;
    public int ticksToCraft;

    public CraftOverTimeResult(ItemStack result, int ticksToCraft) {
        this.result = result;
        this.ticksToCraft = ticksToCraft;
    }

}
