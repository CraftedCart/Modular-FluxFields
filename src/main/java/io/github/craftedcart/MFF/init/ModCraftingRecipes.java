package io.github.craftedcart.MFF.init;

import io.github.craftedcart.MFF.crafting.CrystalConstructorRecipeHandler;
import io.github.craftedcart.MFF.crafting.CrystalRefineryRecipeHandler;
import io.github.craftedcart.MFF.reference.CrystalConstructorTimings;
import io.github.craftedcart.MFF.reference.PowerConf;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by CraftedCart on 15/12/2015 (DD/MM/YYYY)
 */

public class ModCraftingRecipes {

    public static void init() {

        addCraftingTableRecipes();
        addCrystalRefineryRecipes();
        addCrystalConstructorRecipes();

    }

    private static void addCraftingTableRecipes() {

        //Add Crystal Sheet Recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.crystalSheet,
                "ER",
                "AD",

                'E', Items.emerald,
                'R', ModItems.refinedRuby,
                'A', ModItems.refinedAmethyst,
                'D', Items.diamond));

    }

    private static void addCrystalRefineryRecipes() {

        //Add Refined Amethyst Recipe
        CrystalRefineryRecipeHandler.addRecipe(ModItems.rawAmethyst, new ItemStack(ModItems.refinedAmethyst), PowerConf.crystalRefineryBaseTime);
        //Add Refined Ruby Recipe
        CrystalRefineryRecipeHandler.addRecipe(ModItems.rawRuby, new ItemStack(ModItems.refinedRuby), PowerConf.crystalRefineryBaseTime);

    }

    private static void addCrystalConstructorRecipes() {

        //Add Alurite Base Recipe
        CrystalConstructorRecipeHandler.addRecipe(2, 2, new Item[]{
                ModItems.aluriteIngot, null          ,
                null                 , Items.redstone
        }, new ItemStack(ModItems.aluriteBase), CrystalConstructorTimings.aluriteBase);

        //Add Crystal Sheet x4 Recipe
        CrystalConstructorRecipeHandler.addRecipe(2, 2, new Item[]{
                Items.emerald           , ModItems.refinedRuby,
                ModItems.refinedAmethyst, Items.diamond
        }, new ItemStack(ModItems.crystalSheet, 4), CrystalConstructorTimings.crystalSheet);

    }

}
