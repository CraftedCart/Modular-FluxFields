package io.github.craftedcart.modularfluxfields.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 21/12/2015 (DD/MM/YYYY)
 */
public class CrystalRefineryRecipeHandler {

    public static List<CrystalRefineryRecipe> recipes = new ArrayList<CrystalRefineryRecipe>();

    /**
     * Call this to register a new recipe with the Crystal Constructor
     *
     * @param ingredient The item needed to craft the result
     * @param result What is outputted if the correct ingredient is in the input slot
     * @param ticksToCraft How long this takes to craft (in ticks)
     */
    public static void addRecipe(Item ingredient, ItemStack result, int ticksToCraft) {
        recipes.add(new CrystalRefineryRecipe(ingredient, result, ticksToCraft));
    }

    /**
     * Call this to check whether the input ItemStacks should craft into something else
     *
     * @param input An ItemStack (Can be null
     * @return Returns a CraftOverTimeResult if a recipe was found. Read the result ItemStack and ticksToCraft fields of that. If no recipe was found, it returns null
     */
    public static CraftOverTimeResult checkRecipe(ItemStack input) {

        for (CrystalRefineryRecipe recipe : recipes) {

            if (recipe.doesRecipeMatch(input)) {
                return new CraftOverTimeResult(recipe.result, recipe.ticksToCraft);
            }

        }

        return null;
    }

}
