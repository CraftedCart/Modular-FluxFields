package io.github.craftedcart.modularfluxfields.crafting;

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
    CrystalRefineryRecipe(Item ingredient, ItemStack result, int ticksToCraft) {
        this.ingredient = ingredient;
        this.result = result;
        this.ticksToCraft = ticksToCraft;
    }

    boolean doesRecipeMatch(ItemStack input) {

        return input != null && input.isItemEqual(new ItemStack(ingredient));

    }

}
