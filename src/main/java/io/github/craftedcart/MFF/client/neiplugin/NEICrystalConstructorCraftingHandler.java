package io.github.craftedcart.MFF.client.neiplugin;

import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import io.github.craftedcart.MFF.crafting.CrystalConstructorRecipe;
import io.github.craftedcart.MFF.crafting.CrystalConstructorRecipeHandler;
import io.github.craftedcart.MFF.init.ModItems;
import io.github.craftedcart.MFF.item.*;
import io.github.craftedcart.MFF.reference.CrystalConstructorTimings;
import io.github.craftedcart.MFF.utility.LogHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 20/12/2015 (DD/MM/YYYY)
 */

public class NEICrystalConstructorCraftingHandler extends TemplateRecipeHandler implements ICraftingHandler {

    public class CachedCrystalConstructorRecipe extends CachedRecipe
    {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedCrystalConstructorRecipe(int width, int height, Object[] items, ItemStack out) {
            result = new PositionedStack(out, 119, 24);
            ingredients = new ArrayList<PositionedStack>();
            setIngredients(width, height, items);
        }

        /**
         * @param width How many slots wide the recipe is
         * @param height How many slots tall the recipe is
         * @param items An ItemStack[] or ItemStack[][]
         */
        public void setIngredients(int width, int height, Object[] items) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (items[y * width + x] == null)
                        continue;

                    PositionedStack stack = new PositionedStack(items[y * width + x], 25 + x * 18, 6 + y * 18, false);
                    stack.setMaxSize(1);
                    ingredients.add(stack);
                }
            }
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return getCycledIngredients(cycleticks / 20, ingredients);
        }

        public PositionedStack getResult() {
            return result;
        }

        public void computeVisuals() {
            for (PositionedStack p : ingredients)
                p.generatePermutations();
        }
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {

        for (CrystalConstructorRecipe recipe : CrystalConstructorRecipeHandler.recipes) {

            if (result.isItemEqual(recipe.result)) {

                ItemStack[] ingredients = new ItemStack[recipe.ingredients.length];
                for (int i = 0; i < recipe.ingredients.length; i++) {
                    if (recipe.ingredients[i] != null) {
                        ingredients[i] = new ItemStack(recipe.ingredients[i]);
                    }
                }
                CachedCrystalConstructorRecipe cachedRecipe = new CachedCrystalConstructorRecipe(recipe.width, recipe.height, ingredients, recipe.result);
                cachedRecipe.computeVisuals();
                arecipes.add(cachedRecipe);

            }

        }

    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        for (CrystalConstructorRecipe recipe : CrystalConstructorRecipeHandler.recipes) {

            for (int i = 0; i < recipe.ingredients.length; i ++) {
                if (ingredient.isItemEqual(new ItemStack(recipe.ingredients[i]))) {

                    ItemStack[] ingredients = new ItemStack[recipe.ingredients.length];
                    for (int j = 0; j < recipe.ingredients.length; j++) {
                        if (recipe.ingredients[j] != null) {
                            ingredients[j] = new ItemStack(recipe.ingredients[j]);
                        }
                    }
                    CachedCrystalConstructorRecipe cachedRecipe = new CachedCrystalConstructorRecipe(recipe.width, recipe.height, ingredients, recipe.result);
                    cachedRecipe.computeVisuals();
                    arecipes.add(cachedRecipe);

                }
            }

        }

    }

    @Override
    public String getGuiTexture() {
        return "mff:textures/gui/container/crystalConstructor.png";
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("container.mff:crystalConstructor");
    }

    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);

        int ticksTime = 0;

        ccRecipeLoop:
        for (CrystalConstructorRecipe ccRecipe : CrystalConstructorRecipeHandler.recipes) {
            for (CachedRecipe cachedRecipe : arecipes) {
                if (cachedRecipe.getResult().item.getIsItemStackEqual(ccRecipe.result)) {
                    ticksTime = ccRecipe.ticksToCraft;
                    break ccRecipeLoop;
                }
            }
        }

        final int minsLeft = (int) Math.floor(ticksTime / 20 / 60);
        final int secsLeft = (int) Math.floor(ticksTime / 20 - minsLeft * 60);

        GuiContainerManager.getFontRenderer(null).drawString(StatCollector.translateToLocal("gui.mff:time") + ": " + String.format("%02d : %02d", minsLeft, secsLeft),
                80, 7, 0xFF212121);

    }

}
