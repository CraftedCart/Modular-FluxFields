package io.github.craftedcart.MFF.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 21/12/2015 (DD/MM/YYYY)
 */

public class CrystalConstructorRecipe {

    public int width;
    public int height;
    public Item[] ingredients;
    public ItemStack result;
    public int ticksToCraft;

    /**
     * This shouldn't be called normally.
     * Call {@link io.github.craftedcart.MFF.crafting.CrystalConstructorRecipeHandler#addRecipe(int, int, Item[], ItemStack, int)} instead
     *
     * @param width How many slots wide the recipe is (Min: 2, Max: 3)
     * @param height How many slots tall the recipe is (Min: 2, Max: 3)
     * @param ingredients The stuff needed to craft the result
     * @param result What is outputted if the correct ingredient are in the correct places
     * @param ticksToCraft How long this takes to craft (in ticks)
     */
    public CrystalConstructorRecipe(int width, int height, Item[] ingredients, ItemStack result, int ticksToCraft) {
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.result = result;
        this.ticksToCraft = ticksToCraft;
    }

    boolean doesRecipeMatch(ItemStack[] input) {

        List<List<Integer>> slotsToTest = new ArrayList<List<Integer>>();

        //<editor-fold desc="Get the slots for the sized recipe">
        if (width == 2 && height == 2) {
            int[] offsets = new int[]{0, 1, 3, 4};
            for (int i = 0; i < offsets.length; i++) {
                List<Integer> slots = new ArrayList<Integer>();
                slots.add(offsets[i]);
                slots.add(1 + offsets[i]);
                slots.add(3 + offsets[i]);
                slots.add(4 + offsets[i]);
                slotsToTest.add(slots);
            }

        } else if (width == 3 && height == 2) {
            int[] offsets = new int[]{0, 3};
            for (int i = 0; i < offsets.length; i++) {
                List<Integer> slots = new ArrayList<Integer>();
                slots.add(offsets[i]);
                slots.add(1 + offsets[i]);
                slots.add(2 + offsets[i]);
                slots.add(3 + offsets[i]);
                slots.add(4 + offsets[i]);
                slots.add(5 + offsets[i]);
                slotsToTest.add(slots);
            }

        } else if (width == 2 && height == 3) {
            int[] offsets = new int[]{0, 1};
            for (int i = 0; i < offsets.length; i++) {
                List<Integer> slots = new ArrayList<Integer>();
                slots.add(offsets[i]);
                slots.add(1 + offsets[i]);
                slots.add(3 + offsets[i]);
                slots.add(4 + offsets[i]);
                slots.add(6 + offsets[i]);
                slots.add(7 + offsets[i]);
                slotsToTest.add(slots);
            }

        } else if (width == 3 && height == 3) {
            List<Integer> slots = new ArrayList<Integer>();
            for (int i = 0; i < 9; i++) {
                slots.add(i);
            }
            slotsToTest.add(slots);
        }
        //</editor-fold>

        slotsLoop:
        for (List<Integer> slots : slotsToTest) {

            int ingredientSlot = 0;

            for (int i = 0; i < 9; i++) {

                if (input[i] == null && slots.contains(i)) {
                    if (ingredients[ingredientSlot] != null) {
                        continue slotsLoop;
                    } else {
                        ingredientSlot++;
                    }
                } else if (input[i] != null && !slots.contains(i)) {
                    continue slotsLoop;
                } else if (input[i] == null && !slots.contains(i)) {
                    //continue slotsLoop; (Not needed, as it's the last statement)
                } else if (input[i] != null && ingredients[ingredientSlot] == null) {
                    continue slotsLoop;
                } else if (input[i] == null && ingredients[ingredientSlot] != null) {
                    continue slotsLoop;
                } else if (!input[i].isItemEqual(new ItemStack(ingredients[ingredientSlot]))) {
                    continue slotsLoop;
                } else {
                    ingredientSlot++;
                }

            }

            return true;

        }

        return false;
    }

}
