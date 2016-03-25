package io.github.craftedcart.modularfluxfields.client.neiplugin;

import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import io.github.craftedcart.modularfluxfields.crafting.CrystalRefineryRecipe;
import io.github.craftedcart.modularfluxfields.crafting.CrystalRefineryRecipeHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CraftedCart on 20/12/2015 (DD/MM/YYYY)
 */

public class NEICrystalRefineryCraftingHandler extends TemplateRecipeHandler implements ICraftingHandler {

    public class CachedCrystalRefineryRecipe extends CachedRecipe
    {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedCrystalRefineryRecipe(ItemStack item, ItemStack out) {
            result = new PositionedStack(out, 109, 24);
            ingredients = new ArrayList<PositionedStack>();
            setIngredient(item);
        }

        /**
         * @param itemStack The input item
         */
        public void setIngredient(ItemStack itemStack) {
            PositionedStack stack = new PositionedStack(itemStack, 41, 24, false);
            stack.setMaxSize(1);
            ingredients.add(stack);
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

        for (CrystalRefineryRecipe recipe : CrystalRefineryRecipeHandler.recipes) {

            if (result.isItemEqual(recipe.result)) {

                CachedCrystalRefineryRecipe cachedRecipe = new CachedCrystalRefineryRecipe(new ItemStack(recipe.ingredient), recipe.result);
                cachedRecipe.computeVisuals();
                arecipes.add(cachedRecipe);

            }

        }

    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        for (CrystalRefineryRecipe recipe : CrystalRefineryRecipeHandler.recipes) {

            if (ingredient.isItemEqual(new ItemStack(recipe.ingredient))) {

                CachedCrystalRefineryRecipe cachedRecipe = new CachedCrystalRefineryRecipe(ingredient, recipe.result);
                cachedRecipe.computeVisuals();
                arecipes.add(cachedRecipe);

            }

        }

    }

    @Override
    public String getGuiTexture() {
        return "modularfluxfields:textures/gui/container/crystalRefinery.png";
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal("container.modularfluxfields:crystalRefinery");
    }

    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);

        int ticksTime = 0;

        ccRecipeLoop:
        for (CrystalRefineryRecipe ccRecipe : CrystalRefineryRecipeHandler.recipes) {
            for (CachedRecipe cachedRecipe : arecipes) {
                if (cachedRecipe.getResult().item.getIsItemStackEqual(ccRecipe.result)) {
                    ticksTime = ccRecipe.ticksToCraft;
                    break ccRecipeLoop;
                }
            }
        }

        final int minsLeft = (int) Math.floor(ticksTime / 20 / 60);
        final int secsLeft = (int) Math.floor(ticksTime / 20 - minsLeft * 60);

        GuiContainerManager.getFontRenderer(null).drawString(StatCollector.translateToLocal("gui.modularfluxfields:time") + ": " + String.format("%02d : %02d", minsLeft, secsLeft),
                7, 7, 0xFF212121);

    }

}
