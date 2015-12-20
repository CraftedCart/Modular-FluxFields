package io.github.craftedcart.MFF.client.neiplugin;

import codechicken.nei.PositionedStack;
import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.ICraftingHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
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

    public class CachedCrystalRefineryRecipe extends CachedRecipe
    {
        public ArrayList<PositionedStack> ingredients;
        public PositionedStack result;

        public CachedCrystalRefineryRecipe(int width, int height, Object[] items, ItemStack out) {
            result = new PositionedStack(out, 119, 24);
            ingredients = new ArrayList<PositionedStack>();
            setIngredients(width, height, items);
        }

        /**
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

        if (result.getItem() instanceof ItemAluriteBase) {
            //Add Alurite Base recipe
            CachedCrystalRefineryRecipe recipeAluriteBase = new CachedCrystalRefineryRecipe(2, 2,
                    new ItemStack[]{
                            new ItemStack(ModItems.aluriteIngot), null,
                            null, new ItemStack(Items.redstone)},
                    new ItemStack(ModItems.aluriteBase));
            recipeAluriteBase.computeVisuals();
            arecipes.add(recipeAluriteBase);

        } else if (result.getItem() instanceof ItemCrystalSheet) {
            //Add Crystal Sheet recipe
            CachedCrystalRefineryRecipe recipeCrystalSheet = new CachedCrystalRefineryRecipe(2, 2,
                    new ItemStack[]{
                            new ItemStack(Items.emerald), new ItemStack(ModItems.refinedRuby),
                            new ItemStack(ModItems.refinedAmethyst), new ItemStack(Items.diamond)},
                    new ItemStack(ModItems.crystalSheet, 4));
            recipeCrystalSheet.computeVisuals();
            arecipes.add(recipeCrystalSheet);
        }

    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {

        LogHelper.info(ItemStack.areItemsEqual(ingredient, new ItemStack(Items.emerald)));

        if (ItemStack.areItemsEqual(ingredient, new ItemStack(Items.emerald)) || ingredient.getItem() instanceof ItemRefinedRuby ||
                ingredient.getItem() instanceof ItemRefinedAmethyst || ItemStack.areItemsEqual(ingredient, new ItemStack(Items.diamond))) {
            //Add Alurite Base recipe
            CachedCrystalRefineryRecipe recipeAluriteBase = new CachedCrystalRefineryRecipe(2, 2,
                    new ItemStack[]{
                            new ItemStack(Items.emerald), new ItemStack(ModItems.refinedRuby),
                            new ItemStack(ModItems.refinedAmethyst), new ItemStack(Items.diamond)},
                    new ItemStack(ModItems.crystalSheet, 4));
            recipeAluriteBase.computeVisuals();
            arecipes.add(recipeAluriteBase);

        }

        if (ingredient.getItem() instanceof ItemAluriteIngot || ItemStack.areItemsEqual(ingredient, new ItemStack(Items.redstone))) {
            //Add Crystal Sheet recipe
            CachedCrystalRefineryRecipe recipeCrystalSheet = new CachedCrystalRefineryRecipe(2, 2,
                    new ItemStack[]{
                            new ItemStack(ModItems.aluriteIngot), null,
                            null, new ItemStack(Items.redstone)},
                    new ItemStack(ModItems.aluriteBase));
            recipeCrystalSheet.computeVisuals();
            arecipes.add(recipeCrystalSheet);
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

        final int ticksTime;

        switch (recipe) {
            case 0: //Alurite Base
                ticksTime = CrystalConstructorTimings.aluriteBase;
                break;
            case 1: //Crystal Sheet
                ticksTime = CrystalConstructorTimings.crystalSheet;
                break;
            default: //Error prevention
                ticksTime = 0;
                break;
        }

        final int minsLeft = (int) Math.floor(ticksTime / 20 / 60);
        final int secsLeft = (int) Math.floor(ticksTime / 20 - minsLeft * 60);
        final int partSecsLeft = (int) (Math.abs(Math.floor(ticksTime / 20) - (ticksTime / 20)) * 100);

        GuiContainerManager.getFontRenderer(null).drawString(StatCollector.translateToLocal("gui.mff:time") + ": " + String.format("%02d : %02d . %02d", minsLeft, secsLeft, partSecsLeft),
                80, 7, 0xFF212121);

    }

}
