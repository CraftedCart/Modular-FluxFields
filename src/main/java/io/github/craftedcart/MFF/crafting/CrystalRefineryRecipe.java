package io.github.craftedcart.MFF.crafting;

import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by CraftedCart on 21/12/2015 (DD/MM/YYYY)
 */

public class CrystalRefineryRecipe {

    public Item ingredient;
    public ItemStack result;
    public int ticksToCraft;

    /**
     * This shouldn't be called normally.
     * Call {@link CrystalRefineryRecipeHandler#addRecipe(Item, ItemStack, int)} instead
     *
     * @param ingredient The item needed to craft the result
     * @param result What is outputted if the correct ingredient is in the input slot
     * @param ticksToCraft How long this takes to craft (in ticks)
     */
    public CrystalRefineryRecipe(Item ingredient, ItemStack result, int ticksToCraft) {
        this.ingredient = ingredient;
        this.result = result;
        this.ticksToCraft = ticksToCraft;
    }

    boolean doesRecipeMatch(ItemStack input) {

        if (input != null) {
            return input.isItemEqual(new ItemStack(ingredient));
        } else {
            return false;
        }

    }

}
