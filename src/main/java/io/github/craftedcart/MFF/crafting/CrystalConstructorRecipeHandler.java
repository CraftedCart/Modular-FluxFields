package io.github.craftedcart.MFF.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 21/12/2015 (DD/MM/YYYY)
 */

public class CrystalConstructorRecipeHandler {

    public static List<CrystalConstructorRecipe> recipes = new ArrayList<CrystalConstructorRecipe>();

    /**
     * Call this to register a new recipe with the Crystal Constructor
     *
     * @param width How many slots wide the recipe is (Min: 2, Max: 3)
     * @param height How many slots tall the recipe is (Min: 2, Max: 3)
     * @param ingredients The stuff needed to craft the result
     * @param result What is outputted if the correct ingredient are in the correct places
     * @param ticksToCraft How long this takes to craft (in ticks)
     */
    public static void addRecipe(int width, int height, Item[] ingredients, ItemStack result, int ticksToCraft) {
        recipes.add(new CrystalConstructorRecipe(width, height, ingredients, result, ticksToCraft));
    }

    /**
     * Call this to check whether the input ItemStacks should craft into something else
     *
     * @param input 9 ItemStacks (Can include null objects) in an array (3 x 3 crafting grid)
     * @return Returns a CraftOverTimeResult if a recipe was found. Read the result ItemStack and ticksToCraft fields of that. If no recipe was found, it returns null
     */
    public static CraftOverTimeResult checkRecipe(ItemStack[] input) {

        for (CrystalConstructorRecipe recipe : recipes) {

            if (recipe.doesRecipeMatch(input)) {
                return new CraftOverTimeResult(recipe.result, recipe.ticksToCraft);
            }
        }

        return null;
    }

}
